package com.example.demo.controller;

import com.example.demo.model.Brigade;
import com.example.demo.model.Employee;
import com.example.demo.model.Position;
import com.example.demo.model.Project;
import com.example.demo.repository.BrigadeRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.PositionRepository;
import com.example.demo.repository.ProjectRepository;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@EnableWebSecurity
public class ManagerController {

    EmployeeRepository employeeRepository;
    BrigadeRepository brigadeRepository;
    ProjectRepository projectRepository;
    RedirectAttributes redirectAttributes;
    PositionRepository positionRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    public ManagerController(EmployeeRepository employeeRepository,
                             BrigadeRepository brigadeRepository,
                             ProjectRepository projectRepository,
                             PositionRepository positionRepository,
                             PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.brigadeRepository = brigadeRepository;
        this.projectRepository = projectRepository;
        this.positionRepository = positionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("manager/projectInfo/editBuilder")
    public String editBuilder(@RequestParam("idEmployee") Integer idEmployee, Model model) {
        Optional<Employee> employee = employeeRepository.findById(idEmployee);
        if (employee.isEmpty()) {
            return redirectAttributes.addFlashAttribute("errorMessage", "Builder not found.").toString();
        }

        List<Brigade> brigades = brigadeRepository.findAll();
        List<Position> positions = positionRepository.findAll();

        model.addAttribute("employee", employee.get());
        model.addAttribute("brigades", brigades);
        model.addAttribute("positions", positions);
        return "edit-builder-by-manager";
    }

    @PostMapping("manager/projectInfo/updateEmployee")
    public String updateBuilder(@RequestParam("id") Integer employeeId,
                                @RequestParam("brigade") Integer brigadeId,
                                @RequestParam("position") Integer positionId,
                                RedirectAttributes redirectAttributes) {

        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isEmpty()) {
            return "redirect:/error";
        }

        Employee employee = optionalEmployee.get();

        Optional<Brigade> optionalBrigade = brigadeRepository.findById(brigadeId);
        Optional<Position> optionalPosition = positionRepository.findById(positionId);


        if (optionalBrigade.isEmpty() || optionalPosition.isEmpty()) {
            return "redirect:/error";
        }

        Brigade brigade = optionalBrigade.get();
        Position position = optionalPosition.get();

        employee.setIdBrigade(brigade);
        employee.setIdPosition(position);

        employeeRepository.save(employee);

        redirectAttributes.addFlashAttribute("successMessage", "Builder updated successfully.");

        return "redirect:/manager/myProjects";
    }


    @GetMapping("manager/projectInfo/deleteBuilder")
    public String deleteBuilder(@RequestParam("idEmployee") Integer idEmployee) {
        employeeRepository.deleteById(idEmployee);
        return "redirect:/manager/myProjects";
    }

    @GetMapping("manager/editProject")
    public String editProjectInfo(@RequestParam(value="projectId")Integer projectId, Model model){
        Optional<Project> project = projectRepository.findById(projectId);
        model.addAttribute("projectInfo", project.get());
        return "edit-project-form";
    }

    @PostMapping("/manager/myProjects/editProject")
    public String editProject(@RequestParam("projectId") Integer projectId,
                              @RequestParam("projectName") String projectName,
                              @RequestParam(value = "description", required = false) String description,
                              @RequestParam("cost") Double cost,
                              @RequestParam("status") String status,
                              @RequestParam("doneJob") Integer doneJob,
                              @RequestParam("uncompletedJob") Integer notDoneJob,
                              @RequestParam("dueDate") LocalDate dueDate,
                              RedirectAttributes redirectAttributes) {

        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isEmpty()) {
            return "redirect:/error";
        }

        Project project = optionalProject.get();

        project.setProjectName(projectName);
        project.setDescription(description);
        project.setCost(cost);
        project.setStatus(status);
        project.setCountDoneJob(doneJob);
        project.setCountNotDoneJob(notDoneJob);
        project.setDueDate(dueDate);

        projectRepository.save(project);

