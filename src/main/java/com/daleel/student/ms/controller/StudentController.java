package com.daleel.student.ms.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.daleel.student.ms.entity.StudentDTO;
import com.daleel.student.ms.model.Student;
import com.daleel.student.ms.service.StudentService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;




@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "X-API-KEY")
public class StudentController {
	private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
	   
	 @Autowired
	  private StudentService studentService;
	 @GetMapping("/students")
	  public ResponseEntity<List<Student>> getAllStudents(@RequestParam(required = false) String firstname, @RequestParam(required = false) String lastname,@RequestParam(required = false) String departmentName) {
	    try {
	    	List<Student> students=studentService.getAllStudents(firstname, lastname, departmentName);
	    	
	     

	      return new ResponseEntity<>(students, HttpStatus.OK);
	    } catch (Exception e) {
	    	logger.error("Exception in getAllStudents first name:{}, lastname:{}, departmentname:{}",firstname,lastname,departmentName,e);
	      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }

	  @GetMapping("/students/{id}")
	  public ResponseEntity<Student> getStudentById(@PathVariable("id") String id) {
	      return new ResponseEntity<>(studentService.getStudentById(id), HttpStatus.OK);
	    
	  }

	  @PostMapping("/students")
	  
	  public ResponseEntity<Student> createStudent(@RequestBody StudentDTO student) {
	    try {
	      return new ResponseEntity<>(studentService.createStudent(new Student(student.getFirstname(), student.getLastname(),student.getDepartmentName())), HttpStatus.CREATED);
	    } catch (Exception e) {
	    	logger.error("Exception in createStudent student:{}",student,e);
	    	
	      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }
	

}
