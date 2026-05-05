package com.example.minor_project1.controllers;

import com.example.minor_project1.dtos.CreateStudentRequest;
import com.example.minor_project1.dtos.GetStudentsDetailsResponse;
import com.example.minor_project1.dtos.UpdateStudentRequest;
import com.example.minor_project1.models.Authority;
import com.example.minor_project1.models.Student;
import com.example.minor_project1.models.User;
import com.example.minor_project1.services.StudentService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    StudentController(StudentService studentService){
        this.studentService = studentService;
    }

    /// To add a student to the library
    @PostMapping("/create") /// Similar to /signup
    public Integer createStudent(@Valid @RequestBody CreateStudentRequest createStudentRequest){
        return this.studentService.create(createStudentRequest);
    }

    @GetMapping("/admin/{studentId}") ///  authority of a student.
    public GetStudentsDetailsResponse getStudentDetailsForAdmin(
            @PathVariable("studentId") Integer studentId,
            @RequestParam(value = "requireBookList", required = false,defaultValue = "false") boolean requireBookList
    ) throws Exception {

        /// Fetch the SecurityContext tied to the current request thread
        SecurityContext securityContext = SecurityContextHolder.getContext();

        /// Get Authentication object : Who the user is (principal), Roles (authorities), Auth status
        Authentication authentication = securityContext.getAuthentication();

        /// Get the logged-in user - it returns our custom User object (which implements UserDetails)
        User user = (User) authentication.getPrincipal();

        if(!user.getAuthorities().contains(Authority.ADMIN)){
            throw new Exception("User is not an Admin!");
        }

        return this.studentService.getStudentsDetails(studentId, requireBookList);
    }

    /**
     * We won't pass the studentId in the URL, we need to extract it.
     * */
    @GetMapping("") ///  authority of a student.
    public GetStudentsDetailsResponse getStudentDetails(
            @RequestParam(value = "requireBookList", required = false,defaultValue = "false") boolean requireBookList
    ) throws Exception {
        /// Fetch the SecurityContext tied to the current request thread
        SecurityContext securityContext = SecurityContextHolder.getContext();

        /// Get Authentication object : Who the user is (principal), Roles (authorities), Auth status
        Authentication authentication = securityContext.getAuthentication();

        /// Get the logged-in user - it returns our custom User object (which implements UserDetails)
        User user = (User) authentication.getPrincipal();

        /// From the authenticated user, get the associated Student entity”
        Student student = user.getStudent();

        /// Check if the student is null (i.e. if Admin has requested - student will be null)

        /// Now, if the admin calls this API, getStudent() will be null.

        Integer studentId = null;
        if(student != null){
            studentId = student.getId();
        }else{
            throw new Exception("User is not a Student.");
        }

        return this.studentService.getStudentsDetails(studentId, requireBookList);
    }

    @PatchMapping("")
    public GetStudentsDetailsResponse updateStudentDetails(@Valid
                                                           @RequestBody UpdateStudentRequest updateStudentRequest) throws Exception {
        /// Fetch the SecurityContext tied to the current request thread
        SecurityContext securityContext = SecurityContextHolder.getContext();

        /// Get Authentication object : Who the user is (principal), Roles (authorities), Auth status
        Authentication authentication = securityContext.getAuthentication();

        /// Get the logged-in user - it returns our custom User object (which implements UserDetails)
        User user = (User) authentication.getPrincipal();

        /// From the authenticated user, get the associated Student entity”
        Student student = user.getStudent();

        /// Check if the student is null (i.e. if Admin has requested - student will be null)

        Integer studentId = null;
        if(student != null){
            studentId = student.getId();
        }else{
            throw new Exception("User is not a Student!");
        }

        return this.studentService.update(studentId, updateStudentRequest);
    }

    /// This API would deactivate the student account, not delete its record.
    @DeleteMapping("")
    public GetStudentsDetailsResponse deactivateStudent() throws Exception {
        /// Fetch the SecurityContext tied to the current request thread
        SecurityContext securityContext = SecurityContextHolder.getContext();

        /// Get Authentication object : Who the user is (principal), Roles (authorities), Auth status
        Authentication authentication = securityContext.getAuthentication();

        /// Get the logged-in user - it returns our custom User object (which implements UserDetails)
        User user = (User) authentication.getPrincipal();

        /// From the authenticated user, get the associated Student entity”
        Student student = user.getStudent();

        /// Check if the student is null (i.e. if Admin has requested - student will be null)

        Integer studentId = null;

        if(student != null){
            studentId = student.getId();
        }else{
            throw new Exception("User is not a Student!");
        }


        return studentService.deactivate(studentId);
    }

}
