package com.example.minor_project1.dtos;

import com.example.minor_project1.models.Student;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateStudentRequest {

    /**
     * Nothing needs to be mandatory, everything is optional.
     * */
    private String name;

    private String email;

    private String mobile;

    public Student mapToStudent(){
        return Student.builder()
                .name(this.name)
                .email(this.email)
                .mobile(this.mobile)
                .build();
    }
}
