package com.example.minor_project1.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //This is like a uuid -----> instead of returning an id, we will return an uuid that would provide us with a cushion of security.
    private String externalTransactionId;

    /**
     * From the Logs:
     * Hibernate: alter table transaction add constraint FK8hddvclv2iqa3sg1dm8295pqw foreign key (book_id) references book (id)
     * Hibernate: alter table transaction add constraint FKd71rhpdlg8cjw7byfd16lg304 foreign key (student_id) references student (id)
     * */
    @ManyToOne
    @JoinColumn /// <---------------- This is a FK
    @JsonIgnoreProperties("transactionList")
    private Book book;

    @ManyToOne
    @JoinColumn /// <---------------- This is a FK
    //This will be a FK in Student table
    @JsonIgnoreProperties("transactions")
    private Student student;

    @Enumerated(value= EnumType.STRING)
    private TransactionStatus transactionStatus;

    @Enumerated(value= EnumType.STRING)
    private TransactionType transactionType;

    private Integer fine;

    @CreationTimestamp
    private Date createdOn;

    @UpdateTimestamp
    private Date updatedOn;


}
