package com.example.demo.repository;

import com.example.demo.model.Brigade;
import com.example.demo.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
   void deleteById(@NonNull Integer id);
   Employee findEmployeeByLogin(String login);

//   @Query("SELECT e FROM Employee e WHERE e.idBrigade = :brigadeId")
   List<Employee> findByIdBrigade(Brigade brigade);
}
