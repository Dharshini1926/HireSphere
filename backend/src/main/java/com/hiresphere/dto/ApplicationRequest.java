
package com.hiresphere.dto;

import jakarta.validation.constraints.Size;

public class ApplicationRequest {

    @Size(max = 2000, message = "Cover letter cannot exceed 2000 characters")
    private String coverLetter;

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }
}
