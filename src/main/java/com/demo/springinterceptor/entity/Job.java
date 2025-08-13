package com.demo.springinterceptor.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String tittle;
    private String description;

    @ManyToMany(mappedBy = "jobs")
    @JsonBackReference
    private List<Applicant> applicant= new ArrayList<>();
}
