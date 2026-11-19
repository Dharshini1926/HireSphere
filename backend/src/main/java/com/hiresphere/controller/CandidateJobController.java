
package com.hiresphere.controller;

import com.hiresphere.dto.JobResponse;
import com.hiresphere.service.JobService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.data.domain.Page;
import com.hiresphere.entity.Job;

@RestController
@RequestMapping("/api/candidate/jobs")
public class CandidateJobController {

    private final JobService jobService;

    public CandidateJobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllJobs() {

        List<JobResponse> jobs = jobService.getAllJobs()
                .stream()
                .map(JobResponse::new)
                .toList();

        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Job>> getPaginatedJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        return ResponseEntity.ok(
                jobService.getPaginatedJobs(
                        page, size, sortBy, direction
                )
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobResponse>> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String jobType) {

        List<JobResponse> jobs = jobService.searchJobs(
                        keyword,
                        location,
                        jobType
                )
                .stream()
                .map(JobResponse::new)
                .toList();

        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<JobResponse> getJobById(
            @PathVariable Long jobId) {

        return ResponseEntity.ok(
                new JobResponse(jobService.getJobById(jobId))
        );
    }

}
