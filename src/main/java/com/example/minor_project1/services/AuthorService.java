package com.example.minor_project1.services;

import com.example.minor_project1.dtos.CreateBookRequest;
import com.example.minor_project1.models.Author;
import com.example.minor_project1.repositories.AuthorRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    AuthorRepository authorRepository;

    AuthorService(AuthorRepository authorRepository){
        this.authorRepository = authorRepository;
    }

    public Author getOrCreate(Author author){
        Author savedAuthor = this.authorRepository.findByEmail(author.getEmail());

        if(savedAuthor == null){
            author = this.authorRepository.save(author);
            return author;
        }

        return savedAuthor;
    }
}
