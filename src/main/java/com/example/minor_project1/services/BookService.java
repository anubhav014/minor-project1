package com.example.minor_project1.services;

import com.example.minor_project1.dtos.CreateBookRequest;
import com.example.minor_project1.models.Author;
import com.example.minor_project1.models.Student;
import com.example.minor_project1.repositories.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.minor_project1.models.Book;

import java.util.List;

@Service
public class BookService {

    BookRepository bookRepository;
    AuthorService authorService;

    BookService(BookRepository bookRepository, AuthorService authorService){
        this.bookRepository = bookRepository;
        this.authorService = authorService;
    }

    public List<Book> getBooksByStudentId(Integer studentId){
        return this.bookRepository.findByStudentId(studentId);
    }

    public Book getBookId(Integer bookId){
       return this.bookRepository.findById(bookId).orElseThrow(null);
    }

    public Integer create(CreateBookRequest createBookRequest){
        /// Convert the DTO to a Book object
        Book book = createBookRequest.mapToBook();

        ///Now to save the Book, we also need to save the author

        ///Extracting author from the bool
        Author author = book.getAuthor();
        author = this.authorService.getOrCreate(author);

        ///Attach this author to the book
        book.setAuthor(author);

        ///save the book
        bookRepository.save(book);
        return book.getId();
    }

    public Book createOrUpdate(Book book){
        return this.bookRepository.save(book);
    }

    public Page<Book> getBooks(Pageable pageable, String name, String author) {
        return this.bookRepository.findBooks(name, author, pageable);
    }
}
