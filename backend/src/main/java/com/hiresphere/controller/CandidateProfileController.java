
package com.hiresphere.controller;

import com.hiresphere.dto.CandidateProfileRequest;
import com.hiresphere.dto.CandidateProfileResponse;
import com.hiresphere.entity.CandidateProfile;
import com.hiresphere.service.CandidateProfileService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/candidate/profile")
public class CandidateProfileController {

    private final CandidateProfileService profileService;

    public CandidateProfileController(
            CandidateProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping
    public ResponseEntity<?> createOrUpdateProfile(
            Authentication authentication,
            @Valid @RequestBody CandidateProfileRequest request) {

        CandidateProfile profile =
                profileService.createOrUpdateProfile(
                        authentication.getName(), request);

        return ResponseEntity.ok(
                Map.of(
                        "message", "Candidate profile saved successfully",
                        "profileId", profile.getId()
                )
        );
    }

    @GetMapping
    public ResponseEntity<?> getMyProfile(
            Authentication authentication) {

        CandidateProfile profile =
                profileService.getMyProfile(authentication.getName());

        return ResponseEntity.ok(
                new CandidateProfileResponse(profile)
        );
    }

    @PostMapping("/resume")
    public ResponseEntity<?> uploadResume(
            Authentication authentication,
            @RequestParam("file") MultipartFile file) throws IOException {

        CandidateProfile profile = profileService.uploadResume(
                authentication.getName(), file);

        return ResponseEntity.ok(
                Map.of(
                        "message", "Resume uploaded successfully",
                        "resumeUrl", profile.getResumeUrl()
                )
        );
    }
    @GetMapping("/resume")
    public ResponseEntity<Resource> downloadResume(
            Authentication authentication) throws IOException {

        Resource resource = profileService.downloadResume(
                authentication.getName());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"resume.pdf\""
                )
                .body(resource);
    }
}
