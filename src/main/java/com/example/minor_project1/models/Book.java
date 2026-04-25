package com.example.minor_project1.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    /**
     * JPA Joins
     * Book : Author (N : 1)
     * Which
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(value = EnumType.STRING)
    private Genre genre;

    private String name;

    /**
     * We need to define a relationship here
     * Since We are writing this under Book so we will think of the relationship with respect to Book
     * So It has to be ----> Book TO Author
     * Since we have deiced: Many books can be written by an author
     * therefore, it has to be ManyToOne
     * */

    /**
     * From the logs:
     * Hibernate: alter table book add constraint FKklnrv3weler2ftkweewlky958 foreign key (author_id) references author (id)
     * Hibernate: alter table book add constraint FK8ik6mo7lcgguka7gglqr88in1 foreign key (student_id) references student (id)
     * */

    ///@ManyToOne(cascade = CascadeType.PERSIST) <-------------------- This would cascade the Foreign dependencies as well.
    @ManyToOne() /// <---------------- This ensures author is a FK.
    @JoinColumn /// <----------- This defines the foreign key column (author_id) in the book table.
    @JsonIgnoreProperties({"bookList", "createdOn"})
    private Author author; /// <---------- Owning side of the relationship (holds FK)

    @ManyToOne()
    @JoinColumn
    @JsonIgnoreProperties("bookList")
    private Student student;

    @CreationTimestamp /// <----------------- This allows us not to create a new Date()
    private Date createdOn;

    @UpdateTimestamp
    private Date updatedOn;

    @OneToMany(mappedBy = "book")
    @JsonIgnoreProperties("book")
    private List<Transaction> transactionList;
        /**
         * Fetch behavior depends on FetchType:
         *
         * By default:
         * - @ManyToOne → EAGER (author will be fetched with book)
         * - @OneToMany → LAZY (books will NOT be fetched with author unless accessed)
         *
         * So:
         * getBook() → author is usually fetched (EAGER by default)
         * getAuthor() → bookList is NOT fetched unless accessed (LAZY by default)
         */

    /**
     * Pros and Cons of Bidirectional relationship:
     * Merit:
     *  1. As soon as we add List<Book> in the Author and whenever we make a call to getAuthors() details,
     *      Hibernate internally will make a call to the book table and fetch the book details and attach it with the author object.

     *      e.g. if we make a call to get Author by id: It not only executes the first SQL query, but also the second one.
     *      findById() : SELECT * FROM author WHERE id = 1; (t1)
     *                   SELECT * FROM author a, book b WHERE a.id = 1 AND a.id = b.author_id; (t2)

        Con:
            1. Slight delay in getting the data from some other table.
                If you see, it made 2 calls, the first one is a simple SELECT (t1), however, the second one involves JOINS (t2)
     *              - Not much significance.
     *
     *              NOTE:
     *                       * Hibernate does NOT always fire 2 queries.
     *                         Depending on FetchType and configuration:
                                    - LAZY → second query runs only when bookList is accessed
                                    - EAGER → may use JOIN or separate query
                                So actual SQL depends on fetch strategy, not just bidirectional mapping.

     * 2. Cyclic dependency during serialization:
                            book → author → book → author → ...
     This can cause infinite recursion (e.g., in JSON serialization).
     Typically handled using:
            - @JsonManagedReference / @JsonBackReference
            - @JsonIgnore
     *
     */

}
