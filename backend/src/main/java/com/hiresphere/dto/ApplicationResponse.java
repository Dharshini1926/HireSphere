
package com.hiresphere.dto;

import com.hiresphere.entity.Application;
import java.time.LocalDateTime;

public class ApplicationResponse {

    private Long id;
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private String candidateName;
    private String candidateEmail;
    private String coverLetter;
    private String status;
    private LocalDateTime appliedAt;

    public ApplicationResponse(Application application) {
        this.id = application.getId();
        this.jobId = application.getJob().getId();
        this.jobTitle = application.getJob().getTitle();
        this.companyName = application.getJob().getCompanyName();
        this.candidateName = application.getCandidate().getFullName();
        this.candidateEmail = application.getCandidate().getEmail();
        this.coverLetter = application.getCoverLetter();
        this.status = application.getStatus();
        this.appliedAt = application.getAppliedAt();
    }

    public Long getId() { return id; }
    public Long getJobId() { return jobId; }
    public String getJobTitle() { return jobTitle; }
    public String getCompanyName() { return companyName; }
    public String getCandidateName() { return candidateName; }
    public String getCandidateEmail() { return candidateEmail; }
    public String getCoverLetter() { return coverLetter; }
    public String getStatus() { return status; }
    public LocalDateTime getAppliedAt() { return appliedAt; }
}
