package com.example.minor_project1.dtos;

import com.example.minor_project1.models.Author;
import com.example.minor_project1.models.Book;
import com.example.minor_project1.models.Genre;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBookRequest {

    private Integer id;

    private Genre genre;

    private String name;

    private String authorEmail;

    private String authorName;

    public Book mapToBook(){
        return Book.builder()
                .id(this.id)
                .genre(this.genre)
                .name(this.name)
                .author(
                        Author.builder()
                                .email(this.authorEmail)
                                .name(this.authorName)
                                .build()
                )
                .build();
    }
}
