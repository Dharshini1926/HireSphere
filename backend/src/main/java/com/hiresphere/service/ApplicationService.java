
package com.hiresphere.service;

import com.hiresphere.dto.ApplicationRequest;
import com.hiresphere.dto.ApplicationResponse;
import com.hiresphere.entity.Application;
import com.hiresphere.entity.Job;
import com.hiresphere.entity.Role;
import com.hiresphere.entity.User;
import com.hiresphere.repository.ApplicationRepository;
import com.hiresphere.repository.JobRepository;
import com.hiresphere.repository.UserRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import com.hiresphere.entity.CandidateProfile;
import com.hiresphere.repository.CandidateProfileRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.hiresphere.dto.RecruiterApplicationStatsResponse;
import com.hiresphere.dto.CandidateApplicationStatsResponse;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final CandidateProfileRepository candidateProfileRepository;

    public ApplicationService(
            ApplicationRepository applicationRepository,
            JobRepository jobRepository,
            UserRepository userRepository,
            CandidateProfileRepository candidateProfileRepository) {

        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.candidateProfileRepository = candidateProfileRepository;
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Transactional
    public ApplicationResponse applyForJob(
            String email, Long jobId, ApplicationRequest request) {

        User candidate = getUser(email);

        if (candidate.getRole() != Role.CANDIDATE) {
            throw new AccessDeniedException("Only candidates can apply for jobs");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        if (applicationRepository.existsByCandidateAndJob(candidate, job)) {
            throw new IllegalArgumentException("You have already applied for this job");
        }

        Application application = new Application();
        application.setCandidate(candidate);
        application.setJob(job);
        application.setCoverLetter(request.getCoverLetter());

        Application savedApplication = applicationRepository.save(application);

        return new ApplicationResponse(savedApplication);
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> getCandidateApplications(String email) {

        User candidate = getUser(email);

        if (candidate.getRole() != Role.CANDIDATE) {
            throw new AccessDeniedException("Only candidates can view their applications");
        }

        return applicationRepository.findByCandidate(candidate)
                .stream()
                .map(ApplicationResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> getJobApplicants(String email, Long jobId) {

        User recruiter = getUser(email);

        if (recruiter.getRole() != Role.RECRUITER) {
            throw new AccessDeniedException("Only recruiters can view applicants");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
            throw new AccessDeniedException("You can only view applicants for your own jobs");
        }

        return applicationRepository.findByJob(job)
                .stream()
                .map(ApplicationResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> getJobApplicantsByStatus(
            String email, Long jobId, String status) {

        User recruiter = getUser(email);

        if (recruiter.getRole() != Role.RECRUITER) {
            throw new AccessDeniedException("Only recruiters can view applicants");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
            throw new AccessDeniedException("You can only view applicants for your own jobs");
        }

        if (!List.of("APPLIED", "SHORTLISTED", "REJECTED", "HIRED").contains(status)) {
            throw new IllegalArgumentException("Invalid application status");
        }

        return applicationRepository.findByJobAndStatus(job, status)
                .stream()
                .map(ApplicationResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public RecruiterApplicationStatsResponse getRecruiterApplicationStats(String email) {

        User recruiter = getUser(email);

        if (recruiter.getRole() != Role.RECRUITER) {
            throw new AccessDeniedException("Only recruiters can view application statistics");
        }

        Long recruiterId = recruiter.getId();

        long total = applicationRepository.countByJobRecruiterId(recruiterId);
        long applied = applicationRepository.countByJobRecruiterIdAndStatus(recruiterId, "APPLIED");
        long shortlisted = applicationRepository.countByJobRecruiterIdAndStatus(recruiterId, "SHORTLISTED");
        long rejected = applicationRepository.countByJobRecruiterIdAndStatus(recruiterId, "REJECTED");
        long hired = applicationRepository.countByJobRecruiterIdAndStatus(recruiterId, "HIRED");

        return new RecruiterApplicationStatsResponse(
                total, applied, shortlisted, rejected, hired);
    }

    @Transactional(readOnly = true)
    public CandidateApplicationStatsResponse getCandidateApplicationStats(String email) {

        User candidate = getUser(email);

        if (candidate.getRole() != Role.CANDIDATE) {
            throw new AccessDeniedException("Only candidates can view application statistics");
        }

        Long candidateId = candidate.getId();

        long total = applicationRepository.countByCandidateId(candidateId);
        long applied = applicationRepository.countByCandidateIdAndStatus(candidateId, "APPLIED");
        long shortlisted = applicationRepository.countByCandidateIdAndStatus(candidateId, "SHORTLISTED");
        long rejected = applicationRepository.countByCandidateIdAndStatus(candidateId, "REJECTED");
        long hired = applicationRepository.countByCandidateIdAndStatus(candidateId, "HIRED");

        return new CandidateApplicationStatsResponse(
                total, applied, shortlisted, rejected, hired);
    }

    @Transactional
    public ApplicationResponse updateApplicationStatus(
            String email, Long applicationId, String status) {

        User recruiter = getUser(email);

        if (recruiter.getRole() != Role.RECRUITER) {
            throw new AccessDeniedException("Only recruiters can update application status");
        }

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        if (!application.getJob().getRecruiter().getId().equals(recruiter.getId())) {
            throw new AccessDeniedException("You can only update applications for your own jobs");
        }

        if (!List.of("SHORTLISTED", "REJECTED", "HIRED").contains(status)) {
            throw new IllegalArgumentException("Invalid application status");
        }

        application.setStatus(status);

        return new ApplicationResponse(applicationRepository.save(application));
    }
    @Transactional(readOnly = true)
    public Resource downloadCandidateResume(String email, Long applicationId) {

        User recruiter = getUser(email);

        if (recruiter.getRole() != Role.RECRUITER) {
            throw new AccessDeniedException("Only recruiters can download candidate resumes");
        }

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        // Verify that this application belongs to the recruiter's job
        if (!application.getJob().getRecruiter().getId().equals(recruiter.getId())) {
            throw new AccessDeniedException("You can only download resumes of your own job applicants");
        }

        // Get candidate profile
        CandidateProfile profile = candidateProfileRepository.findByUserId(
                application.getCandidate().getId()
        ).orElseThrow(() -> new IllegalArgumentException("Candidate profile not found"));

        String resumeFileName = profile.getResumeUrl();

        if (resumeFileName == null || resumeFileName.isBlank()) {
            throw new IllegalArgumentException("Candidate has not uploaded a resume");
        }

        try {
            Path uploadPath = Paths.get("uploads").toAbsolutePath().normalize();
            Path filePath = uploadPath.resolve(resumeFileName).normalize();

            if (!filePath.startsWith(uploadPath)
                    || !Files.exists(filePath)
                    || !Files.isRegularFile(filePath)) {
                throw new IllegalArgumentException("Resume file not found");
            }

            return new UrlResource(filePath.toUri());

        } catch (java.net.MalformedURLException e) {
            throw new IllegalArgumentException("Unable to access resume file");
        }
    }
}
