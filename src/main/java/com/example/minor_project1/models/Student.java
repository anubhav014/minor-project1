package com.example.minor_project1.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(unique = true)
    private String email;

    @Column(unique = true, nullable = false)
    private String mobile;

    @CreationTimestamp
    private Date createdOn;

    @UpdateTimestamp
    private Date updatedOn;

    @OneToMany(mappedBy = "student") ///  student is an attribute name in Book
    @JsonIgnoreProperties("student")
    private List<Book> bookList;

    @OneToMany(mappedBy = "student")
    @JsonIgnoreProperties("student")
    private List<Transaction> transactions;

    @Enumerated(value = EnumType.STRING)
    private StudentStatus status;

    @JoinColumn
    @OneToOne
    @JsonIgnoreProperties("student") ///  to avoid cyclic dependencies.
    private User user;
    /**
     * I had thought of these properties as well, however, they somehow don't fit.
     * If you see, 1 book can be issued to 1 student therefore it's good to have this property kept in Book.
     * Because here we can't establish a relationship of which book were issued on what date
     * Similarly all these property doesn't make more sense in terms of Students.
     *
    private Date issuedOn;

    private Date returnedOn;

    @Column(unique = true)
    private String srNum;

    */
}
