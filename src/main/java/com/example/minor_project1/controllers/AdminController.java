package com.example.minor_project1.controllers;

import com.example.minor_project1.dtos.CreateAdminRequest;
import com.example.minor_project1.services.AdminService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    private AdminService adminService;

    AdminController(AdminService adminService){
        this.adminService = adminService;
    }

    @PostMapping("/admin")
    public Integer createAdmin(@Valid @RequestBody CreateAdminRequest createAdminRequest){
        return adminService.create(createAdminRequest);
    }
}
