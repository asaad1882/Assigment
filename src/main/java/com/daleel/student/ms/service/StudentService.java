package com.daleel.student.ms.service;

import java.util.List;

import com.daleel.student.ms.data.StudentDTO;


public interface StudentService {
	StudentDTO createStudent(StudentDTO student);
	
	
	List<StudentDTO> getAllStudents();
	List<StudentDTO> getAllStudents(String firstname,String lastname,String departmentName);
	StudentDTO getStudentById(String studentId);
	List<StudentDTO> getAllStudents(String firstname, String lastname, String departmentName,int page,int size);
	

}
