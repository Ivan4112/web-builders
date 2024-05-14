package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Position")
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_position", nullable = false)
    private Integer id;

    @Column(name = "position_name", nullable = false, length = 20)
    private String positionName;

    @Column(name = "salary", nullable = false)
    private Double salary;

    @OneToMany(mappedBy = "idPosition")
    private Set<Employee> employees = new LinkedHashSet<>();

}
