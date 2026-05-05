package com.example.minor_project1.controllers;

import com.example.minor_project1.dtos.CreateBookRequest;
import com.example.minor_project1.models.Book;
import com.example.minor_project1.services.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {

    private BookService bookService;
    BookController(BookService bookService){
        this.bookService = bookService;
    }

    @PostMapping("")
    public void createBook(@Valid @RequestBody CreateBookRequest createBookRequest){
        this.bookService.create(createBookRequest);
    }

    /// For this GET request we only need to update the Authorization, nothing else needs to be done. Update SecurityFilterChain.
    @GetMapping("/{bookId}")
    public Book getBookById(@PathVariable("bookId") Integer bookId){
        return this.bookService.getBookId(bookId);
    }

    /**
     * Writing this GET API to implement Pagination + Filtering
     * */

    @GetMapping("")
    public Page<Book> getBooks(
            Pageable pageable,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "author", required = false) String author)
    {
        return bookService.getBooks(pageable, name, author);
    }

}
