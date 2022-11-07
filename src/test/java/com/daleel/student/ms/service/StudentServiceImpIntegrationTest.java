package com.daleel.student.ms.service;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.daleel.student.ms.model.Student;
import com.daleel.student.ms.repository.StudentRepository;
import static org.assertj.core.api.Assertions.assertThat;



@RunWith(SpringRunner.class)
public class StudentServiceImpIntegrationTest {
//	@TestConfiguration
//    static class StudentServiceImplTestContextConfiguration {
//        @Bean
//        public StudentService StudentService() {
//            return new StudentServiceImpl();
//        }
//    }

    @Autowired
    private StudentService studentService;

    @MockBean
    private StudentRepository studentRepository;
    

    @Before
    public void setUp() {
    	Student testStudent = new Student("id", "Asmaa", "Saad", "IT");
    	Student testStudent2 = new Student("123", "Asma", "Mahmoud", "IT");
    	Student testStudent3 = new Student("124", "Manar", "Ahmed", "IT");

        List<Student> allStudents = Arrays.asList(testStudent, testStudent2, testStudent3);
        List<Student> allStudents2 = Arrays.asList(testStudent);
        List<Student> allStudents3 = Arrays.asList(testStudent2);

        Mockito.when(studentRepository.findByFirstname(testStudent.getFirstname())).thenReturn(allStudents2);
        Mockito.when(studentRepository.findByFirstname(testStudent2.getFirstname())).thenReturn(allStudents3);
        Mockito.when(studentRepository.findByLastname(testStudent.getLastname())).thenReturn(allStudents2);
        Mockito.when(studentRepository.findByFirstname("wrong_name")).thenReturn(null);
        Mockito.when(studentRepository.findById(testStudent2.getId())).thenReturn(Optional.of(testStudent2));
        Mockito.when(studentRepository.findAll()).thenReturn(allStudents);
        Mockito.when(studentRepository.findById("1235")).thenReturn(Optional.empty());
        Student svStudent = new Student("xxxx", "Eden", "McBride", "IT");
        Mockito.when(studentRepository.save(svStudent)).thenReturn(svStudent);
    }
    @Test
    public void whenAddNewStudent_thenStudentShouldBeReturned() {
    	Student testStudent = new Student("xxxx", "Eden", "McBride", "IT");
        Student found = studentService.createStudent(testStudent);
        
        assertThat(found.getFirstname()).isEqualTo(testStudent.getFirstname());
    }


    @Test
    public void whenValidFirstName_thenStudentShouldBeFound() {
        String name = "Asmaa";
        List<Student> found = studentService.getAllStudents(name, null, null);

        assertThat(found.get(0).getFirstname()).isEqualTo(name);
    }
   
    @Test
    public void whenInValidFirstName_thenStudentShouldNotBeFound() {
        List<Student> fromDb = studentService.getAllStudents("wrong_name", null, null);
        assertThat(fromDb).isEmpty();

        verifyFindByNameIsCalledOnce("wrong_name");
    }

    @Test
    public void whenValidLastName_thenStudentShouldExist() {
    	   String name = "Saad";
           List<Student> found = studentService.getAllStudents(null, name, null);
      
           assertThat(found.get(0).getLastname()).isEqualTo(name);

        
    }

    @Test
    public void whenNonExistingLastName_thenStudentShouldNotExist() {
    	List<Student> fromDb = studentService.getAllStudents(null, "wrong_name", null);
        assertThat(!fromDb.isEmpty()).isEqualTo(false);

       
    }

    @Test
    public void whenValidId_thenStudentShouldBeFound() {
        Student fromDb = studentService.getStudentById("123");
        assertThat(fromDb.getFirstname()).isEqualTo("Asma");

        verifyFindByIdIsCalledOnce();
    }

    @Test
    public void whenInValidId_thenEmployeeShouldNotBeFound() {
    	Student fromDb = studentService.getStudentById("1235");
        verifyFindByIdIsCalledOnce();
        assertThat(fromDb).isNull();
    }

    private void verifyFindByNameIsCalledOnce(String name) {
        Mockito.verify(studentRepository, VerificationModeFactory.times(1)).findByFirstname(name);
        Mockito.reset(studentRepository);
    }

    private void verifyFindByIdIsCalledOnce() {
        Mockito.verify(studentRepository, VerificationModeFactory.times(1)).findById(Mockito.anyString());
        Mockito.reset(studentRepository);
    }

}
