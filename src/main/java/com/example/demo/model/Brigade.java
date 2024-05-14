package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Brigade")
public class Brigade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_brigade", nullable = false)
    private Integer id;

    @Column(name = "brigade_name", nullable = false, length = 20)
    private String brigadeName;

    @OneToMany(mappedBy = "idBrigade")
    private Set<Employee> employees = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idBrigade")
    private Set<Project> projects = new LinkedHashSet<>();

}
