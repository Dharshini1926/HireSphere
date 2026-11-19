
package com.hiresphere.service;

import com.hiresphere.dto.JobRequest;
import com.hiresphere.entity.Job;
import com.hiresphere.entity.Role;
import com.hiresphere.entity.User;
import com.hiresphere.repository.JobRepository;
import com.hiresphere.repository.UserRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public JobService(
            JobRepository jobRepository,
            UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    private User getRecruiter(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        if (user.getRole() != Role.RECRUITER) {
            throw new AccessDeniedException(
                    "Only recruiters can manage job postings");
        }

        return user;
    }

    public Job createJob(String email, JobRequest request) {

        User recruiter = getRecruiter(email);

        Job job = new Job();

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setCompanyName(request.getCompanyName());
        job.setLocation(request.getLocation());
        job.setJobType(request.getJobType());
        job.setSalaryRange(request.getSalaryRange());
        job.setExperienceRequired(request.getExperienceRequired());
        job.setRequiredSkills(request.getRequiredSkills());
        job.setRecruiter(recruiter);

        return jobRepository.save(job);
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }
    public Page<Job> getPaginatedJobs(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                sort
        );

        return jobRepository.findAll(pageRequest);
    }

    public List<Job> searchJobs(
            String keyword,
            String location,
            String jobType) {

        return jobRepository.searchJobs(
                keyword,
                location,
                jobType
        );
    }

    public List<Job> getRecruiterJobs(String email) {

        User recruiter = getRecruiter(email);

        return jobRepository.findByRecruiterId(recruiter.getId());
    }

    public Job updateJob(
            String email, Long jobId, JobRequest request) {

        User recruiter = getRecruiter(email);

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Job not found"));

        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
            throw new AccessDeniedException(
                    "You cannot update another recruiter's job");
        }

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setCompanyName(request.getCompanyName());
        job.setLocation(request.getLocation());
        job.setJobType(request.getJobType());
        job.setSalaryRange(request.getSalaryRange());
        job.setExperienceRequired(request.getExperienceRequired());
        job.setRequiredSkills(request.getRequiredSkills());

        return jobRepository.save(job);
    }

    public void deleteJob(String email, Long jobId) {

        User recruiter = getRecruiter(email);

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Job not found"));

        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
            throw new AccessDeniedException(
                    "You cannot delete another recruiter's job");
        }

        jobRepository.delete(job);
    }

    public Job getJobById(Long jobId) {

        return jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Job not found"));
    }
}
