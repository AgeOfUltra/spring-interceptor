package com.demo.springinterceptor.controller;

import com.demo.springinterceptor.entity.Application;
import com.demo.springinterceptor.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/application")
public class ApplicationController {
    @Autowired
    private ApplicationService service;

    @PostMapping("/{applicantId}/saveApplication")
    public ResponseEntity<Application> saveApplication(@PathVariable long applicantId, @RequestBody Application application) {
        return ResponseEntity.ok(service.saveApplication(applicantId,application));
    }
}
