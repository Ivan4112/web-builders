package com.example.demo.repository;

import com.example.demo.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface LogRepository extends JpaRepository<Log, Integer> {
    List<Log> findByLogDateAfter(LocalDate date);
    List<Log> findLogByActionTakenContaining(String action);
}
