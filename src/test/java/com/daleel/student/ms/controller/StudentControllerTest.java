package com.daleel.student.ms.controller;
import com.daleel.student.ms.model.Student;
import com.daleel.student.ms.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class StudentControllerTest {
	@MockBean
    private StudentService studentService;

    @Autowired
    private MockMvc mockMvc;
    @Before
    void setup() throws IOException{
    	try {
    		System.out.println("WAIT!!!!!!!!!!!!!!!!!!!!!!!");
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Test
    @DisplayName("GET /students/{id} Asmaa - Success")
    public void getStudentByIdSuccess() throws Exception {
        //given

    	Student testStudent = new Student("id", "Asmaa", "Saad", "IT");
        given(studentService.getStudentById(any())).willReturn(testStudent);
        //when

        mockMvc.perform(get("/api/students/{id}", "id").header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D"))

        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value("Asmaa"));


    }
    @Test
    @DisplayName("GET /students by firstname Asmaa - Success")
    public void getStudentByFirstNameSuccess() throws Exception {
        //given

    	Student testStudent = new Student("id", "Asmaa", "Saad", "IT");
    	List<Student> students=new ArrayList<Student>();
    	students.add(testStudent);
        given(studentService.getAllStudents(any(),any(),any())).willReturn(students);
        //when

        mockMvc.perform(get("/api/students?firstname=Asmaa").header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D"))

        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[:1].firstname").value("Asmaa"));


    }
    
    @Test
    @DisplayName("GET /students  paged Asmaa - Success")
    public void getAllStudentPagedSuccess() throws Exception {
    	
        //given

    	Student testStudent = new Student("id", "Asmaa", "Saad", "IT");
    	List<Student> students=new ArrayList<Student>();
    	students.add(testStudent);
        given(studentService.getAllStudents(null,null,null,0,1)).willReturn(students);
        //when

        mockMvc.perform(get("/api/students/0/1").header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D"))

        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[:1].firstname").value("Asmaa"));


    }
    @Test
    @DisplayName("GET /students by firstname paged Asmaa - Success")
    public void getStudentByFirstNamePagedSuccess() throws Exception {
        //given

    	Student testStudent = new Student("id", "Asmaa", "Saad", "IT");
    	List<Student> students=new ArrayList<Student>();
    	students.add(testStudent);
        given(studentService.getAllStudents("Asmaa",null,null,0,1)).willReturn(students);
        //when

        mockMvc.perform(get("/api/students/0/1?firstname=Asmaa").header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D"))

        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[:1].firstname").value("Asmaa"));


    }
    @Test
    @DisplayName("GET /students by firstname Test - Fail")
    public void getStudentByFirstNameFailure() throws Exception {
        //given

    	
    	List<Student> students=new ArrayList<Student>();
    	
        given(studentService.getAllStudents("Test",null,null)).willReturn(students);
        //when

        mockMvc.perform(get("/api/students?firstname=Test").header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D"))

        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[:1].firstname").doesNotExist());
               


    }
    @Test
    @DisplayName("GET /students by firstname Test - Fail")
    public void getStudentByLastNameFailure() throws Exception {
        //given

    	
    	List<Student> students=new ArrayList<Student>();
    	
        given(studentService.getAllStudents(null,"Test",null)).willReturn(students);
        //when

        mockMvc.perform(get("/api/students?lastname=Test").header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D"))

        //then
                .andExpect(status().isOk())
        .andExpect(jsonPath("$.[:1].lastname").doesNotExist());
               


    }
    @Test
    @DisplayName("GET /students by departmentname IT - Fail")
    public void getStudentByDepartmentNameFailure() throws Exception {
        //given

    	
    	List<Student> students=new ArrayList<Student>();
    	
        given(studentService.getAllStudents(null,null,"IT")).willReturn(students);
        //when

        mockMvc.perform(get("/api/students?departmentName=IT").header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D"))

        //then
                .andExpect(status().isOk())
        .andExpect(jsonPath("$.[:1].departmentName").doesNotExist());
               


    }
    @Test
    @DisplayName("GET /students by firstname Test - Fail")
    public void getStudentByFirstNamePagedFailure() throws Exception {
        //given

    	
    	List<Student> students=new ArrayList<Student>();
    	
        given(studentService.getAllStudents("Test",null,null,0,1)).willReturn(students);
        //when

        mockMvc.perform(get("/api/students/0/1?firstname=Test").header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D"))

        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[:1].firstname").doesNotExist());
               


    }
    @Test
    @DisplayName("GET /students by firstname Test - Fail")
    public void getStudentByLastNamePagedFailure() throws Exception {
        //given

    	
    	List<Student> students=new ArrayList<Student>();
    	
        given(studentService.getAllStudents(null,"Test",null,0,1)).willReturn(students);
        //when

        mockMvc.perform(get("/api/students/0/1?lastname=Test").header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D"))

        //then
                .andExpect(status().isOk())
        .andExpect(jsonPath("$.[:1].lastname").doesNotExist());
               


    }
    @Test
    @DisplayName("GET /students All Paged Test - Fail")
    public void getAllStudentPagedFailure() throws Exception {
        //given

    	
    	List<Student> students=new ArrayList<Student>();
    	
        given(studentService.getAllStudents(null,null,null,0,1)).willReturn(students);
        //when

        mockMvc.perform(get("/api/students/0/1").header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D"))

        //then
                .andExpect(status().isOk())
        .andExpect(jsonPath("$.[:1].lastname").doesNotExist());
               


    }
    @Test
    @DisplayName("GET /students by departmentname IT - Fail")
    public void getStudentByDepartmentNamePagedFailure() throws Exception {
        //given

    	
    	List<Student> students=new ArrayList<Student>();
    	
        given(studentService.getAllStudents(null,null,"IT",0,1)).willReturn(students);
        //when

        mockMvc.perform(get("/api/students/0/1?departmentName=IT").header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D"))

        //then
                .andExpect(status().isOk())
        .andExpect(jsonPath("$.[:1].departmentName").doesNotExist());
               


    }
    @Test
    @DisplayName("GET /students by lastname Saad - Success")
    public void getStudentByLastNameSuccess() throws Exception {
    	Thread.sleep(60000);
        //given

    	Student testStudent = new Student("id", "Asmaa", "Saad", "IT");
    	List<Student> students=new ArrayList<Student>();
    	students.add(testStudent);
        given(studentService.getAllStudents(any(),any(),any())).willReturn(students);
        //when

        mockMvc.perform(get("/api/students?lastname=Saad").header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D"))

        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[:1].lastname").value("Saad"));


    }
    @Test
    @DisplayName("GET /students by lastname Paged Saad - Success")
    public void getStudentByLastNamePagedSuccess() throws Exception {
        //given

    	Student testStudent = new Student("id", "Asmaa", "Saad", "IT");
    	List<Student> students=new ArrayList<Student>();
    	students.add(testStudent);
        given(studentService.getAllStudents(null,"Saad",null,0,1)).willReturn(students);
        //when

        mockMvc.perform(get("/api/students/0/1?lastname=Saad").header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D"))

        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[:1].lastname").value("Saad"));


    }
    @Test
    @DisplayName("GET /students by Department Name IT - Success")
    public void getStudentByDepartmentNameSuccess() throws Exception {
        //given

    	Student testStudent = new Student("id", "Asmaa", "Saad", "IT");
    	List<Student> students=new ArrayList<Student>();
    	students.add(testStudent);
        given(studentService.getAllStudents(null,null,"IT")).willReturn(students);
        //when

        mockMvc.perform(get("/api/students?departmentName=IT").header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D"))

        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[:1].departmentName").value("IT"));


    }
    @Test
    @DisplayName("GET /students by Department Name IT - Success")
    public void getStudentByDepartmentNamePagedSuccess() throws Exception {
        //given

    	Student testStudent = new Student("id", "Asmaa", "Saad", "IT");
    	List<Student> students=new ArrayList<Student>();
    	students.add(testStudent);
        given(studentService.getAllStudents(null,null,"IT",0,1)).willReturn(students);
        //when

        mockMvc.perform(get("/api/students/0/1?departmentName=IT").header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D"))

        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[:1].departmentName").value("IT"));


    }

    @Test
    @DisplayName("POST /students Asmaa - Success")
    public void createStudentSuccess() throws Exception {
        //Given
    	Student testStudent = new Student("id", "Asmaa", "Saad", "IT");
        given(studentService.createStudent(any())).willReturn(testStudent);
        //when
        mockMvc.perform(post("/api/students")
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D")
        .content(asJsonString(testStudent)))
        //then
        .andExpect(status().isCreated());

    }
    
    


    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
