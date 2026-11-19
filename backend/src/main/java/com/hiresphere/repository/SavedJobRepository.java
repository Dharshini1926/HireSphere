package com.hiresphere.repository;

import com.hiresphere.entity.SavedJob;
import com.hiresphere.entity.User;
import com.hiresphere.entity.Job;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {

    boolean existsByCandidateAndJob(User candidate, Job job);

    List<SavedJob> findByCandidateOrderBySavedAtDesc(User candidate);

    Optional<SavedJob> findByCandidateAndJob(User candidate, Job job);
    boolean existsByCandidateId(Long candidateId);

}