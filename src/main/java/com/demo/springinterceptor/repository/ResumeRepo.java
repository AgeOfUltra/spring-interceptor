package com.demo.springinterceptor.repository;

import com.demo.springinterceptor.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeRepo extends JpaRepository<Resume,Long> {
}
