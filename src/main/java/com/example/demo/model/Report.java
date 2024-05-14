package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Report")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_report", nullable = false)
    private Integer id;

    @Column(name = "report_name", nullable = false, length = 20)
    private String reportName;

    @Column(name = "date_of_created", nullable = false)
    private LocalDate dateOfCreated;

    @OneToMany(mappedBy = "idReport")
    private Set<Project> projects = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idReport")
    private Set<Workday> workdays = new LinkedHashSet<>();

}
