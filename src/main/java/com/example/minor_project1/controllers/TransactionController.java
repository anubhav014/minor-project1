package com.example.minor_project1.controllers;

import com.example.minor_project1.models.TransactionType;
import com.example.minor_project1.services.TransactionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/transactions")
@RestController
public class TransactionController {

    /**
     * Approach 1:
            - Separate APIs for issuance and return
            - Issuance: StudentId + bookId
            - Return: studentId + bookId

     Approach 2:
            - Single API i.e. initiate transaction
            - StudentId + bookId + type of Transaction

     ---> We are going ahead with the second approach
     */

    TransactionService transactionService;

    TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    @PostMapping("/initiate")
    public String initiateTxn(@RequestParam("studentId") Integer studentId,
                              @RequestParam("bookId") Integer bookId,
                              @RequestParam("transactionType")TransactionType transactionType) throws Exception {

        return this.transactionService.initiateTransaction(studentId, bookId, transactionType);

    }
}
