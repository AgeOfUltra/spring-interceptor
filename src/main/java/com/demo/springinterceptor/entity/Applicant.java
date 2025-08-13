package com.demo.springinterceptor.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Applicant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String email;

    private String phone;

    private String status;

    @OneToOne(mappedBy = "applicant",cascade = CascadeType.ALL) // first save/delete the resume first and then save the applicant
    private Resume resume; // bidirectional mapping.

    @OneToMany(mappedBy = "applicant",cascade = CascadeType.ALL)
    private List<Application> applications = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "applicant_job",
            joinColumns = @JoinColumn(name = "applicantId"),
            inverseJoinColumns = @JoinColumn(name = "jobId")
    )

/*
* The key rules are:

Only ONE side can have @JoinTable (the owning side)
The other side must use mappedBy and reference the field name from the owning side
Never use both @JoinTable and mappedBy on the same field
* */
    private List<Job> jobs = new ArrayList<>();
}
