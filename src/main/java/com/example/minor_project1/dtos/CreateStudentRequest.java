package com.example.minor_project1.dtos;

import com.example.minor_project1.models.Student;
import com.example.minor_project1.models.User;
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
public class CreateStudentRequest {

    private String name;

    @Column(unique = true)
    private String email;

    /**
     * Why not @Column(nullable = false)??
     * If in the POST I give the body as:
     *  {
     *     "name":"Alka",
     *     "email": "alka@gmail.com",
     *     "mobile": " "
     *     }
     * @nullable=false allows whitespaces as well.
     * However, @NotBlank doesn't. However, @NotBlank needs @Valid that checks the JSON body at the client side itself.
     * */
    @NotBlank
    @Column(length = 10)
    private String mobile;

    /**
     * For Spring security ----- sign up, I also need:
     * username
     * password
     * */

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public Student mapToStudent(){
        return Student.builder()
                .name(this.name)
                .email(this.email)
                .mobile(this.mobile)
                .user(
                        User.builder() /// Since this is a DTO, we won't map the authorities.
                                .username(this.username)
                                .password(this.password)
                                .build()
                )
                .build();
    }
}
