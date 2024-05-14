package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Resources")
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resource", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_project", nullable = false)
    private Project idProject;

    @Column(name = "name_resource", nullable = false, length = 50)
    private String nameResource;

    @Column(name = "cost", nullable = false)
    private Double cost;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

}
