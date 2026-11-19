package com.hiresphere.dto;

public class RecruiterApplicationStatsResponse {

    private long totalApplications;
    private long applied;
    private long shortlisted;
    private long rejected;
    private long hired;

    public RecruiterApplicationStatsResponse(
            long totalApplications,
            long applied,
            long shortlisted,
            long rejected,
            long hired) {

        this.totalApplications = totalApplications;
        this.applied = applied;
        this.shortlisted = shortlisted;
        this.rejected = rejected;
        this.hired = hired;
    }

    public long getTotalApplications() {
        return totalApplications;
    }

    public long getApplied() {
        return applied;
    }

    public long getShortlisted() {
        return shortlisted;
    }

    public long getRejected() {
        return rejected;
    }

    public long getHired() {
        return hired;
    }
}