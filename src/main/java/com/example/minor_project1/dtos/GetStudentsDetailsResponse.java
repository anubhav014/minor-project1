package com.example.minor_project1.dtos;

import com.example.minor_project1.models.Book;
import com.example.minor_project1.models.Student;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetStudentsDetailsResponse {

    private Student student;

    private List<Book> bookList;
}
