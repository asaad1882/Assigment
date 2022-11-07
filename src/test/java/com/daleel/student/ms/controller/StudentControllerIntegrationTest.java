package com.daleel.student.ms.controller;

import static org.junit.Assert.assertEquals;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.daleel.student.ms.App;
import com.daleel.student.ms.model.Student;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(classes = App.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class StudentControllerIntegrationTest {
	@LocalServerPort
	private int port;
	@Autowired
	private TestRestTemplate restTemplate;
	@Autowired
	private MongoTemplate mongoTemplate;
	private ObjectMapper mapper = new ObjectMapper();

	@BeforeEach
	void setup() throws IOException {
		Path resourceDirectory = Paths.get("src", "test", "resources");
		Path tstRes = resourceDirectory.resolve("studentCreate.json");
		String absolutePath = tstRes.toFile().getAbsolutePath();
		File file = new File(absolutePath);
		Student[] events = mapper.readValue(file, Student[].class);
		Arrays.stream(events).forEach(mongoTemplate::save);

	}

	@AfterEach
	void destroy() {
		mongoTemplate.getDb().drop();
	}

	@Test
	public void testAllStudents() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("accept", "application/json");
		headers.set("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D");

		HttpEntity<Student> requestEntity = new HttpEntity<>(null, headers);

		ResponseEntity<List<Student>> responseEntity = this.restTemplate.exchange(
				"http://localhost:" + port + "/api/students", HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<List<Student>>() {
				});
		List<Student> students = responseEntity.getBody();

		assertTrue(responseEntity.getStatusCode().is2xxSuccessful(), "HTTP Response status code should be 200");
		assertTrue(students.size() == 13, "There should be exactly 1 student in the list");
	}

	@Test
	public void whenValidFirstName_thenStudentShouldBeFound() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("accept", "application/json");
		headers.set("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D");

		HttpEntity<Student> requestEntity = new HttpEntity<>(null, headers);

		ResponseEntity<List<Student>> responseEntity = this.restTemplate.exchange(
				"http://localhost:" + port + "/api/students?firstname=test", HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<List<Student>>() {
				});
		List<Student> students = responseEntity.getBody();

		assertTrue(responseEntity.getStatusCode().is2xxSuccessful(), "HTTP Response status code should be 200");

		assertTrue(students.get(0).getFirstname().equalsIgnoreCase("Test"),
				"There should be exactly 1 student in the list");
	}

	@Test
	public void whenInValidFirstName_thenStudentShouldNotBeFound() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("accept", "application/json");
		headers.set("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D");

		HttpEntity<Student> requestEntity = new HttpEntity<>(null, headers);

		ResponseEntity<List<Student>> responseEntity = this.restTemplate.exchange(
				"http://localhost:" + port + "/api/students?firstname=Asmaa", HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<List<Student>>() {
				});
		List<Student> students = responseEntity.getBody();

		assertTrue(responseEntity.getStatusCode().is2xxSuccessful(), "HTTP Response status code should be 200");
		assertTrue(students.size() == 0, "There should be exactly 0 Student in the list");

	}

	@Test
	public void whenValidLastName_thenStudentShouldBeFound() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("accept", "application/json");
		headers.set("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D");

		HttpEntity<Student> requestEntity = new HttpEntity<>(null, headers);

		ResponseEntity<List<Student>> responseEntity = this.restTemplate.exchange(
				"http://localhost:" + port + "/api/students?lastname=test", HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<List<Student>>() {
				});
		List<Student> students = responseEntity.getBody();

		assertTrue(responseEntity.getStatusCode().is2xxSuccessful(), "HTTP Response status code should be 200");
		
		assertTrue(students.get(0).getFirstname().equalsIgnoreCase("Test"),
				"There should be exactly 1 student in the list");
	}

	@Test
	public void whenInValidLastName_thenStudentShouldNotBeFound() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("accept", "application/json");
		headers.set("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D");

		HttpEntity<Student> requestEntity = new HttpEntity<>(null, headers);

		ResponseEntity<List<Student>> responseEntity = this.restTemplate.exchange(
				"http://localhost:" + port + "/api/students?lastname=Saad", HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<List<Student>>() {
				});
		List<Student> students = responseEntity.getBody();

		assertTrue(responseEntity.getStatusCode().is2xxSuccessful(), "HTTP Response status code should be 200");
		assertTrue(students.size() == 0, "There should be exactly 0 Student in the list");

	}
	@Test
	public void testAllStudentsPaged() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("accept", "application/json");
		headers.set("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D");

		HttpEntity<Student> requestEntity = new HttpEntity<>(null, headers);

		ResponseEntity<List<Student>> responseEntity = this.restTemplate.exchange(
				"http://localhost:" + port + "/api/students/0/4", HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<List<Student>>() {
				});
		List<Student> students = responseEntity.getBody();

		assertTrue(responseEntity.getStatusCode().is2xxSuccessful(), "HTTP Response status code should be 200");
		assertTrue(students.size() == 4, "There should be exactly 1 student in the list");
	}

	@Test
	public void whenValidFirstNamePaged_thenStudentShouldBeFound() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("accept", "application/json");
		headers.set("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D");

		HttpEntity<Student> requestEntity = new HttpEntity<>(null, headers);

		ResponseEntity<List<Student>> responseEntity = this.restTemplate.exchange(
				"http://localhost:" + port + "/api/students/0/1?firstname=test", HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<List<Student>>() {
				});
		List<Student> students = responseEntity.getBody();

		assertTrue(responseEntity.getStatusCode().is2xxSuccessful(), "HTTP Response status code should be 200");
		assertTrue(students.size() == 1, "There should be exactly 1 user in the list");
		assertTrue(students.get(0).getFirstname().equalsIgnoreCase("Test"),
				"There should be exactly 1 student in the list");
	}

	@Test
	public void whenInValidFirstNamePaged_thenStudentShouldNotBeFound() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("accept", "application/json");
		headers.set("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D");

		HttpEntity<Student> requestEntity = new HttpEntity<>(null, headers);

		ResponseEntity<List<Student>> responseEntity = this.restTemplate.exchange(
				"http://localhost:" + port + "/api/students/0/4?firstname=Asmaa", HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<List<Student>>() {
				});
		List<Student> students = responseEntity.getBody();

		assertTrue(responseEntity.getStatusCode().is2xxSuccessful(), "HTTP Response status code should be 200");
		assertTrue(students.size() == 0, "There should be exactly 0 Student in the list");

	}

	@Test
	public void whenValidLastNamePaged_thenStudentShouldBeFound() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("accept", "application/json");
		headers.set("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D");

		HttpEntity<Student> requestEntity = new HttpEntity<>(null, headers);

		ResponseEntity<List<Student>> responseEntity = this.restTemplate.exchange(
				"http://localhost:" + port + "/api/students/0/1?lastname=test", HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<List<Student>>() {
				});
		List<Student> students = responseEntity.getBody();

		assertTrue(responseEntity.getStatusCode().is2xxSuccessful(), "HTTP Response status code should be 200");
		assertTrue(students.size() == 1, "There should be exactly 1 student in the list");
		assertTrue(students.get(0).getFirstname().equalsIgnoreCase("Test"),
				"There should be exactly 1 student in the list");
	}

	@Test
	public void whenInValidLastNamePaged_thenStudentShouldNotBeFound() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("accept", "application/json");
		headers.set("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D");

		HttpEntity<Student> requestEntity = new HttpEntity<>(null, headers);

		ResponseEntity<List<Student>> responseEntity = this.restTemplate.exchange(
				"http://localhost:" + port + "/api/students/0/0?lastname=Saad", HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<List<Student>>() {
				});
		List<Student> students = responseEntity.getBody();

		assertTrue(responseEntity.getStatusCode().is2xxSuccessful(), "HTTP Response status code should be 200");
		assertTrue(students.size() == 0, "There should be exactly 0 Student in the list");

	}


	@Test
	public void testAddStudent() {
		Student testStudent = new Student("xxxx", "Eden", "McBride", "IT");
		HttpHeaders headers = new HttpHeaders();
		headers.set("accept", "application/json");
		headers.set("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D");

		HttpEntity<Student> requestEntity = new HttpEntity<>(testStudent, headers);
		ResponseEntity<Student> responseEntity = this.restTemplate
				.exchange("http://localhost:" + port + "/api/students",HttpMethod.POST, requestEntity, Student.class);
		assertEquals(201, responseEntity.getStatusCodeValue());
	}

}
