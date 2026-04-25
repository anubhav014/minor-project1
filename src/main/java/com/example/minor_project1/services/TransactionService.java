package com.example.minor_project1.services;

import com.example.minor_project1.models.*;
import com.example.minor_project1.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class TransactionService {

    @Value("${students.books.max-allowed}")
    Integer maxAllowedBooks;

    //@Value("${books.return.duration}")
    Integer returnDuration = 15;

    //@Value("${fine.per-day}")
    Integer finePerDay = 1;

    TransactionRepository transactionRepository;
    StudentService studentService;
    BookService bookService;

    TransactionService(TransactionRepository transactionRepository, StudentService studentService, BookService bookService){
        this.transactionRepository = transactionRepository;
        this.studentService = studentService;
        this.bookService = bookService;
    }

    public String initiateTransaction(Integer studentId, Integer bookId, TransactionType transactionType) throws Exception {
        return switch (transactionType) {
            case ISSUE -> initiateIssuance(studentId, bookId);
            case RETURN -> initiateReturn(studentId, bookId);
            default -> throw new Exception("Invalid transaction type.");
        };
    }

    /**
     * initiateIssuance -
     * -----------------------------    Validations    ------------------------------------
     * 1. Validate Student and Book, throw 400 / 404 if any of these details are invalid.
     * 2. Validate whether the book is available or not? - book.getStudent() != null - book is assigned to someone else.
     * 3. Validate Student's limit of issuance.
     * ------------------------------------------------------------------------------------
     * 4. Creating transaction entry in the transaction table with the status as PENDING.
     * 5. Make the book unavailable / assign it to a student so that no one else can issue the book concurrently.
     * 6. Update the Transaction entry with the status as SUCCESS.
     * 7. If there are issues in (5) or (6) then update the transaction entry with status as FAILED.
     * */
    public String initiateIssuance(Integer studentId, Integer bookId) throws Exception {
        Student student = this.studentService.getStudentsDetails(studentId, false).getStudent();
        ///1. Validating student is valid.
        if(student == null){
            throw new Exception("Student is not present.");
        }

        Book book = this.bookService.getBookId(bookId);
        /// 2. Validating book is valid and available.
        if(book == null || book.getStudent() != null){
            throw new Exception("Book is not available for issuance.");
        }

        /// 3. Validate Student's limit of issuance.
        List<Book> issuedBooks = student.getBookList();
        if(issuedBooks != null && issuedBooks.size() >= this.maxAllowedBooks){
            throw new Exception("Student has issued maximum number of books allowed.");
        }

        /// 4. Creating transaction entry in the transaction table with the status as PENDING.
        Transaction transaction = Transaction
                .builder()
                .externalTransactionId(UUID.randomUUID().toString())
                .book(book) /// adding student id as a FK
                .student(student) /// adding book id as a FK
                .transactionStatus(TransactionStatus.PENDING)
                .transactionType(TransactionType.ISSUE)
                .build();

        transaction = this.transactionRepository.save(transaction);

        /// 5. Make the book unavailable / assign it to a student so that no one else can issue the book concurrently.
        try{
            book.setStudent(student); /// Joining Book and Student table
            book = this.bookService.createOrUpdate(book);

            ///6. Update the Transaction entry with the status as SUCCESS.
            transaction.setTransactionStatus(TransactionStatus.SUCCESS);
            this.transactionRepository.save(transaction);
        }catch (Exception e){
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            this.transactionRepository.save(transaction);

            if(book.getStudent() != null){
                book.setStudent(null); /// ROLLBACK : Removing the association from the Student table for this particular record.
                this.bookService.createOrUpdate(book);
            }
        }

        return transaction.getExternalTransactionId();
    }

    /**
     * initiateReturn
     * -----------------------------    Validations    ------------------------------------
     * 1. Validate Student and Book, throw 400 / 404 if any of these details are invalid.
     * 2. Validate the book is assigned or not and if assigned then it should be assigned to that particular student.
     * ------------------------------------------------------------------------------------
     * 3. Creating transaction entry in the transaction table with the status as PENDING.
     * 4. Make the book available / unassign it from the student so that others can issue the book concurrently.
     * 5. Update the bookList for the student.
     * 6. Update the Transaction entry with the status as SUCCESS.
     * 7. If there are issues in (5) or (6) then update the transaction entry with status as FAILED.
     * */
    public String initiateReturn(Integer studentId, Integer bookId) throws Exception {

        Student student = this.studentService.getStudentsDetails(studentId, false).getStudent();
        Book book = this.bookService.getBookId(bookId);

        if(student == null){
            throw new Exception("Student is not present.");
        }

        Student stuBook = book.getStudent();

        if(book == null || stuBook == null || stuBook.getId() != studentId){
            throw new Exception("Book is not present.");
        }

       Integer fine =  this.calculateFine(student, book);

        Transaction transaction = Transaction
                .builder()
                .student(student)
                .book(book)
                .externalTransactionId(UUID.randomUUID().toString())
                .transactionStatus(TransactionStatus.PENDING)
                .transactionType(TransactionType.RETURN)
                .fine(fine)
                .build();

        transaction = this.transactionRepository.save(transaction);

        try{
            book.setStudent(null);
            book = this.bookService.createOrUpdate(book);

            transaction.setTransactionStatus(TransactionStatus.SUCCESS);
            this.transactionRepository.save(transaction);
        }catch (Exception e){
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            this.transactionRepository.save(transaction);

            if(book.getStudent() == null){
                book.setStudent(student); /// ROLLBACK
                this.bookService.createOrUpdate(book);
            }
        }

        return transaction.getExternalTransactionId();
       //return null;

    }
    /**
     * 1. Get the Issuance transaction
     * 2. Calculate the time taken from the transaction updated_on time to the current time.
     * */
    public Integer calculateFine(Student student, Book book){
        /// 1. Get the Issuance transaction
        Transaction issuedTxn = this.transactionRepository.findTopByStudentAndBookAndTransactionTypeAndTransactionStatusOrderByIdDesc(student, book, TransactionType.ISSUE, TransactionStatus.SUCCESS);

        //Transaction issuedTxn = Transaction.builder().build();
        Long issuedTimeInMillis =  issuedTxn.getUpdatedOn().getTime();

        Long timePassedInMillis = System.currentTimeMillis() - issuedTimeInMillis;

        Long daysPassed = TimeUnit.DAYS.convert(timePassedInMillis, TimeUnit.MILLISECONDS);

        if(daysPassed > returnDuration){
            return (daysPassed.intValue() - returnDuration) * finePerDay;
        }

        return 0;
    }
}
