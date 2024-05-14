package com.example.demo.repository;

import com.example.demo.model.Workday;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkdayRepository extends JpaRepository<Workday, Integer> {
}
