package com.example.demo.repository;

import com.example.demo.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Integer> {
    List<Resource> findResourceByIdProject(Project id);
}
