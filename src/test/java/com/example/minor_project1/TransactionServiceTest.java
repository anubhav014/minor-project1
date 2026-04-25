package com.example.minor_project1;

import com.example.minor_project1.models.*;
import com.example.minor_project1.repositories.TransactionRepository;
import com.example.minor_project1.services.TransactionService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

    /**
     * Unit Tests (UT) are typically written for service classes, as they contain core business logic.
     * Controllers and repositories are often not unit tested in isolation unless they contain custom logic.
     *
     * Code coverage indicates how much of the codebase is executed during testing.
     * Example:
     *   - Total lines: 100
     *   - Lines executed by tests: 70 → Coverage = 70%
     *
     * Tests should cover not only happy paths but also edge cases and failure scenarios.
     *
     * A unit test verifies that the actual output matches the expected output.
     *
     * JUnit:
     *   - Provides annotations like @Test and assertion utilities.
     *
     * Mockito:
     *   - Used to mock dependencies and simulate behavior of external components.
     *   - Helps isolate the unit under test by avoiding real database or service calls.
     */

    @InjectMocks  /// This is similar to @Component - Creates an instance of TransactionService and injects all @Mock dependencies into it.
    TransactionService transactionService;

    @Mock  /// Creates a mock (fake) instance of TransactionRepository to simulate its behavior during tests.
    TransactionRepository transactionRepository;

    @Test
    public void testCalculateFine_PositiveFine(){
        Book book = Book.builder()
                .id(5)
                .name("History 101")
                .build();

        Student student = Student.builder()
                .id(1)
                .name("Ram")
                .build();

        /**
         * When calculateFine() is invoked, it internally calls the repository:
         *
         *   Transaction issuedTxn = transactionRepository.findTopByStudentAndBookAndTransactionTypeAndTransactionStatusOrderByIdDesc(...)
         *
         * Since TransactionRepository is mocked, it does not have real behavior unless explicitly defined.
         * By default, mocked methods return null.
         *
         * If we do not stub this method, issuedTxn will be null, and calling:
         *     issuedTxn.getUpdatedOn()
         * will result in a NullPointerException.
         *
         * Therefore, we use Mockito.when(...).thenReturn(...) to define expected behavior.
         */

        Transaction transaction = Transaction.builder()
                .id(1)
                .book(book)
                .student(student)
                .transactionType(TransactionType.ISSUE)
                .transactionStatus(TransactionStatus.SUCCESS)
                .updatedOn(new Date(1774419644000L))
                .build();

        ///Mockito.when(transactionRepository.findTopByStudentAndBookAndTransactionTypeAndTransactionStatusOrderByIdDesc(student, book, TransactionType.ISSUE, TransactionStatus.SUCCESS)).thenReturn(transaction);

        Mockito.when(transactionRepository.findTopByStudentAndBookAndTransactionTypeAndTransactionStatusOrderByIdDesc(
                Mockito.eq(student), Mockito.eq(book), Mockito.eq(TransactionType.ISSUE), Mockito.eq(TransactionStatus.SUCCESS))).thenReturn(transaction);

        int fine = transactionService.calculateFine(student, book);
        Assert.assertEquals(16, fine);
    }

    @Test
    public void testCalculateFine_NoFine(){
        Book book = Book.builder()
                .id(5)
                .name("History 101")
                .build();

        Student student = Student.builder()
                .id(1)
                .name("Ram")
                .build();

        Transaction transaction = Transaction.builder()
                .student(student)
                .book(book)
                .transactionType(TransactionType.ISSUE)
                .transactionStatus(TransactionStatus.SUCCESS)
                .updatedOn(new Date(1792909244000L))
                .build();

        Mockito.when(transactionRepository.findTopByStudentAndBookAndTransactionTypeAndTransactionStatusOrderByIdDesc(
                Mockito.eq(student), Mockito.eq(book), Mockito.eq(TransactionType.ISSUE), Mockito.eq(TransactionStatus.SUCCESS))).thenReturn(transaction);

        int fine = transactionService.calculateFine(student, book);

        Assert.assertEquals(0, fine);

    }
}
