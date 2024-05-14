package com.example.demo.controller;

import com.example.demo.model.Log;
import com.example.demo.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class LogController {
    LogRepository logRepository;

    @Autowired
    public LogController(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @GetMapping("/manager/logs")
    public String searchLogs(Model model) {
        // Search logs after the specified date
//        @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
//        List<Log> logs = logRepository.findByLogDateAfter(date);
        List<Log> logs = logRepository.findAll();
        model.addAttribute("logs", logs);
        return "logs";
    }

    @PostMapping("manager/logs/date")
    public String searchLogs(@RequestParam("date") LocalDate localDate, Model model) {
        List<Log> logs = logRepository.findByLogDateAfter(localDate);
        model.addAttribute("logs", logs);
        return "logs";
    }

    @PostMapping("/manager/logs/action")
    public String searchLogs(@RequestParam("actionTaken") String actionTaken, Model model) {
        // Perform search operation based on date and action taken
        List<Log> logs = logRepository.findLogByActionTakenContaining(actionTaken);
        model.addAttribute("logs", logs);
        return "logs";
    }

}
