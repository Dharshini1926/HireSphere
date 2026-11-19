
package com.hiresphere.service;

import com.hiresphere.dto.CandidateProfileRequest;
import com.hiresphere.entity.CandidateProfile;
import com.hiresphere.entity.Role;
import com.hiresphere.entity.User;
import com.hiresphere.repository.CandidateProfileRepository;
import com.hiresphere.repository.UserRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

@Service
public class CandidateProfileService {

    private final CandidateProfileRepository profileRepository;
    private final UserRepository userRepository;

    public CandidateProfileService(
            CandidateProfileRepository profileRepository,
            UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    public CandidateProfile createOrUpdateProfile(
            String email,
            CandidateProfileRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        if (user.getRole() != Role.CANDIDATE) {
            throw new AccessDeniedException(
                    "Only candidates can manage candidate profiles");
        }

        CandidateProfile profile = profileRepository
                .findByUserId(user.getId())
                .orElseGet(CandidateProfile::new);

        profile.setUser(user);
        profile.setPhone(request.getPhone());
        profile.setDateOfBirth(request.getDateOfBirth());
        profile.setGender(request.getGender());
        profile.setEducation(request.getEducation());
        profile.setSkills(request.getSkills());
        profile.setExperienceYears(request.getExperienceYears());
        profile.setResumeUrl(request.getResumeUrl());
        profile.setLocation(request.getLocation());
        profile.setAbout(request.getAbout());

        return profileRepository.save(profile);
    }

    public CandidateProfile getMyProfile(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        if (user.getRole() != Role.CANDIDATE) {
            throw new AccessDeniedException(
                    "Only candidates can view candidate profiles");
        }

        return profileRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Candidate profile not found"));
    }
    public CandidateProfile uploadResume(String email, MultipartFile file)
            throws IOException {

        CandidateProfile profile = getMyProfile(email);

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Please select a resume");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("Resume must be below 5 MB");
        }

        String originalName = file.getOriginalFilename();

        if (originalName == null ||
                !originalName.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("Only PDF resumes are allowed");
        }

        byte[] header = file.getInputStream().readNBytes(5);

        if (!new String(header).equals("%PDF-")) {
            throw new IllegalArgumentException("Invalid PDF file");
        }

        Path uploadPath = Paths.get("uploads").toAbsolutePath().normalize();

        Files.createDirectories(uploadPath);

        String fileName = UUID.randomUUID() + ".pdf";

        Path filePath = uploadPath.resolve(fileName);

        Files.copy(file.getInputStream(), filePath);

        profile.setResumeUrl(fileName);

        return profileRepository.save(profile);
    }
    public Resource downloadResume(String email) throws IOException {

        CandidateProfile profile = getMyProfile(email);

        String fileName = profile.getResumeUrl();

        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("No resume uploaded");
        }

        Path uploadPath = Paths.get("uploads")
                .toAbsolutePath().normalize();

        Path filePath = uploadPath.resolve(fileName).normalize();

        if (!filePath.startsWith(uploadPath) ||
                !Files.exists(filePath) ||
                !Files.isRegularFile(filePath)) {
            throw new IllegalArgumentException("Resume file not found");
        }

        return new UrlResource(filePath.toUri());
    }
}
