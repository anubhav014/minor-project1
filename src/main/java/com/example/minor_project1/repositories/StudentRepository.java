package com.example.minor_project1.repositories;

import com.example.minor_project1.models.Student;
import com.example.minor_project1.models.StudentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    @Transactional
    @Modifying
    @Query("UPDATE Student S SET S.status = :status WHERE S.id = :studentId")
    void deactivate(int studentId, StudentStatus status);
}
