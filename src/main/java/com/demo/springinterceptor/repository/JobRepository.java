package com.demo.springinterceptor.repository;

import com.demo.springinterceptor.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository  extends JpaRepository<Job,Long>
{

}
