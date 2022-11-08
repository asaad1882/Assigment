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
import org.springframework.beans.factory.annotation.Value;
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
import com.daleel.student.ms.data.StudentDTO;
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
	@Value("${web.authentication.apikey}")
    private String apiKeyVal;
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
	public void testGetAllStudents() {
		
		String HOST="http://localhost:"+port+"/api/students";
		HttpEntity<StudentDTO> requestEntity = new HttpEntity<>(null, getHeaders());

		ResponseEntity<List<StudentDTO>> responseEntity = this.restTemplate.exchange(
				HOST, HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<List<StudentDTO>>() {
				});
		List<StudentDTO> students = responseEntity.getBody();

		assertTrue(responseEntity.getStatusCode().is2xxSuccessful(), "HTTP Response status code should be 200");
		assertTrue(students.size() == 13, "There should be exactly 1 student in the list");
	}

	@Test
	public void testGetStudentByFirstName() {
		String HOST="http://localhost:"+port+"/api/students";
		HttpEntity<StudentDTO> requestEntity = new HttpEntity<>(null, getHeaders());

		ResponseEntity<List<StudentDTO>> responseEntity = this.restTemplate.exchange(
				HOST+"?firstname=test", HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<List<StudentDTO>>() {
				});
		List<StudentDTO> students = responseEntity.getBody();
		System.out.println("responseEntity.getStatusCode()"+responseEntity.getStatusCode());

		assertTrue(responseEntity.getStatusCode().is2xxSuccessful(), "HTTP Response status code should be 200");

		assertTrue(students.get(0).getFirstname().equalsIgnoreCase("Test"),
				"There should be exactly 1 student in the list");
	}

	@Test
	public void testGetStudentByFirstName_UnKnownFirstName() {
		String HOST="http://localhost:"+port+"/api/students";
		System.out.println("Host:::::"+HOST);
		HttpEntity<StudentDTO> requestEntity = new HttpEntity<>(null, getHeaders());

		ResponseEntity<List<StudentDTO>> responseEntity = this.restTemplate.exchange(
				HOST+"?firstname=Asmaa", HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<List<StudentDTO>>() {
				});
		List<StudentDTO> students = responseEntity.getBody();

		assertTrue(responseEntity.getStatusCode().is2xxSuccessful(), "HTTP Response status code should be 200");
		assertTrue(students.size() == 0, "There should be exactly 0 Student in the list");

	}

	@Test
	public void testGetStudentByLastName() {
		
		String HOST="http://localhost:"+port+"/api/students";
		HttpEntity<StudentDTO> requestEntity = new HttpEntity<>(null, getHeaders());

		ResponseEntity<List<StudentDTO>> responseEntity = this.restTemplate.exchange(
				HOST+"?lastname=test", HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<List<StudentDTO>>() {
				});
		List<StudentDTO> students = responseEntity.getBody();

		assertTrue(responseEntity.getStatusCode().is2xxSuccessful(), "HTTP Response status code should be 200");
		
		assertTrue(students.get(0).getFirstname().equalsIgnoreCase("Test"),
				"There should be exactly 1 student in the list");
	}

	@Test
	public void testGetStudentByLastName_UnKnownLastName() {
		String HOST="http://localhost:"+port+"/api/students";

		HttpEntity<StudentDTO> requestEntity = new HttpEntity<>(null, getHeaders());

		ResponseEntity<List<StudentDTO>> responseEntity = this.restTemplate.exchange(
				HOST+"?lastname=Saad", HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<List<StudentDTO>>() {
				});
		List<StudentDTO> students = responseEntity.getBody();
		System.out.println("Students"+students);
		assertTrue(responseEntity.getStatusCode().is2xxSuccessful(), "HTTP Response status code should be 200");
		assertTrue(students.size() == 0, "There should be exactly 0 Student in the list");

	}
	@Test
	public void testGetAllStudentsPaged() {
		
		String HOST="http://localhost:"+port+"/api/students";
		HttpEntity<StudentDTO> requestEntity = new HttpEntity<>(null, getHeaders());

		ResponseEntity<List<StudentDTO>> responseEntity = this.restTemplate.exchange(
				HOST+"/0/4", HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<List<StudentDTO>>() {
				});
		List<StudentDTO> students = responseEntity.getBody();

		assertTrue(responseEntity.getStatusCode().is2xxSuccessful(), "HTTP Response status code should be 200");
		assertTrue(students.size() == 4, "There should be exactly 1 student in the list");
	}

	@Test
	public void testGetStudentByFirstNamePaged() {
		
		String HOST="http://localhost:"+port+"/api/students";
		HttpEntity<StudentDTO> requestEntity = new HttpEntity<>(null, getHeaders());

		ResponseEntity<List<StudentDTO>> responseEntity = this.restTemplate.exchange(
				HOST+"/0/1?firstname=test", HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<List<StudentDTO>>() {
				});
		List<StudentDTO> students = responseEntity.getBody();

		assertTrue(responseEntity.getStatusCode().is2xxSuccessful(), "HTTP Response status code should be 200");
		assertTrue(students.size() == 1, "There should be exactly 1 user in the list");
		assertTrue(students.get(0).getFirstname().equalsIgnoreCase("Test"),
				"There should be exactly 1 student in the list");
	}

	@Test
	public void testGetStudentByFirstNamePaged_UnKnownFirstName() {
		String HOST="http://localhost:"+port+"/api/students";
		HttpEntity<StudentDTO> requestEntity = new HttpEntity<>(null, getHeaders());

		ResponseEntity<List<StudentDTO>> responseEntity = this.restTemplate.exchange(
				HOST+"/0/4?firstname=Asmaa", HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<List<StudentDTO>>() {
				});
		List<StudentDTO> students = responseEntity.getBody();

		assertTrue(responseEntity.getStatusCode().is2xxSuccessful(), "HTTP Response status code should be 200");
		assertTrue(students.size() == 0, "There should be exactly 0 Student in the list");

	}

	@Test
	public void testGetStudentByLastNamePaged() {
		String HOST="http://localhost:"+port+"/api/students";
		
		HttpEntity<StudentDTO> requestEntity = new HttpEntity<>(null, getHeaders());

		ResponseEntity<List<StudentDTO>> responseEntity = this.restTemplate.exchange(
				HOST+"/0/1?lastname=test", HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<List<StudentDTO>>() {
				});
		List<StudentDTO> students = responseEntity.getBody();

		assertTrue(responseEntity.getStatusCode().is2xxSuccessful(), "HTTP Response status code should be 200");
		assertTrue(students.size() == 1, "There should be exactly 1 student in the list");
		assertTrue(students.get(0).getFirstname().equalsIgnoreCase("Test"),
				"There should be exactly 1 student in the list");
	}

	@Test
	public void testGetStudentByLastNamePaged_UnKnownLastName() {
		String HOST="http://localhost:"+port+"/api/students";

		HttpEntity<StudentDTO> requestEntity = new HttpEntity<>(null, getHeaders());

		ResponseEntity<List<StudentDTO>> responseEntity = this.restTemplate.exchange(
				HOST+"/0/0?lastname=Saad", HttpMethod.GET, requestEntity,
				new ParameterizedTypeReference<List<StudentDTO>>() {
				});
		List<StudentDTO> students = responseEntity.getBody();

		assertTrue(responseEntity.getStatusCode().is2xxSuccessful(), "HTTP Response status code should be 200");
		assertTrue(students.size() == 0, "There should be exactly 0 Student in the list");

	}


	@Test
	public void testAddStudent() {
		String HOST="http://localhost:"+port+"/api/students";
		StudentDTO testStudent = new StudentDTO("xxxx", "Eden", "McBride", "IT");
		

		HttpEntity<StudentDTO> requestEntity = new HttpEntity<>(testStudent, getHeaders());
		ResponseEntity<StudentDTO> responseEntity = this.restTemplate
				.exchange(HOST,HttpMethod.POST, requestEntity, StudentDTO.class);
		assertEquals(201, responseEntity.getStatusCodeValue());
	}
	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("accept", "application/json");
		
		headers.set("X-API-KEY", apiKeyVal);
		return headers;
	}

}
