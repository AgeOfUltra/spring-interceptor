package com.demo.springinterceptor.service;

import com.demo.springinterceptor.entity.Applicant;
import com.demo.springinterceptor.entity.Application;
import com.demo.springinterceptor.repository.ApplicantRepo;
import com.demo.springinterceptor.repository.ApplicationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepo appRepo;

    @Autowired
    private ApplicantRepo applicantRepo;

    public Application saveApplication(Long applicantId,Application application) {
        Optional<Applicant> currApplicant = applicantRepo.findById(applicantId);
        if(currApplicant.isPresent()){
            application.setApplicant(currApplicant.get());
            return appRepo.save(application);
        }else{
            throw  new RuntimeException("Applicant not found with id " + applicantId);
        }

    }
}
