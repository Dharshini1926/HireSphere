
package com.hiresphere.dto;

import com.hiresphere.entity.Company;
import java.time.LocalDateTime;

public class CompanyResponse {

    private Long id;
    private String companyName;
    private String description;
    private String website;
    private String industry;
    private String location;
    private String companySize;
    private String logoUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CompanyResponse(Company company) {
        this.id = company.getId();
        this.companyName = company.getCompanyName();
        this.description = company.getDescription();
        this.website = company.getWebsite();
        this.industry = company.getIndustry();
        this.location = company.getLocation();
        this.companySize = company.getCompanySize();
        this.logoUrl = company.getLogoUrl();
        this.createdAt = company.getCreatedAt();
        this.updatedAt = company.getUpdatedAt();
    }

    public Long getId() { return id; }
    public String getCompanyName() { return companyName; }
    public String getDescription() { return description; }
    public String getWebsite() { return website; }
    public String getIndustry() { return industry; }
    public String getLocation() { return location; }
    public String getCompanySize() { return companySize; }
    public String getLogoUrl() { return logoUrl; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
