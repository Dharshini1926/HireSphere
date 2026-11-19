
package com.hiresphere.repository;

import com.hiresphere.entity.Job;
import com.hiresphere.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByRecruiter(User recruiter);

    List<Job> findByRecruiterId(Long recruiterId);
    boolean existsByRecruiterId(Long recruiterId);
    List<Job> findAllByOrderByCreatedAtDesc();


    @Query("""
    SELECT j FROM Job j
    WHERE
        (:keyword IS NULL OR
         LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
         LOWER(j.companyName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
         LOWER(j.requiredSkills) LIKE LOWER(CONCAT('%', :keyword, '%')))
    AND
        (:location IS NULL OR
         LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%')))
    AND
        (:jobType IS NULL OR
         LOWER(j.jobType) = LOWER(:jobType))
    ORDER BY j.createdAt DESC
    """)
    List<Job> searchJobs(
            @Param("keyword") String keyword,
            @Param("location") String location,
            @Param("jobType") String jobType
    );
    Page<Job> findAll(Pageable pageable);
}
