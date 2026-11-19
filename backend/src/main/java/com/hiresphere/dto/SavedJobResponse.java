package com.hiresphere.dto;

import com.hiresphere.entity.SavedJob;
import java.time.LocalDateTime;

public class SavedJobResponse {

    private Long savedJobId;
    private Long jobId;
    private String title;
    private String companyName;
    private String location;
    private String jobType;
    private String salaryRange;
    private LocalDateTime savedAt;

    public SavedJobResponse(SavedJob savedJob) {

        this.savedJobId = savedJob.getId();
        this.jobId = savedJob.getJob().getId();
        this.title = savedJob.getJob().getTitle();
        this.companyName = savedJob.getJob().getCompanyName();
        this.location = savedJob.getJob().getLocation();
        this.jobType = savedJob.getJob().getJobType();
        this.salaryRange = savedJob.getJob().getSalaryRange();
        this.savedAt = savedJob.getSavedAt();
    }

    public Long getSavedJobId() {
        return savedJobId;
    }

    public Long getJobId() {
        return jobId;
    }

    public String getTitle() {
        return title;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getLocation() {
        return location;
    }

    public String getJobType() {
        return jobType;
    }

    public String getSalaryRange() {
        return salaryRange;
    }

    public LocalDateTime getSavedAt() {
        return savedAt;
    }
}