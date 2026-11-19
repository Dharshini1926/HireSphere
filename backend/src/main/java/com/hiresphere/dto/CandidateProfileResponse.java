
package com.hiresphere.dto;

import com.hiresphere.entity.CandidateProfile;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CandidateProfileResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String gender;
    private String education;
    private String skills;
    private Integer experienceYears;
    private String resumeUrl;
    private String location;
    private String about;
    private LocalDateTime updatedAt;

    public CandidateProfileResponse(CandidateProfile profile) {
        this.id = profile.getId();
        this.fullName = profile.getUser().getFullName();
        this.email = profile.getUser().getEmail();
        this.phone = profile.getPhone();
        this.dateOfBirth = profile.getDateOfBirth();
        this.gender = profile.getGender();
        this.education = profile.getEducation();
        this.skills = profile.getSkills();
        this.experienceYears = profile.getExperienceYears();
        this.resumeUrl = profile.getResumeUrl();
        this.location = profile.getLocation();
        this.about = profile.getAbout();
        this.updatedAt = profile.getUpdatedAt();
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getGender() { return gender; }
    public String getEducation() { return education; }
    public String getSkills() { return skills; }
    public Integer getExperienceYears() { return experienceYears; }
    public String getResumeUrl() { return resumeUrl; }
    public String getLocation() { return location; }
    public String getAbout() { return about; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
