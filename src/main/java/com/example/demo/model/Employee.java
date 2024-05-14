package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_employee", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_brigade", nullable = false)
    private Brigade idBrigade;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_position", nullable = false)
    private Position idPosition;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "role", nullable = false, length = 20)
    private String role;

    @Column(name = "password", nullable = false, length = 60)
    private String password;

    @Column(name = "login", nullable = false, length = 50)
    private String login;

    @Column(name = "phone_number", nullable = false, length = 10)
    private String phoneNumber;

}
