package com.example.minor_project1.dtos;

import com.example.minor_project1.models.Admin;
import com.example.minor_project1.models.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAdminRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String name;

    public Admin mapToAdmin(){
        return Admin.builder()
                .name(this.name)
                .user(
                        User.builder()
                                .username(this.username)
                                .password(this.password)
                                .build()
                )
                .build();
    }
}
