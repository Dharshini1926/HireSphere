package com.hiresphere.dto;

public class AdminDashboardResponse {

    private long totalUsers;
    private long totalCandidates;
    private long totalRecruiters;
    private long totalJobs;
    private long totalApplications;

    public AdminDashboardResponse(
            long totalUsers,
            long totalCandidates,
            long totalRecruiters,
            long totalJobs,
            long totalApplications) {

        this.totalUsers = totalUsers;
        this.totalCandidates = totalCandidates;
        this.totalRecruiters = totalRecruiters;
        this.totalJobs = totalJobs;
        this.totalApplications = totalApplications;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public long getTotalCandidates() {
        return totalCandidates;
    }

    public long getTotalRecruiters() {
        return totalRecruiters;
    }

    public long getTotalJobs() {
        return totalJobs;
    }

    public long getTotalApplications() {
        return totalApplications;
    }
}