package com.istanbul.eminonurehber.Controller;

import com.istanbul.eminonurehber.Config.JwtUtil;
import com.istanbul.eminonurehber.DTO.CompanyDTO;
import com.istanbul.eminonurehber.DTO.CompanyListForMobileIdName;
import com.istanbul.eminonurehber.DTO.CompanyMobileDTO;
import com.istanbul.eminonurehber.Entity.Company;
import com.istanbul.eminonurehber.Service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/companies")
@Tag(name = "Firma İşlemleri", description = "Kategori CRUD işlemleri")
public class CompanyController {


    private final CompanyService companyService;
    private final JwtUtil jwtUtil;

    public CompanyController(CompanyService companyService, JwtUtil jwtUtil) {
        this.companyService = companyService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping()
    public List<CompanyDTO> getCompanies(@RequestHeader(value = "Authorization",required = false) String token) {
        String role = jwtUtil.extractRoles(token).toString();  // Token'dan roller alınır
        String userEmail = jwtUtil.extractEmail(token);  // Kullanıcı email'i alınır

        if (role.contains("ADMIN")) {
            return companyService.getAllCompanies();  // Admin tüm firmaları görebilir
        } else if (role.contains("FIRMA")) {
            return companyService.getCompanyByEmail(userEmail);  // Firma sadece kendi şirketini görebilir
        }
        return Collections.emptyList();  // Diğer kullanıcılar için veri döndürülmez
    }

    @GetMapping("/mobile")
    @Operation(summary = "Tüm mobil firmaları getir")
    public List<CompanyMobileDTO> getCompaniesForMobile(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isBlank()) {
            return companyService.getAllCompaniesMobile();  // Sadece mobilde basit DTO döndür
        }
        return Collections.emptyList();
    }

    //tüm firmaların sadece id ve kategorisini getirir
    @GetMapping("/mobileIdName")
    public List<CompanyListForMobileIdName> getCompaniesIDNameForMobile(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isBlank()) {
            return companyService.getAllCompaniesIDName();  // Sadece mobilde basit DTO döndür
        }
        return Collections.emptyList();
    }

    @GetMapping("/category/{category}")
    public List<CompanyDTO> getCompaniesByCategory(@PathVariable String category) {
        return companyService.getCompaniesByCategory(category);
    }

    @GetMapping("/{id}")
    public CompanyDTO getCompany(@PathVariable Long id){
        CompanyDTO ret = companyService.getOneCompany(id).get();
       System.out.println("return mobil "+ret);
        return ret;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Sadece ADMIN şirket ekleyebilir
    public ResponseEntity<Company> createCompany(@RequestBody CompanyDTO companyDTO) {

        return ResponseEntity.ok(companyService.createCompany(companyDTO));
    }

    @DeleteMapping("/{id}")
    public void deleteCompany(@PathVariable Long id){
        companyService.deleteACompany(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyDTO> updateCompany(@PathVariable Long id, @RequestBody CompanyDTO companyDto) {
        CompanyDTO updatedCompany = companyService.updateCompany(id, companyDto);
        return ResponseEntity.ok(updatedCompany);
    }

    @PutMapping("/{id}/increment-views")
    public ResponseEntity<Void> incrementCompanyViews(@PathVariable Long id) {
       companyService.incrementCompanyClick(id);
       return ResponseEntity.ok().build();
    }





}
