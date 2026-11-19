
package com.hiresphere.repository;

import com.hiresphere.entity.Application;
import com.hiresphere.entity.User;
import com.hiresphere.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByCandidate(User candidate);

    List<Application> findByJob(Job job);

    List<Application> findByJobAndStatus(Job job, String status);

    long countByJobRecruiterId(Long recruiterId);

    long countByJobRecruiterIdAndStatus(Long recruiterId, String status);

    long countByCandidateId(Long candidateId);

    long countByCandidateIdAndStatus(Long candidateId, String status);

    boolean existsByCandidateAndJob(User candidate, Job job);

    List<Application> findAllByOrderByAppliedAtDesc();
}
