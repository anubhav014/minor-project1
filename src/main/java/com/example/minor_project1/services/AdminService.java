package com.example.minor_project1.services;

import com.example.minor_project1.dtos.CreateAdminRequest;
import com.example.minor_project1.models.Admin;
import com.example.minor_project1.models.Authority;
import com.example.minor_project1.models.User;
import com.example.minor_project1.repositories.AdminRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private UserService userService;
    private AdminRepository adminRepository;

    AdminService(UserService userService, AdminRepository adminRepository){
        this.userService = userService;
        this.adminRepository = adminRepository;
    }

    public Integer create(CreateAdminRequest request){

        Admin admin = request.mapToAdmin();
        User user = this.userService.create(admin.getUser(), Authority.ADMIN);
        admin.setUser(user);

        this.adminRepository.save(admin);
        return admin.getId();
    }

}
