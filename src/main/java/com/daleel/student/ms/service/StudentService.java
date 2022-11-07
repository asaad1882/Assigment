package com.daleel.student.ms.service;

import java.util.List;

import com.daleel.student.ms.model.Student;

public interface StudentService {
	Student createStudent(Student student);
	
	
	List<Student> getAllStudents();
	List<Student> getAllStudents(String firstname,String lastname,String departmentName);
	Student getStudentById(String studentId);
	

}
