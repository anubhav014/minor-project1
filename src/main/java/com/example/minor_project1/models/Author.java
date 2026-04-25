package com.example.minor_project1.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Author {

    /**
     * JPA's unidirectional Relationships
     * Bi-directional relationships
     * */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(unique = true)
    private String email;

    @CreationTimestamp
    private Date createdOn;

    /**
     * Now since we have added this attribute to the Author table, this does NOT create a column in the Author table.
     * The foreign key is already present in the Book table (author_id), because Book is the owning side.
     * We are only defining the inverse side of the relationship here.

     * In order to have a column, we explicitly need to give @JoinColumn annotation.
     * We will add this to establish a bidirectional relationship between Book <-----> Author.
     * */
    @OneToMany(mappedBy = "author") /// <---------------- Inverse side (back reference). 'author' refers to the field name in Book.
    @JsonIgnoreProperties("author") /// <--------- breaks the cyclic dependency
    private List<Book> bookList;
}
