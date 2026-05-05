package com.example.minor_project1.controllers;

import com.example.minor_project1.models.Student;
import com.example.minor_project1.models.TransactionType;
import com.example.minor_project1.models.User;
import com.example.minor_project1.services.TransactionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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

    /**
     * We need to get rid of @RequestParam("studentId") because any student can use this with someone else's id and return/issue a book.
     * */
    @PostMapping("/initiate")
    public String initiateTxn(@RequestParam("bookId") Integer bookId,
                              @RequestParam("transactionType")TransactionType transactionType) throws Exception {

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        User user = (User) authentication.getPrincipal();
        Student student = user.getStudent();

        Integer studentId = null;

        if(student != null){
            studentId = student.getId();
        }else{
            throw new Exception("Not a valid student!");
        }

        return this.transactionService.initiateTransaction(studentId, bookId, transactionType);

    }
}
