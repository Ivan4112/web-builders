package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Service
public class DataBaseService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(DataBaseService.class);

    public void archiveDatabase(String filePath) throws IOException {
        FileSystemResource resource = new FileSystemResource(filePath);
        InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
        String sqlQuery = FileCopyUtils.copyToString(reader);
        jdbcTemplate.execute(sqlQuery);

        /*File sqlFile = new File(filePath);
        sqlFile.delete();*/
    }

    // Method to restore the database from a backup
    public void restoreDatabase(MultipartFile backupFile, String location) throws IOException {
        String sqlQuery = "RESTORE DATABASE Builders FROM DISK = '" + location + ".bak'";

        String sqlFilePath = "D:/DB_Backup/sqlRestore.sql";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(sqlFilePath))) {
            writer.write(sqlQuery);
        }
        logger.info("Sql query, {} is created.", sqlFilePath);

        // Read the SQL script from the file and execute it
        FileSystemResource resource = new FileSystemResource(sqlFilePath);
        InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
        sqlQuery = FileCopyUtils.copyToString(reader);
        jdbcTemplate.execute(sqlQuery);

        /*File sqlFile = new File(sqlFilePath);
        sqlFile.delete();*/
    }

    // Method to create a copy of the database
    public void copyDatabase() {
        // Implement the logic to create a copy of the database using JDBC or other tools
        // Example: Execute SQL query to create a copy of the database
        /*String sqlQuery = "SELECT * INTO NewDatabase.dbo FROM OldDatabase.dbo";
        jdbcTemplate.execute(sqlQuery);*/
    }
}
