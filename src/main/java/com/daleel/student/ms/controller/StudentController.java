package com.daleel.student.ms.controller;

import java.time.Duration;
import java.util.List;

import javax.validation.Valid;


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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.daleel.student.ms.data.StudentDTO;

import com.daleel.student.ms.service.StudentService;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;

import io.github.bucket4j.Refill;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "X-API-KEY")
@Slf4j

public class StudentController {
	
	private final StudentService studentService;
	private final Bucket listBucket;
	private final Bucket createBucket;
	private final Bucket listPagedBucket;
	@Autowired
	public StudentController(StudentService studentService) {
		this.studentService=studentService;
		Bandwidth limit = Bandwidth.classic(50, Refill.greedy(50, Duration.ofMinutes(1)));
		this.listBucket = Bucket.builder().addLimit(limit).build();
		this.createBucket = Bucket.builder().addLimit(limit).build();
		this.listPagedBucket = Bucket.builder().addLimit(limit).build();
		
	}

	@Operation(summary = "Retrieve Matching Student filtered by firstname, lastname or department ")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully List students"),
			@ApiResponse(responseCode = "401", description = "Invalid token"),

			@ApiResponse(responseCode = "500", description = "Unexpected system exception"),
			@ApiResponse(responseCode = "429", description = "Reach current API rate limit") })
	@GetMapping(value="/students",produces="application/json")
	public @ResponseBody ResponseEntity<List<StudentDTO>> getAllStudents(@RequestParam(required = false) String firstname,
			@RequestParam(required = false) String lastname, @RequestParam(required = false) String departmentName) {
		log.info("Request  {}{}{}",firstname,lastname,departmentName);
		try {
			if (listBucket.tryConsume(1)) {
				List<StudentDTO> students = studentService.getAllStudents(firstname, lastname, departmentName);
				log.info("Request  {}{}{} response {}",firstname,lastname,departmentName,students);
				return new ResponseEntity<>(students, HttpStatus.OK);
			}
			log.info("Request  {}{}{} too Many Requests",firstname,lastname,departmentName);
			return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
		} catch (Exception e) {
			log.error("Exception in getAllStudents first name:{}, lastname:{}, departmentname:{}", firstname,
					lastname, departmentName, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	@Operation(summary = "Retrieve Matching Student filtered by firstname, lastname or department paged by current page and page size")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully List students"),
			@ApiResponse(responseCode = "401", description = "Invalid token"),

			@ApiResponse(responseCode = "500", description = "Unexpected system exception"),
			@ApiResponse(responseCode = "429", description = "Reach current API rate limit") })

	@GetMapping("/students/{page}/{size}")
	public @ResponseBody ResponseEntity<List<StudentDTO>> getAllStudentsPaged(@RequestParam(required = false) String firstname,
			@RequestParam(required = false) String lastname, @RequestParam(required = false) String departmentName,
			@PathVariable("page") int page, @PathVariable("size") int size) {
		try {
			if (listPagedBucket.tryConsume(1)) {
				List<StudentDTO> students = studentService.getAllStudents(firstname, lastname, departmentName, page, size);

				return new ResponseEntity<>(students, HttpStatus.OK);
			}
			return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
		} catch (Exception e) {
			log.error("Exception in getAllStudents first name:{}, lastname:{}, departmentname:{}", firstname,
					lastname, departmentName, e);
			return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	@Operation(summary = "Retrieve Matching Student by Id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully get student"),
			@ApiResponse(responseCode = "401", description = "Invalid token"),

			@ApiResponse(responseCode = "500", description = "Unexpected system exception"),
			@ApiResponse(responseCode = "429", description = "Reach current API rate limit") })
	@GetMapping("/students/{id}")
	public @ResponseBody ResponseEntity<StudentDTO> getStudentById(@PathVariable("id") String id) {
		StudentDTO studentDTO=studentService.getStudentById(id);
		if(studentDTO!=null) {

		return new ResponseEntity<>(studentService.getStudentById(id), HttpStatus.OK);
		}else {
			
			      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	
		}

	}
	@Operation(summary = "Create student ")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Successfully created"),
			@ApiResponse(responseCode = "401", description = "Invalid token"),
			@ApiResponse(responseCode = "400", description = "Bad Request"),
			@ApiResponse(responseCode = "500", description = "Unexpected system exception"),
			@ApiResponse(responseCode = "429", description = "Reach current API rate limit") })
	
	@PostMapping(value="/students",consumes="application/json",produces="application/json")
	

	public @ResponseBody ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody  StudentDTO student) {
		try {
			if (createBucket.tryConsume(1)) {
				return new ResponseEntity<>(studentService.createStudent(
						student),
						HttpStatus.CREATED);
			}
			return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
		} catch (Exception e) {
			log.error("Exception in createStudent student:{}", student, e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	@GetMapping("/bucketsReset")

	public  void resetBucket() {
		listBucket.reset();
		createBucket.reset();
		listPagedBucket.reset();
	}

}
