package com.example.minor_project1.repositories;

import com.example.minor_project1.models.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    /**
     * Say
     *  S1 - B1 (Issue) -> T1
     *  S1 - B1 (Return) -> T2
     *  S1 - B1 (Issue) -> T3
     *
     * So Student S1 can have multiple transactions on the same book, therefore we need to consider the last one issued, not any.
     * Therefore, we do Desc By Id and then selecting TOP 1.
     * */
    Transaction findTopByStudentAndBookAndTransactionTypeAndTransactionStatusOrderByIdDesc(
            Student student, Book book, TransactionType transactionType ,TransactionStatus transactionStatus
    );
}
