package com.hiresphere.service;

import com.hiresphere.dto.SavedJobResponse;
import com.hiresphere.entity.Job;
import com.hiresphere.entity.Role;
import com.hiresphere.entity.SavedJob;
import com.hiresphere.entity.User;
import com.hiresphere.repository.JobRepository;
import com.hiresphere.repository.SavedJobRepository;
import com.hiresphere.repository.UserRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SavedJobService {

    private final SavedJobRepository savedJobRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public SavedJobService(
            SavedJobRepository savedJobRepository,
            JobRepository jobRepository,
            UserRepository userRepository) {

        this.savedJobRepository = savedJobRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    private User getCandidate(String email) {

        User candidate = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (candidate.getRole() != Role.CANDIDATE) {
            throw new AccessDeniedException("Only candidates can save jobs");
        }

        return candidate;
    }

    @Transactional
    public String saveJob(String email, Long jobId) {

        User candidate = getCandidate(email);

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        if (savedJobRepository.existsByCandidateAndJob(candidate, job)) {
            throw new IllegalArgumentException("Job is already saved");
        }

        SavedJob savedJob = new SavedJob();
        savedJob.setCandidate(candidate);
        savedJob.setJob(job);

        savedJobRepository.save(savedJob);

        return "Job saved successfully";
    }

    @Transactional(readOnly = true)
    public List<SavedJobResponse> getSavedJobs(String email) {

        User candidate = getCandidate(email);

        return savedJobRepository.findByCandidateOrderBySavedAtDesc(candidate)
                .stream()
                .map(SavedJobResponse::new)
                .toList();
    }

    @Transactional
    public String removeSavedJob(String email, Long jobId) {

        User candidate = getCandidate(email);

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        SavedJob savedJob = savedJobRepository.findByCandidateAndJob(candidate, job)
                .orElseThrow(() -> new IllegalArgumentException("Saved job not found"));

        savedJobRepository.delete(savedJob);

        return "Saved job removed successfully";
    }
}