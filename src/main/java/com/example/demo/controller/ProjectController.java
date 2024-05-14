package com.example.demo.controller;

import com.example.demo.model.Brigade;
import com.example.demo.model.Project;
import com.example.demo.model.Resource;
import com.example.demo.repository.BrigadeRepository;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.ResourceRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class ProjectController {

    ProjectRepository projectRepository;
    ResourceRepository resourceRepository;
    BrigadeRepository brigadeRepository;

    @Autowired
    public ProjectController(ProjectRepository projectRepository,
                             ResourceRepository resourceRepository,
                             BrigadeRepository brigadeRepository) {
        this.projectRepository = projectRepository;
        this.resourceRepository = resourceRepository;
        this.brigadeRepository=brigadeRepository;
    }

    @GetMapping("/manager/generatePDFReport")
    public ResponseEntity<ByteArrayResource> generatePDFReport(@RequestParam("projectId") Integer projectId) {
        double totalCost = 0.0, profit;
        Optional<Project> project = projectRepository.findById(projectId);

        if (project.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Resource> resourceList = resourceRepository.findResourceByIdProject(project.get());

        for (Resource resource : resourceList) {
            totalCost += resource.getCost();
        }
        profit = project.get().getCost() - totalCost;

        // Create a PDF document
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // Start a new content stream which will "hold" the PDF content
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 700);
                contentStream.setLeading(14.5f);

                contentStream.showText("Project Name: " + project.get().getProjectName());
                contentStream.newLine();

                for (Resource resource : resourceList) {
                    contentStream.showText(resource.getNameResource());
                    contentStream.showText("    ");
                    contentStream.showText(resource.getCost().toString() + "$");
                    contentStream.showText("    ");
                    contentStream.showText(resource.getQuantity().toString());
                    contentStream.newLine();
                }

                contentStream.showText("Total sum: " + totalCost + "$");
                contentStream.newLine();
                contentStream.showText("Profit: " + profit + "$");
                contentStream.newLine();

                // Add more project details as needed
                contentStream.endText();
            }

            // Save the document to a byte array
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.save(byteArrayOutputStream);

            // Upload the byte array to a location (e.g., Amazon S3, local filesystem, etc.)
            // Here, I'll demonstrate using ByteArrayResource
            ByteArrayResource resource = new ByteArrayResource(byteArrayOutputStream.toByteArray());

            // Return a ResponseEntity with the PDF file
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=project_report.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/manager/addResource")
    public String formAddResource(@RequestParam("projectId") Integer projectId, Model model){
        model.addAttribute("projectId", projectId);
        return "add-resource";
    }

    @PostMapping("/manager/addResource")
    public String addResource(@RequestParam("projectId") Integer projectId,
                              @RequestParam("nameResource") String nameResource,
                              @RequestParam("cost") Double cost,
                              @RequestParam("quantity") Integer quantity) {
        Optional<Project> project = projectRepository.findById(projectId);
        if (project.isEmpty()) {
            return "error";
        }
        Resource resource = new Resource();
        resource.setIdProject(project.get());
        resource.setNameResource(nameResource);
        resource.setCost(cost);
        resource.setQuantity(quantity);
        resourceRepository.save(resource);
        return "redirect:/manager/projectInfo?projectId="+projectId;
    }


    @GetMapping("/manager/add-project")
    public String formAddProject(Model model){
        List<Brigade> brigadeList = brigadeRepository.findAll();
        model.addAttribute("brigadeList", brigadeList);
        return "add-project";
    }

    @PostMapping("/manager/addProject")
    public String addProject(@RequestParam("brigade") Integer brigadeId,
                             @RequestParam("projectName") String projectName,
                             @RequestParam("description") String description,
                             @RequestParam("cost") Double cost,
                             @RequestParam("status") String status,
                             @RequestParam("doneJob") Integer doneJob,
                             @RequestParam("notDoneJob") Integer notDoneJob,
                             @RequestParam("dueDate") LocalDate dueDate) {

        // Check if the brigade has reached the maximum count of projects
        Brigade brigade = brigadeRepository.findById(brigadeId).orElse(null);
        if (brigade != null) {
            long projectCount = projectRepository.countProjectByIdBrigade(brigade);
            if (projectCount >= 2) {
                return "/error-add-project";
            }
        }
        Project project = new Project();
        project.setIdBrigade(brigade);
        project.setProjectName(projectName);
        project.setDescription(description);
        project.setCost(cost);
        project.setStatus(status);
        project.setCountDoneJob(doneJob);
        project.setCountNotDoneJob(notDoneJob);
        project.setDueDate(dueDate);

        projectRepository.save(project);

        return "redirect:/manager/myProjects";
    }
}
