
package com.hiresphere.controller;

import com.hiresphere.dto.JobRequest;
import com.hiresphere.dto.JobResponse;
import com.hiresphere.entity.Job;
import com.hiresphere.service.JobService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recruiter/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public ResponseEntity<?> createJob(
            Authentication authentication,
            @Valid @RequestBody JobRequest request) {

        Job job = jobService.createJob(
                authentication.getName(), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of(
                        "message", "Job created successfully",
                        "jobId", job.getId()
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<JobResponse>> getRecruiterJobs(
            Authentication authentication) {

        List<JobResponse> jobs = jobService
                .getRecruiterJobs(authentication.getName())
                .stream()
                .map(JobResponse::new)
                .toList();

        return ResponseEntity.ok(jobs);
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<?> updateJob(
            Authentication authentication,
            @PathVariable Long jobId,
            @Valid @RequestBody JobRequest request) {

        Job job = jobService.updateJob(
                authentication.getName(), jobId, request);

        return ResponseEntity.ok(
                Map.of(
                        "message", "Job updated successfully",
                        "jobId", job.getId()
                )
        );
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<?> deleteJob(
            Authentication authentication,
            @PathVariable Long jobId) {

        jobService.deleteJob(authentication.getName(), jobId);

        return ResponseEntity.ok(
                Map.of("message", "Job deleted successfully")
        );
    }
}
