
package com.hiresphere.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ApplicationStatusRequest {

    @NotBlank(message = "Application status is required")
    @Pattern(
            regexp = "APPLIED|SHORTLISTED|REJECTED|ACCEPTED",
            message = "Invalid application status"
    )
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
