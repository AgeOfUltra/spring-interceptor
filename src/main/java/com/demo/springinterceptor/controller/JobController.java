package com.demo.springinterceptor.controller;

import com.demo.springinterceptor.entity.Applicant;
import com.demo.springinterceptor.entity.Job;
import com.demo.springinterceptor.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job")
public class JobController {
    @Autowired
    private JobService service;
    @Autowired
    private JobService jobService;

    @PostMapping("/createJob")
    public ResponseEntity<Job> createJob(@RequestBody Job job){
        return ResponseEntity.ok(service.create(job));
    }

    @GetMapping("/getAllJobs")
    public ResponseEntity<List<Job>> getAllJobs(){
        List<Job> allJobs = service.getAllJobs();
        return ResponseEntity.ok(allJobs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id){
        return  ResponseEntity.ok(service.getJobById(id));
    }

    @PostMapping("/add-job-to-applicant")
    public ResponseEntity<Applicant> addJobToApplicant(@RequestParam long applicantId,@RequestParam long jobId){
        Applicant applicant = jobService.addJobToApplicant(applicantId,jobId);
        return ResponseEntity.ok(applicant);
    }
}
