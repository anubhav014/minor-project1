package com.example.minor_project1.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

/// Like User, creating this Admin class to integrate spring-security

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @CreationTimestamp
    private Date createdAt;

    @OneToOne
    @JoinColumn
    @JsonIgnoreProperties("admin") ///  to avoid cyclic dependencies.
    private User user;
}
