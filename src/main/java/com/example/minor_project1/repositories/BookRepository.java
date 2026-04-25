package com.example.minor_project1.repositories;

import com.example.minor_project1.models.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findByStudentId(Integer studentId);

    @Query("SELECT b FROM Book b " +
            "WHERE (:name IS NULL OR b.name LIKE %:name%)" +
            "AND (:author IS NULL OR b.author.name LIKE %:author%)")
    Page<Book> findBooks(String name, String author, Pageable pageable);
}
