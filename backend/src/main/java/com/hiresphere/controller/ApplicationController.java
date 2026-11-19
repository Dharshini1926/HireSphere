
package com.hiresphere.controller;

import com.hiresphere.dto.ApplicationRequest;
import com.hiresphere.dto.ApplicationResponse;
import com.hiresphere.service.ApplicationService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import com.hiresphere.dto.RecruiterApplicationStatsResponse;
import com.hiresphere.dto.CandidateApplicationStatsResponse;
import com.hiresphere.dto.ApplicationStatusRequest;

@RestController
@RequestMapping
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    // Candidate applies for a job
    @PostMapping("/api/candidate/applications/{jobId}")
    public ResponseEntity<ApplicationResponse> applyForJob(
            @PathVariable Long jobId,
            @Valid @RequestBody ApplicationRequest request,
            Authentication authentication) {

        ApplicationResponse response = applicationService.applyForJob(
                authentication.getName(), jobId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Candidate views their applications
    @GetMapping("/api/candidate/applications")
    public ResponseEntity<List<ApplicationResponse>> getCandidateApplications(
            Authentication authentication) {

        return ResponseEntity.ok(
                applicationService.getCandidateApplications(authentication.getName()));
    }

    // Recruiter views applicants for their job
    @GetMapping("/api/recruiter/jobs/{jobId}/applicants")
    public ResponseEntity<List<ApplicationResponse>> getJobApplicants(
            @PathVariable Long jobId,
            Authentication authentication) {

        return ResponseEntity.ok(
                applicationService.getJobApplicants(authentication.getName(), jobId));
    }

    // Recruiter filters applicants by application status
    @GetMapping("/api/recruiter/jobs/{jobId}/applicants/status")
    public ResponseEntity<List<ApplicationResponse>> getJobApplicantsByStatus(
            @PathVariable Long jobId,
            @RequestParam String status,
            Authentication authentication) {

        return ResponseEntity.ok(
                applicationService.getJobApplicantsByStatus(
                        authentication.getName(), jobId, status));
    }


    @PutMapping("/api/recruiter/applications/{applicationId}/status")
    public ResponseEntity<ApplicationResponse> updateApplicationStatus(
            @PathVariable Long applicationId,
            @Valid @RequestBody ApplicationStatusRequest request,
            Authentication authentication) {

        ApplicationResponse response = applicationService.updateApplicationStatus(
                authentication.getName(),
                applicationId,
                request.getStatus());

        return ResponseEntity.ok(response);
    }

    // Recruiter downloads a candidate's resume
    @GetMapping("/api/recruiter/applications/{applicationId}/resume")
    public ResponseEntity<Resource> downloadCandidateResume(
            @PathVariable Long applicationId,
            Authentication authentication) {

        Resource resume = applicationService.downloadCandidateResume(
                authentication.getName(), applicationId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"candidate-resume.pdf\""
                )
                .body(resume);
    }
    // Recruiter views application statistics
    @GetMapping("/api/recruiter/applications/statistics")
    public ResponseEntity<RecruiterApplicationStatsResponse> getRecruiterApplicationStats(
            Authentication authentication) {

        return ResponseEntity.ok(
                applicationService.getRecruiterApplicationStats(
                        authentication.getName()));
    }
    // Candidate views application statistics
    @GetMapping("/api/candidate/applications/statistics")
    public ResponseEntity<CandidateApplicationStatsResponse> getCandidateApplicationStats(
            Authentication authentication) {

        return ResponseEntity.ok(
                applicationService.getCandidateApplicationStats(
                        authentication.getName()));
    }
}
