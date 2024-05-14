package com.example.demo.controller;

import com.example.demo.aspect.Loggable;
import com.example.demo.model.Brigade;
import com.example.demo.model.Employee;
import com.example.demo.model.Project;
import com.example.demo.repository.BrigadeRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.service.EmployeeService;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@EnableWebSecurity
public class BuilderController {
    final EmployeeService employeeService;
    final EmployeeRepository employeeRepository;
    final ProjectRepository projectRepository;
    final BrigadeRepository brigadeRepository;

    @Autowired
    public BuilderController(EmployeeService employeeService,
                             EmployeeRepository employeeRepository,
                             ProjectRepository projectRepository,
                             BrigadeRepository brigadeRepository) {
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
        this.brigadeRepository = brigadeRepository;
    }

    @Loggable
    @GetMapping("/builder/editBuilder")
    public String showEditBuilderForm(Authentication authentication, Model model) {
        String username = authentication.getName();
        Employee employee = employeeRepository.findEmployeeByLogin(username);
        if (employee == null) {
            return Message.ERROR.toString();
        }
        model.addAttribute("employee", employee);
        return "edit-builder";
    }

    @Loggable
    @PostMapping("/builder/updateBuilder")
    public String updateBuilder(@RequestParam("id") Integer employeeId,
                                 @RequestParam("name") String name,
                                 @RequestParam("lastName") String lastName,
                                 @RequestParam("login") String login,
                                 @RequestParam("password") String password,
                                 @RequestParam("number") String number,
                                 RedirectAttributes redirectAttributes) {

        boolean success = employeeService.updateEmployee(employeeId, name, lastName, login, password, number);

        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Builder details updated successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update builder details.");
        }
        return  "redirect:/login"; //"redirect:/editBuilder?id=" + employeeId;
    }

    @Loggable
    @GetMapping("/builder/home")
    public String handleBuilderHome(Model model, Authentication authentication) {
        String username = authentication.getName();
        Employee employee = employeeRepository.findEmployeeByLogin(username);
        Optional<Brigade> brigade = brigadeRepository.findById(employee.getIdBrigade().getId());
        List<Project> project = projectRepository.findProjectByIdBrigade(brigade.get());

        if (project == null) {
            return "builder-error";
        }
        model.addAttribute("employee", employee);
        model.addAttribute("project", project);
        return "home_builder";
    }

    /*@PostMapping("/add-employee")
    public String addEmployee(@RequestBody Employee employee){
        employeeService.addEmployee(employee);
        return String.valueOf(Message.INFO);
    }*/
}
