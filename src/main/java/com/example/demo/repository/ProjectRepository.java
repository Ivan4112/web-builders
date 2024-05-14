package com.example.demo.repository;

import com.example.demo.model.Brigade;
import com.example.demo.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    /*@Query("SELECT p FROM Project p WHERE p.idBrigade = :id")
    Project findProjectByIdBrigade(@RequestParam Integer idBrigade);*/
    List<Project> findProjectByIdBrigade(Brigade brigade);

    List<Project> findProjectByIdBrigadeAndStatus(Brigade brigade, String status);
    List<Project> findProjectsByIdBrigadeAndCostBetween(Brigade idBrigade, Double cost, Double cost2);
    List<Project> findProjectsByIdBrigadeAndCountDoneJobAfter(Brigade idBrigade, Integer countDoneJob);
    List<Project> findProjectsByIdBrigadeAndCountNotDoneJobAfter(Brigade idBrigade, Integer countNotDoneJob);

    Long countProjectByIdBrigade(Brigade idBrigade);
}
