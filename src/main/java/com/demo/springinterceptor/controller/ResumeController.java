package com.demo.springinterceptor.controller;


import com.demo.springinterceptor.entity.Resume;
import com.demo.springinterceptor.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {
    @Autowired
    ResumeService resumeService;

    @PostMapping("/{applicantId}/addResume")
    public ResponseEntity<Resume> addResume(@PathVariable Long applicantId, @RequestBody Resume resume){
        return ResponseEntity.ok(resumeService.addResume(applicantId,resume));
    }

}
