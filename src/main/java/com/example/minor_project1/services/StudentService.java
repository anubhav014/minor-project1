package com.example.minor_project1.services;

import com.example.minor_project1.dtos.CreateStudentRequest;
import com.example.minor_project1.dtos.GetStudentsDetailsResponse;
import com.example.minor_project1.dtos.UpdateStudentRequest;
import com.example.minor_project1.models.Book;
import com.example.minor_project1.models.Student;
import com.example.minor_project1.models.StudentStatus;
import com.example.minor_project1.repositories.StudentRepository;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.util.JSONPObject;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class StudentService {

    ObjectMapper mapper = new ObjectMapper();

    StudentRepository studentRepository;
    /**
     * To get the unidirectional relationship working, we need the StudentService talk to the BookService and gather the details of Book.
     * Also note that, the StudentService must not talk to the BookRepository directly bypassing the BookService.
     * This could also avoid cycles - if we bypass the service layer and directly talk to the repo layer, it might be the case
     * that the repo layer depends on some other layer and form a cycle. Hence, it's always better to follow the hierarchy.
     * Because the BookService provides us with the abstraction that it might be performing some operation on the data it get from the BookRepository.
     * */
    BookService bookService;

    StudentService(StudentRepository studentRepository, BookService bookService){
        this.studentRepository = studentRepository;
        this.bookService = bookService;
    }

    public Integer create(CreateStudentRequest createStudentRequest){
        Student student = createStudentRequest.mapToStudent();
        Student newStudent =  this.studentRepository.save(student);
        return newStudent.getId();
    }

    public GetStudentsDetailsResponse getStudentsDetails(Integer studentId, boolean requireBookList){

        //List<Book> bookList = null;
        /**
         * Just a check if the user wants the book data as well.
         * Point to note - If this was a bidirectional relationship, we would have got everything from the StudentService only,
         * and we could not have applied this logic to bifurcate the data easily.
         * */
//        if(requireBookList){
//            bookList = this.bookService.getBooksByStudentId(studentId);
//        }
        Student student = this.studentRepository.findById(studentId).orElse(null);
        //List<Book> bookList = this.bookService.getBooksByStudentId(studentId);

        return GetStudentsDetailsResponse.builder()
                .student(student)
                .bookList(student.getBookList())
                .build();
    }

     public GetStudentsDetailsResponse update(Integer studentId, UpdateStudentRequest updateStudentRequest){

        Student student = updateStudentRequest.mapToStudent(); //Incoming Student which is the updated details.
        GetStudentsDetailsResponse studentsDetailsResponse = this.getStudentsDetails(studentId, false);

        Student savedStudent = studentsDetailsResponse.getStudent(); //already existing record for a Student.
        /// Now, we've got an incoming Student "Student" and a saved Student "savedStudent"- Merge these 2

         /// Another way to retrieve the Student (We do the same thing in getStudentsDetails on line 55) ------------> Student student = this.studentRepository.findById(studentId).orElse(null);

         Student mergedStudent = this.deepMerge(student, savedStudent);
         this.studentRepository.save(mergedStudent); /// if we do not update the database, the merge won't be reflecting there. It would just give us the correct response via API but won't update the DB with the updated records.
         return GetStudentsDetailsResponse.builder()
                 .student(mergedStudent)
                 .build();
     }

     /// Method that will merge the incoming requests to existing records of Student
    private Student deepMerge(Student incoming, Student saved){
        /**
         * Since we now have java Objects : incoming and saved ---> We can't iterate over a Java object as they do not implement Iterable interface by default.
         * */
        JSONObject incomingStudent = mapper.convertValue(incoming, JSONObject.class);
        JSONObject savedStudent = mapper.convertValue(saved, JSONObject.class);

        //Map incomingStudent = mapper.convertValue(incoming, Map.class);
        //Map savedStudent = mapper.convertValue(saved, Map.class);

        Iterator it = incomingStudent.keySet().iterator();
        while(it.hasNext()){
            String key = (String) it.next();
            if(incomingStudent.get(key) != null){
                savedStudent.put(key, incomingStudent.get(key));
            }
        }
        return mapper.convertValue(savedStudent, Student.class);

    }

    /**
     * We can again make use of merge - i.e. take tha boolean and merge with the current record.
     * However, this will simply increase the latency, and there are better ways to get this done.
     * Since we simply need to update one column, we will use a SQL query here.
     * */
    public GetStudentsDetailsResponse deactivate(int studentId){
        this.studentRepository.deactivate(studentId, StudentStatus.INACTIVE);
        return this.getStudentsDetails(studentId, false);
    }

}
