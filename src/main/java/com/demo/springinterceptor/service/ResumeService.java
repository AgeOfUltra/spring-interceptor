package com.demo.springinterceptor.service;

import com.demo.springinterceptor.entity.Applicant;
import com.demo.springinterceptor.entity.Resume;
import com.demo.springinterceptor.repository.ApplicantRepo;
import com.demo.springinterceptor.repository.ResumeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResumeService {

    @Autowired
    ApplicantRepo applicantRepo;

    @Autowired
    ResumeRepo resumeRepo;

    public Resume addResume(Long applicantId, Resume resume) {
        Optional<Applicant> applicantOptional = applicantRepo.findById(applicantId);
        if(applicantOptional.isPresent()) {
            Applicant applicant = applicantOptional.get();
            resume.setApplicant(applicant);
            return resumeRepo.save(resume);
        }else{
            throw  new RuntimeException("Applicant not found with id " + applicantId);
        }
    }
}
