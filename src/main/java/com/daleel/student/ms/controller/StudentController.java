package com.daleel.student.ms.controller;

import java.time.Duration;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.daleel.student.ms.entity.StudentDTO;
import com.daleel.student.ms.model.Student;
import com.daleel.student.ms.service.StudentService;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "X-API-KEY")
public class StudentController {
	private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

	@Autowired
	private StudentService studentService;
	private final Bucket listBucket;
	private final Bucket createBucket;
	private final Bucket listPagedBucket;

	public StudentController() {
		Bandwidth limit = Bandwidth.classic(50, Refill.greedy(50, Duration.ofMinutes(1)));
		this.listBucket = Bucket4j.builder().addLimit(limit).build();
		this.createBucket = Bucket4j.builder().addLimit(limit).build();
		this.listPagedBucket = Bucket4j.builder().addLimit(limit).build();
	}

	@Operation(summary = "Retrieve Matching Student filtered by firstname, lastname or department ")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully List students"),
			@ApiResponse(responseCode = "401", description = "Invalid token"),

			@ApiResponse(responseCode = "500", description = "Unexpected system exception"),
			@ApiResponse(responseCode = "429", description = "Reach current API rate limit") })
	@GetMapping("/students")
	public @ResponseBody ResponseEntity<List<Student>> getAllStudents(@RequestParam(required = false) String firstname,
			@RequestParam(required = false) String lastname, @RequestParam(required = false) String departmentName) {
		try {
			if (listBucket.tryConsume(1)) {
				List<Student> students = studentService.getAllStudents(firstname, lastname, departmentName);

				return new ResponseEntity<>(students, HttpStatus.OK);
			}
			return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
		} catch (Exception e) {
			logger.error("Exception in getAllStudents first name:{}, lastname:{}, departmentname:{}", firstname,
					lastname, departmentName, e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@Operation(summary = "Retrieve Matching Student filtered by firstname, lastname or department paged by current page and page size")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully List students"),
			@ApiResponse(responseCode = "401", description = "Invalid token"),

			@ApiResponse(responseCode = "500", description = "Unexpected system exception"),
			@ApiResponse(responseCode = "429", description = "Reach current API rate limit") })

	@GetMapping("/students/{page}/{size}")
	public @ResponseBody ResponseEntity<List<Student>> getAllStudentsPaged(@RequestParam(required = false) String firstname,
			@RequestParam(required = false) String lastname, @RequestParam(required = false) String departmentName,
			@PathVariable("page") int page, @PathVariable("size") int size) {
		try {
			if (listPagedBucket.tryConsume(1)) {
				List<Student> students = studentService.getAllStudents(firstname, lastname, departmentName, page, size);

				return new ResponseEntity<>(students, HttpStatus.OK);
			}
			return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
		} catch (Exception e) {
			logger.error("Exception in getAllStudents first name:{}, lastname:{}, departmentname:{}", firstname,
					lastname, departmentName, e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@Operation(summary = "Retrieve Matching Student by Id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully get student"),
			@ApiResponse(responseCode = "401", description = "Invalid token"),

			@ApiResponse(responseCode = "500", description = "Unexpected system exception"),
			@ApiResponse(responseCode = "429", description = "Reach current API rate limit") })
	@GetMapping("/students/{id}")
	public @ResponseBody ResponseEntity<Student> getStudentById(@PathVariable("id") String id) {

		return new ResponseEntity<>(studentService.getStudentById(id), HttpStatus.OK);

	}
	@Operation(summary = "Create student ")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Successfully created"),
			@ApiResponse(responseCode = "401", description = "Invalid token"),

			@ApiResponse(responseCode = "500", description = "Unexpected system exception"),
			@ApiResponse(responseCode = "429", description = "Reach current API rate limit") })
	
	@PostMapping("/students")

	public @ResponseBody ResponseEntity<Student> createStudent(@RequestBody StudentDTO student) {
		try {
			if (createBucket.tryConsume(1)) {
				return new ResponseEntity<>(studentService.createStudent(
						new Student(student.getFirstname(), student.getLastname(), student.getDepartmentName())),
						HttpStatus.CREATED);
			}
			return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
		} catch (Exception e) {
			logger.error("Exception in createStudent student:{}", student, e);

			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
