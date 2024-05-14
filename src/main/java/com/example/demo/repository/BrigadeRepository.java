package com.example.demo.repository;

import com.example.demo.model.Brigade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrigadeRepository extends JpaRepository<Brigade, Integer> {

}
