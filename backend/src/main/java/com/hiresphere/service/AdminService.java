package com.hiresphere.service;

import com.hiresphere.dto.AdminUserResponse;
import com.hiresphere.entity.Role;
import com.hiresphere.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import com.hiresphere.dto.JobResponse;
import com.hiresphere.entity.Job;
import com.hiresphere.repository.JobRepository;
import com.hiresphere.dto.ApplicationResponse;
import com.hiresphere.repository.ApplicationRepository;
import com.hiresphere.dto.AdminDashboardResponse;
import org.springframework.transaction.annotation.Transactional;
import com.hiresphere.entity.User;
import com.hiresphere.repository.CandidateProfileRepository;
import com.hiresphere.repository.CompanyRepository;
import com.hiresphere.repository.SavedJobRepository;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final CompanyRepository companyRepository;
    private final SavedJobRepository savedJobRepository;

    public AdminService(
            UserRepository userRepository,
            JobRepository jobRepository,
            ApplicationRepository applicationRepository,
            CandidateProfileRepository candidateProfileRepository,
            CompanyRepository companyRepository,
            SavedJobRepository savedJobRepository) {

        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
        this.candidateProfileRepository = candidateProfileRepository;
        this.companyRepository = companyRepository;
        this.savedJobRepository = savedJobRepository;
    }

    public List<AdminUserResponse> getAllCandidates() {
        return userRepository.findByRole(Role.CANDIDATE)
                .stream()
                .map(AdminUserResponse::new)
                .toList();
    }

    public List<AdminUserResponse> getAllRecruiters() {
        return userRepository.findByRole(Role.RECRUITER)
                .stream()
                .map(AdminUserResponse::new)
                .toList();
    }

    public List<JobResponse> getAllJobs() {
        return jobRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(JobResponse::new)
                .toList();
    }

    public List<ApplicationResponse> getAllApplications() {
        return applicationRepository.findAllByOrderByAppliedAtDesc()
                .stream()
                .map(ApplicationResponse::new)
                .toList();
    }
    public long getTotalUsers() {
        return userRepository.count();
    }

    public long getTotalCandidates() {
        return userRepository.countByRole(Role.CANDIDATE);
    }

    public long getTotalRecruiters() {
        return userRepository.countByRole(Role.RECRUITER);
    }

    public long getTotalJobs() {
        return jobRepository.count();
    }

    public long getTotalApplications() {
        return applicationRepository.count();
    }
    public AdminDashboardResponse getDashboardStatistics() {

        return new AdminDashboardResponse(
                getTotalUsers(),
                getTotalCandidates(),
                getTotalRecruiters(),
                getTotalJobs(),
                getTotalApplications()
        );
    }
    @Transactional
    public String deleteUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        if (user.getRole() == Role.ADMIN) {
            throw new IllegalArgumentException(
                    "Admin account cannot be deleted");
        }

        // Check candidate-related records
        if (user.getRole() == Role.CANDIDATE) {

            if (candidateProfileRepository.existsByUserId(userId)
                    || applicationRepository.countByCandidateId(userId) > 0
                    || savedJobRepository.existsByCandidateId(userId)) {

                throw new IllegalArgumentException(
                        "Cannot delete candidate. Related records exist");
            }
        }

        // Check recruiter-related records
        if (user.getRole() == Role.RECRUITER) {

            if (companyRepository.existsByRecruiterId(userId)
                    || jobRepository.existsByRecruiterId(userId)
                    || applicationRepository.countByJobRecruiterId(userId) > 0) {

                throw new IllegalArgumentException(
                        "Cannot delete recruiter. Related records exist");
            }
        }

        userRepository.delete(user);

        return "User deleted successfully";
    }
}