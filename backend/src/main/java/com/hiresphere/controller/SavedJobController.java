package com.hiresphere.controller;

import com.hiresphere.dto.SavedJobResponse;
import com.hiresphere.service.SavedJobService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/candidate/saved-jobs")
public class SavedJobController {

    private final SavedJobService savedJobService;

    public SavedJobController(SavedJobService savedJobService) {
        this.savedJobService = savedJobService;
    }

    // Candidate saves a job
    @PostMapping("/{jobId}")
    public ResponseEntity<Map<String, String>> saveJob(
            @PathVariable Long jobId,
            Authentication authentication) {

        String message = savedJobService.saveJob(
                authentication.getName(), jobId);

        return ResponseEntity.ok(Map.of("message", message));
    }

    // Candidate views saved jobs
    @GetMapping
    public ResponseEntity<List<SavedJobResponse>> getSavedJobs(
            Authentication authentication) {

        return ResponseEntity.ok(
                savedJobService.getSavedJobs(authentication.getName()));
    }

    // Candidate removes a saved job
    @DeleteMapping("/{jobId}")
    public ResponseEntity<Map<String, String>> removeSavedJob(
            @PathVariable Long jobId,
            Authentication authentication) {

        String message = savedJobService.removeSavedJob(
                authentication.getName(), jobId);

        return ResponseEntity.ok(Map.of("message", message));
    }
}