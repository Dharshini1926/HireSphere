
package com.hiresphere.service;

import com.hiresphere.dto.CompanyRequest;
import com.hiresphere.dto.CompanyResponse;
import com.hiresphere.entity.Company;
import com.hiresphere.entity.Role;
import com.hiresphere.entity.User;
import com.hiresphere.repository.CompanyRepository;
import com.hiresphere.repository.UserRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public CompanyService(
            CompanyRepository companyRepository,
            UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    private User getRecruiter(String email) {
        User recruiter = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Recruiter not found"));

        if (recruiter.getRole() != Role.RECRUITER) {
            throw new AccessDeniedException("Only recruiters can manage company profiles");
        }

        return recruiter;
    }

    @Transactional
    public CompanyResponse createCompanyProfile(
            String email, CompanyRequest request) {

        User recruiter = getRecruiter(email);

        if (companyRepository.existsByRecruiterId(recruiter.getId())) {
            throw new IllegalArgumentException(
                    "You have already created a company profile");
        }

        Company company = new Company();
        company.setCompanyName(request.getCompanyName());
        company.setDescription(request.getDescription());
        company.setWebsite(request.getWebsite());
        company.setIndustry(request.getIndustry());
        company.setLocation(request.getLocation());
        company.setCompanySize(request.getCompanySize());
        company.setLogoUrl(request.getLogoUrl());
        company.setRecruiter(recruiter);

        return new CompanyResponse(companyRepository.save(company));
    }

    @Transactional(readOnly = true)
    public CompanyResponse getCompanyProfile(String email) {

        User recruiter = getRecruiter(email);

        Company company = companyRepository.findByRecruiterId(recruiter.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Company profile not found"));

        return new CompanyResponse(company);
    }

    @Transactional
    public CompanyResponse updateCompanyProfile(
            String email, CompanyRequest request) {

        User recruiter = getRecruiter(email);

        Company company = companyRepository.findByRecruiterId(recruiter.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Company profile not found"));

        company.setCompanyName(request.getCompanyName());
        company.setDescription(request.getDescription());
        company.setWebsite(request.getWebsite());
        company.setIndustry(request.getIndustry());
        company.setLocation(request.getLocation());
        company.setCompanySize(request.getCompanySize());
        company.setLogoUrl(request.getLogoUrl());

        return new CompanyResponse(companyRepository.save(company));
    }
}
