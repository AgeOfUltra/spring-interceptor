package com.demo.springinterceptor.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;
    private String position;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "applicantId",nullable = false)
    private Applicant applicant;

}
