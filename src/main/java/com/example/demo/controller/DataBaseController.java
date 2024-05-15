package com.example.demo.controller;

import com.example.demo.service.DataBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class DataBaseController {
    @Autowired
    DataBaseService dataBaseService;
    private static final Logger logger = LoggerFactory.getLogger(DataBaseController.class);

    @GetMapping("/manager/database")
    public String handleDatabasePostRequest() {
        return "database-service";
    }

    @PostMapping("/manager/database/backup")
    public String archiveDatabase(){
        try {
            // Execute SQL queries from file
            dataBaseService.archiveDatabase("D:/DB_Backup/sqlBackup.sql");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/manager/database";
    }

    @PostMapping("/manager/database/restore")
    public String performDatabaseOperation(
            @RequestParam("backupFile") MultipartFile backupFile) {
        String backupLocation = "D:/DB_Backup/Builders";
        if (!backupFile.isEmpty()) {
            try {
                // Perform database restore operation
                dataBaseService.restoreDatabase(backupFile, backupLocation);
                return "redirect:/manager/home";
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return "error";
            }
        } else {
            return "redirect:/manager/database";
        }
    }

}
