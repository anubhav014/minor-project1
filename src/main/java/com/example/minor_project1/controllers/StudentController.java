package com.example.minor_project1.controllers;

import com.example.minor_project1.dtos.CreateStudentRequest;
import com.example.minor_project1.dtos.GetStudentsDetailsResponse;
import com.example.minor_project1.dtos.UpdateStudentRequest;
import com.example.minor_project1.models.Student;
import com.example.minor_project1.services.StudentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    StudentController(StudentService studentService){
        this.studentService = studentService;
    }

    /// To add a student to the library
    @PostMapping("/create")
    public Integer createStudent(@Valid @RequestBody CreateStudentRequest createStudentRequest){
        return this.studentService.create(createStudentRequest);
    }

    @GetMapping("/{studentId}")
    public GetStudentsDetailsResponse getStudentDetails(
            @PathVariable("studentId") int studentId,
            @RequestParam(value = "requireBookList", required = false,defaultValue = "false") boolean requireBookList
    ){
        return this.studentService.getStudentsDetails(studentId, requireBookList);
    }

    @PatchMapping("/{studentId}")
    public GetStudentsDetailsResponse updateStudentDetails(@Valid
                                                           @RequestBody UpdateStudentRequest updateStudentRequest,
                                                           @PathVariable("studentId") Integer studentId){
        return this.studentService.update(studentId, updateStudentRequest);
    }

    /// This API would deactive the student account, not delete its record.
    @DeleteMapping("/{studentId}")
    public GetStudentsDetailsResponse deactivateStudent(
            @PathVariable("studentId") int studentId
    ){
        return studentService.deactivate(studentId);
    }

}
