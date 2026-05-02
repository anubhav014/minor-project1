package com.example.minor_project1.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
  Adding this class to make use of spring-security.
*/

/**
 * Association:
 * User <-----> Student - (1:1)
 * User <-----> Admin - (1:1)

 * Where can we store the FK??
 *  (1) User Table: {studentId and AdminId will be two foreign keys}
 *  (2) Student / Admin Table: userId will be the FK.
 *
 *  Approach 1: SELECT * FROM user WHERE username = ?{studentId} - log(n)
 *  Approach 2: SELECT * FROM student WHERE username = ? {studentId} - log(n)
 *
 *  So since any of these don't require a join, the time complexity would be similar.
 *  Neither of the query needs extra space as well, so which one to choose?

    Since we will have thousands of students and a handful of admins, if we look at the tabular structure, it would like -
 *
 *  Approach 1: user table:
 *
 *  username                password        authorities     studentId       adminID
 *  anubhav@gmail.com       ********        STUDENT             1           NULL
 *  akash@gmail.com         ********        STUDENT             2           NULL
 *  anuja@gmail.com         ********        STUDENT             3           NULL
 *  .
 *  .
 *  .
    alka@gmail.com          ********        ADMIN               NULL          1
 *  .
 *  .
 *  .
 *
 *
 *  Approach 2:
 *
 *  student table:
 *  id      name        username
 *  1       Anubhav     anubhav@gmail.com
 *  2       Akash       akash@gmail.com
 *  3       Anuja       anuja@gmail.com
 *
 *
 *  admin table:
 *
 * id       name        username
 * 1        Alka        alka@gmail.com
 *
 * Now if we look at the user table, it has most of the admin records as NULL -----> SPARSE table.
 * So user will result to a SPARSE Table.
 *
 * While the second approach is modular.
 *
 * */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User implements UserDetails {

    /// We need these 4 things: id, username, password & authorities

    //private Integer id;

    @Id
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value= EnumType.STRING)
    private Authority authorities;

    @OneToOne(mappedBy = "user") /// bidirectional relationship
    @JsonIgnoreProperties("user") ///  to avoid cyclic dependencies.
    private Student student;

    @OneToOne(mappedBy = "user")
    @JsonIgnoreProperties("user") ///  to avoid cyclic dependencies.
    private Admin admin;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.authorities.name()));
        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
