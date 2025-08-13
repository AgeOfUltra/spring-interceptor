package com.demo.springinterceptor.repository;

import com.demo.springinterceptor.entity.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicantRepo extends JpaRepository<Applicant,Long> {

    List<Applicant> findByStatusOrderByNameAsc(String status);

    @Query("SELECT a FROM Applicant a WHERE a.name LIKE %:name%")
    List<Applicant> findApplicantsByPartialName(@Param("name") String name);
}
