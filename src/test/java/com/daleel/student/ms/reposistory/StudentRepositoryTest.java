package com.daleel.student.ms.reposistory;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.daleel.student.ms.model.Student;
import com.daleel.student.ms.repository.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class StudentRepositoryTest {
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private StudentRepository studentRepository;

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
	@DisplayName("Repository - Create Student")
	public void createStudent() {
		// given
		Student testStudent = new Student("Asmaa", "Saad", "IT");

		// when
		Student savedStudent = studentRepository.save(testStudent);

		// then

		then(savedStudent.getId()).as("Check if the Id generated for the saved Student").isNotEmpty();
	}

	@Test
	@DisplayName("Repository - Find Student by Id")
	public void testFindStudentById() {

		// given
		Student testStudent = new Student("id", "Asmaa", "Saad", "IT");
		Student savedStudent = studentRepository.save(testStudent);

		// when
		Optional<Student> fetchStudent = studentRepository.findById(savedStudent.getId());

		// then

		then("id").as("Validate the Id as saved same find.").isEqualTo(fetchStudent.get().getId());
	}

	@Test
	@DisplayName("Repository - Find Students by firstName")
	public void testFindStudentByFirstName() {

		// given
		Student testStudent = new Student("id", "Asmaa", "Saad", "IT");
		studentRepository.save(testStudent);

		// when
		List<Student> fetchStudents = studentRepository.findByFirstname("Asmaa");

		assertEquals(1, fetchStudents.size());
	}

	@Test
	@DisplayName("Repository - Find Students by lastname")
	public void testFindStudentsByLastName() {

		// given
		Student testStudent = new Student("id", "Asmaa", "Saad", "IT");
		studentRepository.save(testStudent);

		// when
		List<Student> fetchStudents = studentRepository.findByLastname("Saad");

		assertEquals(1, fetchStudents.size());
	}

	@Test
	@DisplayName("Repository - Find Students by department")
	public void testFindStudentsByDepartmentName() {

		// given
		Student testStudent = new Student("id", "Asmaa", "Saad", "IT");
		Student testStudent2 = new Student("AsmaSaad", "Asma", "Saaad", "IT");
		studentRepository.save(testStudent);
		studentRepository.save(testStudent2);

		// when
		List<Student> fetchStudents = studentRepository.findByDepartmentName("IT");

		assertEquals(2, fetchStudents.size());
	}

	@Test
	@DisplayName("Repository - Find Students")
	public void testFindStudents() {

		// given
		Student testStudent = new Student("id", "Asmaa", "Saad", "IT");
		Student testStudent2 = new Student("AsmaSaad", "Asma", "Saaad", "IT");
		Student testStudent3 = new Student("AsmaSaaad", "AsmaT", "Saad", "IT");
		studentRepository.save(testStudent);
		studentRepository.save(testStudent2);
		studentRepository.save(testStudent3);

		// when
		List<Student> fetchStudents = studentRepository.findAll();

		assertEquals(4, fetchStudents.size());
	}

	@Test
	@DisplayName("Repository - Delete Student by Id")
	public void testDeleteStudentById() {
		// given
		Student testStudent = new Student("id", "Asmaa", "Saad", "IT");

		studentRepository.save(testStudent);

		studentRepository.delete(testStudent);

		// when
		Optional<Student> fetchStudent = studentRepository.findById("id");

		assertTrue(!fetchStudent.isPresent(), "Student is already deleted");
	}

}
