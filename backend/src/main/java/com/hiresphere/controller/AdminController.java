package com.hiresphere.controller;

import com.hiresphere.dto.AdminUserResponse;
import com.hiresphere.service.AdminService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.hiresphere.dto.JobResponse;
import com.hiresphere.dto.ApplicationResponse;
import com.hiresphere.dto.AdminDashboardResponse;
import java.util.Map;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/api/admin")
@Validated
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/candidates")
    public List<AdminUserResponse> getAllCandidates() {
        return adminService.getAllCandidates();
    }

    @GetMapping("/recruiters")
    public List<AdminUserResponse> getAllRecruiters() {
        return adminService.getAllRecruiters();
    }

    @GetMapping("/jobs")
    public List<JobResponse> getAllJobs() {
        return adminService.getAllJobs();
    }

    @GetMapping("/applications")
    public List<ApplicationResponse> getAllApplications() {
        return adminService.getAllApplications();
    }
    @GetMapping("/dashboard")
    public AdminDashboardResponse getDashboardStatistics() {
        return adminService.getDashboardStatistics();
    }
    @DeleteMapping("/users/{userId}")
    public Map<String, String> deleteUser(
            @PathVariable @Positive(message = "User ID must be positive") Long userId) {

        String message = adminService.deleteUser(userId);

        return Map.of("message", message);
    }
}