package com.demo.springinterceptor.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String content;

    @OneToOne // one applicant is associated with one résumé
    @JoinColumn(name = "applicantId", nullable= false)
    @JsonBackReference // Prevents infinite recursion during JSON serialization
//    @JsonIgnore// this is to prevent resume to serialize the applicant inorder to avoid the recursion(as the applicant and resume are both in bidirectional association it is causing infinite loop.)
    private Applicant applicant; // it will make the applicant id to forein key
}
