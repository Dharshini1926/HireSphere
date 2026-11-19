
package com.hiresphere.controller;

import com.hiresphere.dto.CompanyRequest;
import com.hiresphere.dto.CompanyResponse;
import com.hiresphere.service.CompanyService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recruiter/company")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    // Create company profile
    @PostMapping
    public ResponseEntity<CompanyResponse> createCompanyProfile(
            @Valid @RequestBody CompanyRequest request,
            Authentication authentication) {

        CompanyResponse response = companyService.createCompanyProfile(
                authentication.getName(), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // View company profile
    @GetMapping
    public ResponseEntity<CompanyResponse> getCompanyProfile(
            Authentication authentication) {

        return ResponseEntity.ok(
                companyService.getCompanyProfile(authentication.getName()));
    }

    // Update company profile
    @PutMapping
    public ResponseEntity<CompanyResponse> updateCompanyProfile(
            @Valid @RequestBody CompanyRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(
                companyService.updateCompanyProfile(
                        authentication.getName(), request));
    }
}
