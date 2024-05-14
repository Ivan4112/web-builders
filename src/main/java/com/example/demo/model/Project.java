package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_project", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "id_report")
    private Report idReport;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_brigade", nullable = false)
    private Brigade idBrigade;

    @Column(name = "project_name", nullable = false, length = 50)
    private String projectName;

    @Column(name = "description", length = 50)
    private String description;

    @Column(name = "cost", nullable = false)
    private Double cost;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "count_done_job", nullable = false)
    private Integer countDoneJob;

    @Column(name = "count_not_done_job", nullable = false)
    private Integer countNotDoneJob;

    @Column(name = "due_date")
    private LocalDate dueDate;

}
