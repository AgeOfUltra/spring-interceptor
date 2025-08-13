package com.demo.springinterceptor.service;


import com.demo.springinterceptor.entity.Applicant;
import com.demo.springinterceptor.entity.Application;
import com.demo.springinterceptor.entity.Job;
import com.demo.springinterceptor.entity.Resume;
import com.demo.springinterceptor.repository.ApplicantRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicantService {
    @Autowired
    private ApplicantRepo applicantRepo;

    public List<Applicant> getAllApplicants() {
        return applicantRepo.findAll();
    }
    public List<Applicant> getApplicantByStatus(String status) {
        return applicantRepo.findByStatusOrderByNameAsc(status);
    }
    public List<Applicant> getApplicantByPartialName(String name) {
        return applicantRepo.findApplicantsByPartialName(name);
    }
    public Applicant saveApplicantCrud(Applicant applicant) {
//        when we are doing the bidirectional relation between the applicant and resume, we are getting the error as
//  not-null property references a null or transient value: com.demo.springinterceptor.entity.Resume.applicant because we are making nullable is false in resume for applicant.

//        to handle it, we are doing programmatic way.
//has only one résumé
        Resume resume = applicant.getResume();
        if(resume != null){
            resume.setApplicant(applicant);
        }

        //request has multiple applications, so each application can have multiple applicants
        List<Application> application = applicant.getApplications();
        if(application != null){
            application.forEach(app -> app.setApplicant(applicant));
        }

        //our applicant request has multiple jobs
//        List<Job> jobs = applicant.getJobs();
//        if(jobs != null){
//            //job also has many-to-many mapping with applicant, as it is expecting data to be list
//            List<Applicant> applicants = new ArrayList<>();
//            applicants.add(applicant);
//            jobs.forEach(job -> job.setApplicant(applicants));
//        }
        List<Job> jobs = applicant.getJobs();
        if(jobs != null){
            jobs.forEach(job -> {
                // Initialize the applicants list if it's null
                if(job.getApplicant() == null) {
                    job.setApplicant(new ArrayList<>());
                }
                // Add this applicant to the job's applicants list if not already present
                if(!job.getApplicant().contains(applicant)) {
                    job.getApplicant().add(applicant);
                }
            });
        }
        return applicantRepo.save(applicant);
    }

    public Iterable<Applicant> getApplicantsWithPagination(int page, int size) {
        return applicantRepo.findAll(PageRequest.of(page,size));
    }

}