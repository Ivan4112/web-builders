package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "Logs")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "log_date")
    private LocalDate logDate;

    @Column(name = "action_taken", nullable = false, length = 50)
    private String actionTaken;

    @Column(name = "user_name", length = 50)
    private String userName;

}
