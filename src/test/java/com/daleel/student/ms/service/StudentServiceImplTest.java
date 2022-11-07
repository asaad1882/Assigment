package com.daleel.student.ms.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.assertj.core.api.SoftAssertions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.daleel.student.ms.model.Student;
import com.daleel.student.ms.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")

public class StudentServiceImplTest {
	@Autowired
	private StudentService studentService;

	@MockBean
	private StudentRepository studentRepository;

	@Autowired
	MongoTemplate mongoTemplate;

	@Test
	@DisplayName("Create Student Service Method Success")
	public void testCreateStudentSuccess() {
		// given

		Student testStudent = new Student("id", "Asmaa", "Saad", "IT");
		given(studentRepository.save(any())).willReturn(testStudent);

		// when
		Student savedStudent = studentService.createStudent(testStudent);

		// then

		then(savedStudent.getFirstname()).as("Check the firstname for the input student with the saved student")
				.isEqualTo(testStudent.getFirstname());

		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(savedStudent.getFirstname())
					.as("Check the firstname for the input student with the saved student")
					.isEqualTo(testStudent.getFirstname());
			softly.assertThat(savedStudent.getLastname())
					.as("Check the lastname for the input student with the saved student")
					.isEqualTo(testStudent.getLastname());
			softly.assertThat(savedStudent.getId()).as("Check the Id exists").isNotEmpty();
		});

	}

	@Test
	@DisplayName("Find student by Id - Success")
	public void testFindStudentByIdSuccess() {
		// given
		Student testStudent = new Student("id", "Asmaa", "Saad", "IT");
		given(studentRepository.findById(any())).willReturn(Optional.of(testStudent));

		// when
		Student findStudent = studentService.getStudentById("id");

		// then

		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(testStudent.getFirstname()).as("Check the firstname for match")
					.isEqualTo(findStudent.getFirstname());
			softly.assertThat(testStudent.getLastname()).as("Check the lastname for match")
					.isEqualTo(findStudent.getLastname());
		});
	}
	@Test
	@DisplayName("Find student by Id - Success")
	public void testFindStudentByIdFail() {
		// given
		
		given(studentRepository.findById("xxx")).willReturn(Optional.empty());

		// when
		Student findStudent = studentService.getStudentById("xxx");

		// then

		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(findStudent==null);
			
		});
	}
	@Test
	@DisplayName("Test - getStudentsByFirstName success")
	public void testStudentsByFirstName() {
		// given
		Student testStudent = new Student("id", "Asmaa", "Saad", "IT");
		List<Student> testStudents=new ArrayList<Student>();
		testStudents.add(testStudent);
		given(studentRepository.findByFirstname(any())).willReturn(testStudents);

		// when
		List<Student> findStudents = studentService.getAllStudents("Asmaa", null, null);

		// then
		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(findStudents.size()).as("Check count of the Students found").isEqualTo(1);
			softly.assertThat(findStudents.get(0).getFirstname()).as("Check if the Student matches").isEqualTo("Asmaa");
		});
	}
	@Test
	@DisplayName("Test - getStudentsByLastName success")
	public void testStudentsByLasttName() {
		// given
		Student testStudent = new Student("id", "Asmaa", "Saad", "IT");
		List<Student> testStudents=new ArrayList<Student>();
		testStudents.add(testStudent);
		given(studentRepository.findByLastname(any())).willReturn(testStudents);

		// when
		List<Student> findStudents = studentService.getAllStudents(null, "Saad", null);

		// then
		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(findStudents.size()).as("Check count of the Students found").isEqualTo(1);
			softly.assertThat(findStudents.get(0).getLastname()).as("Check if the Student matches").isEqualTo("Saad");
		});
	}
	@Test
	@DisplayName("Test - getStudentsByDepartmentName success")
	public void testStudentsByDepartmenttName() {
		// given
		Student testStudent = new Student("id", "Asmaa", "Saad", "IT");
		List<Student> testStudents=new ArrayList<Student>();
		testStudents.add(testStudent);
		given(studentRepository.findByDepartmentName(any())).willReturn(testStudents);

		// when
		List<Student> findStudents = studentService.getAllStudents(null, null, "IT");

		// then
		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(findStudents.size()).as("Check count of the Students found").isEqualTo(1);
			softly.assertThat(findStudents.get(0).getDepartmentName()).as("Check if the Student matches").isEqualTo("IT");
		});
	}
	@Test
	@DisplayName("Test - getAllStudents success")
	public void testAllStudents() {
		// given
		Student testStudent = new Student("id", "Asmaa", "Saad", "IT");
		Student testStudent2 = new Student("xxx", "Adin", "John", "IT");
		List<Student> testStudents=new ArrayList<Student>();
		testStudents.add(testStudent);
		testStudents.add(testStudent2);
		given(studentRepository.findAll()).willReturn(testStudents);

		// when
		List<Student> findStudents = studentService.getAllStudents(null, null, null);

		// then
		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(findStudents.size()).as("Check count of the Students found").isEqualTo(2);
			
		});
	}
	@Test
	@DisplayName("Test - getAllStudents Pageable success")
	public void testAllStudentsPageable() {
		// given
		Student testStudent = new Student("id", "Asmaa", "Saad", "IT");
		Student testStudent2 = new Student("xxx", "Adin", "John", "IT");
		Student testStudent3 = new Student("id1", "Amr", "Mate", "IT");
		Student testStudent4 = new Student("xxxx", "Omar", "Arun", "IT");
		List<Student> testStudents=new ArrayList<Student>();
		testStudents.add(testStudent);
		testStudents.add(testStudent2);
		testStudents.add(testStudent3);
		testStudents.add(testStudent4);
		Page<Student> pro= new PageImpl<Student>(testStudents);
		
		Pageable paging = PageRequest.of(0, 4);
		given(studentRepository.findAll(paging)).willReturn(pro);

		// when
		List<Student> findStudents = studentService.getAllStudents(null, null, null,0,4);

		// then
		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(findStudents.size()).as("Check count of the Students found").isEqualTo(4);
			
		});
	}
	@Test
	@DisplayName("Test - testStudentsByFirstNamePageable Pageable success")
	public void testStudentsByFirstNamePageable() {
		// given
		
		Student testStudent4 = new Student("xxxx", "Omar", "Arun", "IT");
		Student testStudent5 = new Student("xxxxx", "Omar", "Arun", "IT");
		List<Student> testStudents=new ArrayList<Student>();
		
		testStudents.add(testStudent4);
		testStudents.add(testStudent5);
		Page<Student> pro= new PageImpl<Student>(testStudents);
		
		Pageable paging = PageRequest.of(0, 4);
		given(studentRepository.findByFirstname("Omar",paging)).willReturn(pro);

		// when
		List<Student> findStudents = studentService.getAllStudents("Omar", null, null,0,4);

		// then
		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(findStudents.size()).as("Check count of the Students found").isEqualTo(2);
			
		});
	}
	@Test
	@DisplayName("Test - testStudentsByFirstNamePageable Pageable success")
	public void testStudentsByLastNamePageable() {
		// given
		
		Student testStudent4 = new Student("xxxx", "Omar", "Arun", "IT");
		Student testStudent5 = new Student("xxxxx", "Omar", "Arun", "IT");
		List<Student> testStudents=new ArrayList<Student>();
		
		testStudents.add(testStudent4);
		testStudents.add(testStudent5);
		Page<Student> pro= new PageImpl<Student>(testStudents);
		
		Pageable paging = PageRequest.of(0, 4);
		given(studentRepository.findByLastname("Arun",paging)).willReturn(pro);

		// when
		List<Student> findStudents = studentService.getAllStudents (null,"Arun", null,0,4);

		// then
		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(findStudents.size()).as("Check count of the Students found").isEqualTo(2);
			
		});
	}
	@Test
	@DisplayName("Test - testStudentsByDepartmentNamePageable Pageable success")
	public void testStudentsByDepartmentNamePageable() {
		// given
		
		Student testStudent4 = new Student("xxxx", "Omar", "Arun", "IT");
		Student testStudent5 = new Student("xxxxx", "Omar", "Arun", "IT");
		List<Student> testStudents=new ArrayList<Student>();
		
		testStudents.add(testStudent4);
		testStudents.add(testStudent5);
		Page<Student> pro= new PageImpl<Student>(testStudents);
		
		Pageable paging = PageRequest.of(0, 4);
		given(studentRepository.findByDepartmentName("IT",paging)).willReturn(pro);

		// when
		List<Student> findStudents = studentService.getAllStudents(null, null, "IT",0,4);

		// then
		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(findStudents.size()).as("Check count of the Students found").isEqualTo(2);
			
		});
	}
	@Test
	@DisplayName("Test - testStudentsByFirstNamePageable Pageable success")
	public void testStudentsByFirstNamePageable_whenzerosize() {
		// given
		
		Student testStudent4 = new Student("xxxx", "Omar", "Arun", "IT");
		Student testStudent5 = new Student("xxxxx", "Omar", "Arun", "IT");
		List<Student> testStudents=new ArrayList<Student>();
		
		testStudents.add(testStudent4);
		testStudents.add(testStudent5);
		Page<Student> pro= new PageImpl<Student>(testStudents);
		
		Pageable paging = PageRequest.of(0, 10);
		given(studentRepository.findByFirstname("Omar",paging)).willReturn(pro);

		// when
		List<Student> findStudents = studentService.getAllStudents("Omar", null, null,0,0);

		// then
		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(findStudents.size()).as("Check count of the Students found").isEqualTo(2);
			
		});
	}
	@Test
	@DisplayName("Test - testGetAllStudents success")
	public void testGetAllStudents() {
		// given
		Student testStudent = new Student("id", "Asmaa", "Saad", "IT");
		Student testStudent2 = new Student("xxx", "Adin", "John", "IT");
		List<Student> testStudents=new ArrayList<Student>();
		testStudents.add(testStudent);
		testStudents.add(testStudent2);
		given(studentRepository.findAll()).willReturn(testStudents);

		// when
		List<Student> findStudents = studentService.getAllStudents();

		// then
		SoftAssertions.assertSoftly(softly -> {
			softly.assertThat(findStudents.size()).as("Check count of the Students found").isEqualTo(2);
			
		});
	}


}
