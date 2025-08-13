package com.demo.springinterceptor.repository;

import com.demo.springinterceptor.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepo extends JpaRepository<Application,Long> {
}
