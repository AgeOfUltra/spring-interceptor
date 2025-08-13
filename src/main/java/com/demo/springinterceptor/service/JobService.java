package com.demo.springinterceptor.service;

import com.demo.springinterceptor.entity.Applicant;
import com.demo.springinterceptor.entity.Job;
import com.demo.springinterceptor.repository.ApplicantRepo;
import com.demo.springinterceptor.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplicantRepo applicantRepo;

    public Job create(Job job) {
        return jobRepository.save(job);
    }

    public Applicant addJobToApplicant(long applicantId, long  jobId) {
        Optional<Applicant> applicant = applicantRepo.findById(applicantId);
        Optional<Job> jobOptional = jobRepository.findById(jobId);
        if(applicant.isPresent() && jobOptional.isPresent()) {
            applicant.get().getJobs().add(jobOptional.get());
            applicantRepo.save(applicant.get());
            return  applicant.get();
        }else{
            throw new RuntimeException("Applicant or Job not found");
        }
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Job getJobById(long id) {
        Optional<Job> job = jobRepository.findById(id);
        if(job.isPresent()) {
            return job.get();
        }else{
            throw new RuntimeException("Job not found");
        }
    }
}