        redirectAttributes.addFlashAttribute("successMessage", "Project updated successfully.");
        return "redirect:/manager/myProjects";
    }

    @GetMapping("/manager/projectInfo")
    public String viewProjectInfo(@RequestParam(value = "projectId") Integer projectId, Model model) {
        Optional<Project> project = projectRepository.findById(projectId);
        if (project.isEmpty()) {
            return redirectAttributes.addFlashAttribute("errorMessage", "Project not found.").toString();
        }


        Optional<Brigade> brigade = brigadeRepository.findById(project.get().getIdBrigade().getId());
        if (brigade.isEmpty()) {
            return redirectAttributes.addFlashAttribute("errorMessage", "Brigade not found.").toString();
        }

        List<Employee> employees = employeeRepository.findByIdBrigade(brigade.get());

        model.addAttribute("project", project.get());
        model.addAttribute("employees", employees);
        return "projectInfo";
    }


    @GetMapping("/manager/myProjects")
    public String showEditBuilderForm(@RequestParam(value = "status", required = false) String status,
                                      @RequestParam(value = "minCost", required = false) Double minCost,
                                      @RequestParam(value = "maxCost", required = false) Double maxCost,
                                      @RequestParam(value = "minDoneJob", required = false) Integer minDoneJob,
                                      @RequestParam(value = "minNotDoneJob", required = false) Integer minNotDoneJob,
                                      Authentication authentication, Model model) {
        String username = authentication.getName();
        Employee employee = employeeRepository.findEmployeeByLogin(username);
        Optional<Brigade> brigade = brigadeRepository.findById(employee.getIdBrigade().getId());
        if (brigade.isEmpty()) {
            return redirectAttributes.addFlashAttribute("errorMessage", "Brigade not found.").toString();
        }
        List<Project> project = projectRepository.findProjectByIdBrigade(brigade.get());

        if (status != null && !status.isEmpty()) {
            project = projectRepository.findProjectByIdBrigadeAndStatus(brigade.get(), status);
        }
        if (minCost != null && maxCost != null) {
            project = projectRepository.findProjectsByIdBrigadeAndCostBetween(brigade.get(), minCost, maxCost);
        }
        if (minDoneJob != null) {
            project = projectRepository.findProjectsByIdBrigadeAndCountDoneJobAfter(brigade.get(), minDoneJob);
        }
        if (minNotDoneJob != null) {
            project = projectRepository.findProjectsByIdBrigadeAndCountNotDoneJobAfter(brigade.get(), minNotDoneJob);
        }

        model.addAttribute("projects", project);
        model.addAttribute("status", status);
        return "managers-projects";
    }

    @GetMapping("/manager/add-builder")
    public String showAddBuilderForm(Model model) {

        List<Brigade> brigadeList = brigadeRepository.findAll();
        List<Position> positionList = positionRepository.findAll();
        model.addAttribute("brigadeList", brigadeList);
        model.addAttribute("positionList", positionList);
        return "add-builder";
    }

    @PostMapping("/manager/addBuilder")
    public String addBuilder(
            @RequestParam("brigade") Integer idBrigade,
            @RequestParam("position") Integer idPosition,
            @RequestParam("name") String name,
            @RequestParam("lastName") String lastName,
            @RequestParam("role") String role,
            @RequestParam("password") String password,
            @RequestParam("login") String login,
            @RequestParam("phoneNumber") String phoneNumber) {
        Optional<Brigade> brigade = brigadeRepository.findById(idBrigade);
        Optional<Position> position = positionRepository.findById(idPosition);
        if(brigade.isEmpty() || position.isEmpty()){
            return "error";
        }
        Employee employee = new Employee();
        employee.setIdBrigade(brigade.get());
        employee.setIdPosition(position.get());
        employee.setName(name);
        employee.setLastName(lastName);
        employee.setRole(role);
        employee.setPassword(passwordEncoder.encode(password));
        employee.setLogin(login);
        employee.setPhoneNumber(phoneNumber);

        employeeRepository.save(employee);

        return "redirect:/manager/home";
    }

    @GetMapping("/manager/editForm")
    public String showEditManagerForm(Authentication authentication, Model model) {
        String username = authentication.getName();
        Employee employee = employeeRepository.findEmployeeByLogin(username);

        model.addAttribute("oldDataManager", employee);
        return "edit-manager";
    }

    @PostMapping("/manager/updateManager")
    public String updateBuilder(@RequestParam("id") Integer employeeId,
                                @RequestParam("login") String login,
                                @RequestParam("password") String password,
                                @RequestParam("number") String number) {
       Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
       if(employeeOptional.isEmpty()){
           return "error";
       }
       Employee employee = employeeOptional.get();
       employee.setPassword(passwordEncoder.encode(password));
       employee.setLogin(login);
       employee.setPhoneNumber(number);
       employeeRepository.save(employee);
       return "redirect:/login";
    }

    @GetMapping("/manager/home")
    public String handleManagerHome(Model model, Authentication authentication) {
        String username = authentication.getName();
        Employee employee = employeeRepository.findEmployeeByLogin(username);

        /*if (employee == null) {
            return "error";
        }*/

        Optional<Brigade> brigade = brigadeRepository.findById(employee.getIdBrigade().getId());

        if (brigade.isEmpty()) {
            return Message.ERROR.toString();
        }
        model.addAttribute("employee", employee);
        return "home_manager";
    }
}
