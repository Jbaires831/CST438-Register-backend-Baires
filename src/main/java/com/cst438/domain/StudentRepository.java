package com.cst438.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
public interface StudentRepository extends CrudRepository <Student, Integer> {

	Student findByEmail(String email);

	List<Student> findAll();
	Student[] findByNameStartsWith(String name);

	@Query("SELECT s FROM Student s WHERE s.student_id = :studentId")
	Student findByStudentId(@Param("studentId") int studentId);
}