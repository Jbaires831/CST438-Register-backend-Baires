package com.cst438.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.ScheduleDTO;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;
import com.cst438.service.GradebookService;
@RestController
public class StudentController {
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    EnrollmentRepository enrollmentRepository;

    @GetMapping("/student/list")
    public Iterable<Student> listStudents(){
        return studentRepository.findAll();
    }

    @PostMapping("/student/add")
    public Student addStudent(@RequestBody Student s){

        Student student = studentRepository.findByEmail(s.getEmail());

        if (student == null){
            student = new Student();
            student.setName(s.getName());
            student.setEmail(s.getEmail());
            student.setStatus(s.getStatus());
            student.setStatusCode(s.getStatusCode());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "A student with that email a already exists.");
        }
        return studentRepository.findByEmail(s.getEmail());
    }

    @PutMapping("/student/update")
    public Student update(@RequestBody Student us){
        Student student = studentRepository.findById(us.getStudent_id()).orElse(null);
        if(student != null){
            student.setStudent_id(us.getStudent_id());
            student.setName(us.getName());
            student.setEmail(us.getEmail());
            student.setStatus(us.getStatus());
            student.setStatusCode(us.getStatusCode());
        }
        return null;
    }

    @DeleteMapping("student/delete")
    public void delete(
            @RequestParam("id") Integer id,
            @RequestParam("FORCE") Optional<Boolean> FORCE){
        Enrollment[] studentInEnrollment = enrollmentRepository.findStudentInEnrollment(id);
        Student student = studentRepository.findById(id).orElse(null);

        if(student != null){
            if(studentInEnrollment.length == 0){
                studentRepository.deleteById(id);
            } else {
                if(FORCE.orElse(false)){
                    studentRepository.deleteById(id);
                } else{
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
                }
            }
        }
    }
}
