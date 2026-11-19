
package com.hiresphere.dto;

import com.hiresphere.entity.Job;
import java.time.LocalDateTime;

public class JobResponse {

    private Long id;
    private String title;
    private String description;
    private String companyName;
    private String location;
    private String jobType;
    private String salaryRange;
    private Integer experienceRequired;
    private String requiredSkills;
    private String recruiterName;
    private LocalDateTime createdAt;

    public JobResponse(Job job) {
        this.id = job.getId();
        this.title = job.getTitle();
        this.description = job.getDescription();
        this.companyName = job.getCompanyName();
        this.location = job.getLocation();
        this.jobType = job.getJobType();
        this.salaryRange = job.getSalaryRange();
        this.experienceRequired = job.getExperienceRequired();
        this.requiredSkills = job.getRequiredSkills();
        this.recruiterName = job.getRecruiter().getFullName();
        this.createdAt = job.getCreatedAt();
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCompanyName() { return companyName; }
    public String getLocation() { return location; }
    public String getJobType() { return jobType; }
    public String getSalaryRange() { return salaryRange; }
    public Integer getExperienceRequired() {
        return experienceRequired;
    }
    public String getRequiredSkills() { return requiredSkills; }
    public String getRecruiterName() { return recruiterName; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
