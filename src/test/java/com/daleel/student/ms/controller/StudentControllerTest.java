package com.daleel.student.ms.controller;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
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

import com.daleel.student.ms.data.StudentDTO;
import com.daleel.student.ms.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class StudentControllerTest {
	@MockBean
    private StudentService studentService;

    @Autowired
    private MockMvc mockMvc;
    private final UUID STUDENT_UNKNOWN = new UUID(0, 999);
    @BeforeEach
    void setup() throws Exception {
    	 mockMvc.perform(get("/api/bucketsReset").header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D"));
    }
    

    @Test
    @DisplayName("GET /students/{id} Asmaa - Success")
    public void testGetStudentById() throws Exception {
        //given

    	StudentDTO testStudent = new StudentDTO("id", "Asmaa", "Saad", "IT");
        given(studentService.getStudentById(any())).willReturn(testStudent);
        //when

        mockMvc.perform(get("/api/students/{id}", "id").header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D"))

        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value("Asmaa"));


    }
    @Test
    public void testGetStudentByHeader_InvalidHeader() throws Exception {
        mockMvc.perform(get("/api/students/{id}", "id").header("X-API-KEY", "test"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error-code").value("401"))
                .andExpect(jsonPath("$.error-message").value("Authorization header is invalid/not found"));
    }
    @Test
    public void testGetStudentByHeader_MissingHeader() throws Exception {
        mockMvc.perform(get("/api/students/{id}", "id"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error-code").value("401"))
                .andExpect(jsonPath("$.error-message").value("Authorization header is invalid/not found"));
    }
    @Test
    public void testGetStudentById_StudentUnknown() throws Exception {
    	mockMvc.perform(get("/api/students/{id}", STUDENT_UNKNOWN).header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D"))
                .andExpect(status().isNotFound());
    }
    @Test
    @DisplayName("GET /students by firstname Asmaa - Success")
    public void testGetStudentByFirstName() throws Exception {
        //given

    	StudentDTO testStudent = new StudentDTO("id", "Asmaa", "Saad", "IT");
    	List<StudentDTO> students=new ArrayList<>();
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
    public void testGetAllStudentPaged() throws Exception {
    	
        //given

    	StudentDTO testStudent = new StudentDTO("id", "Asmaa", "Saad", "IT");
    	List<StudentDTO> students=new ArrayList<>();
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
    public void testGetStudentByFirstNamePaged() throws Exception {
        //given

    	StudentDTO testStudent = new StudentDTO("id", "Asmaa", "Saad", "IT");
    	List<StudentDTO> students=new ArrayList<>();
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
    public void testGetStudentByFirstName_UnKnowFirstName() throws Exception {
        //given

    	
    	List<StudentDTO> students=new ArrayList<>();
    	
        given(studentService.getAllStudents("Test",null,null)).willReturn(students);
        //when

        mockMvc.perform(get("/api/students?firstname=Test").header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D"))

        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[:1].firstname").doesNotExist());
               


    }
    @Test
    @DisplayName("GET /students by firstname Test - Fail")
    public void testGetStudentByLastName_UnKnowLastName() throws Exception {
        //given

    	
    	List<StudentDTO> students=new ArrayList<>();
    	
        given(studentService.getAllStudents(null,"Test",null)).willReturn(students);
        //when

        mockMvc.perform(get("/api/students?lastname=Test").header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D"))

        //then
                .andExpect(status().isOk())
        .andExpect(jsonPath("$.[:1].lastname").doesNotExist());
               


    }
    @Test
    @DisplayName("GET /students by departmentname IT - Fail")
    public void testGetStudentByDepartmentName_UnKnownDepartmentName() throws Exception {
        //given

    	
    	List<StudentDTO> students=new ArrayList<>();
    	
        given(studentService.getAllStudents(null,null,"IT")).willReturn(students);
        //when

        mockMvc.perform(get("/api/students?departmentName=IT").header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D"))

        //then
                .andExpect(status().isOk())
        .andExpect(jsonPath("$.[:1].departmentName").doesNotExist());
               


    }
    @Test
    @DisplayName("GET /students by firstname Test - Fail")
    public void testGetStudentByFirstNamePaged_UnKnowFirstName() throws Exception {
        //given

    	
    	List<StudentDTO> students=new ArrayList<>();
    	
        given(studentService.getAllStudents("Test",null,null,0,1)).willReturn(students);
        //when

        mockMvc.perform(get("/api/students/0/1?firstname=Test").header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D"))

        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[:1].firstname").doesNotExist());
               


    }
    @Test
    @DisplayName("GET /students by firstname Test - Fail")
    public void testGetStudentByLastPaged_UnKnowLastName() throws Exception {
        //given

    	
    	List<StudentDTO> students=new ArrayList<>();
    	
        given(studentService.getAllStudents(null,"Test",null,0,1)).willReturn(students);
        //when

        mockMvc.perform(get("/api/students/0/1?lastname=Test").header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D"))

        //then
                .andExpect(status().isOk())
        .andExpect(jsonPath("$.[:1].lastname").doesNotExist());
               


    }
    @Test
    @DisplayName("GET /students All Paged Test - Fail")
    public void testGetAllStudentPaged_Empty() throws Exception {
        //given

    	
    	List<StudentDTO> students=new ArrayList<>();
    	
        given(studentService.getAllStudents(null,null,null,0,1)).willReturn(students);
        //when

        mockMvc.perform(get("/api/students/0/1").header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D"))

        //then
                .andExpect(status().isOk())
        .andExpect(jsonPath("$.[:1].lastname").doesNotExist());
               


    }
    @Test
    @DisplayName("GET /students by departmentname IT - Fail")
    public void testGetStudentByDepartmentNamePaged_UnKnowDepartmentName() throws Exception {
        //given

    	
    	List<StudentDTO> students=new ArrayList<>();
    	
        given(studentService.getAllStudents(null,null,"IT",0,1)).willReturn(students);
        //when

        mockMvc.perform(get("/api/students/0/1?departmentName=IT").header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D"))

        //then
                .andExpect(status().isOk())
        .andExpect(jsonPath("$.[:1].departmentName").doesNotExist());
               


    }
    @Test
    @DisplayName("GET /students by lastname Saad - Success")
    public void testGetStudentByLastName() throws Exception {
    	
        //given

    	StudentDTO testStudent = new StudentDTO("id", "Asmaa", "Saad", "IT");
    	List<StudentDTO> students=new ArrayList<>();
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
    public void testGetStudentByLastNamePaged() throws Exception {
        //given

    	StudentDTO testStudent = new StudentDTO("id", "Asmaa", "Saad", "IT");
    	List<StudentDTO> students=new ArrayList<>();
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
    public void testGetStudentByDepartmentName() throws Exception {
        //given

    	StudentDTO testStudent = new StudentDTO("id", "Asmaa", "Saad", "IT");
    	List<StudentDTO> students=new ArrayList<>();
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
    public void testGetStudentByDepartmentNamePaged() throws Exception {
        //given

    	StudentDTO testStudent = new StudentDTO("id", "Asmaa", "Saad", "IT");
    	List<StudentDTO> students=new ArrayList<>();
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
    public void testCreateStudent() throws Exception {
        //Given
    	StudentDTO testStudent = new StudentDTO("id", "Asmaa", "Saad", "IT");
        given(studentService.createStudent(any())).willReturn(testStudent);
        //when
        mockMvc.perform(post("/api/students")
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D")
        .content(asJsonString(testStudent)))
        //then
        .andExpect(status().isCreated());

    }
    @Test
    @DisplayName("POST /students Asmaa - Success")
    public void testCreateStudent_EmptyFirstName() throws Exception {
        //Given
    	StudentDTO testStudent = new StudentDTO("id", "", "Saad", "IT");
        given(studentService.createStudent(any())).willReturn(testStudent);
        //when
        mockMvc.perform(post("/api/students")
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D")
        .content(asJsonString(testStudent)))
        //then
        .andExpect(status().is4xxClientError());

    }
    @Test
    @DisplayName("POST /students Asmaa - Success")
    public void testCreateStudent_EmptyLastName() throws Exception {
        //Given
    	StudentDTO testStudent = new StudentDTO("id", "Asmaa", "", "IT");
        given(studentService.createStudent(any())).willReturn(testStudent);
        //when
        mockMvc.perform(post("/api/students")
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D")
        .content(asJsonString(testStudent)))
        //then
        .andExpect(status().is4xxClientError());

    }
    @Test
    @DisplayName("POST /students Asmaa - Success")
    public void testCreateStudent_EmptyDepartmentName() throws Exception {
        //Given
    	StudentDTO testStudent = new StudentDTO("id", "Asmaa", "Saad", "");
        given(studentService.createStudent(any())).willReturn(testStudent);
        //when
        mockMvc.perform(post("/api/students")
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D")
        .content(asJsonString(testStudent)))
        //then
        .andExpect(status().is4xxClientError());

    }
    @Test
    @DisplayName("POST /students Asmaa - Success")
    public void testCreateStudent_MinFirstName() throws Exception {
        //Given
    	StudentDTO testStudent = new StudentDTO("id", "A", "Saad", "IT");
        given(studentService.createStudent(any())).willReturn(testStudent);
        //when
        mockMvc.perform(post("/api/students")
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D")
        .content(asJsonString(testStudent)))
        //then
        .andExpect(status().is4xxClientError());

    }
    @Test
    @DisplayName("POST /students Asmaa - Success")
    public void testCreateStudent_MaxFirstName() throws Exception {
        //Given
    	StudentDTO testStudent = new StudentDTO("id", "ITTTTTTTTTTTTTTTTTTTT", "Saad", "IT");
        given(studentService.createStudent(any())).willReturn(testStudent);
        //when
        mockMvc.perform(post("/api/students")
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D")
        .content(asJsonString(testStudent)))
        //then
        .andExpect(status().is4xxClientError());

    }
    @Test
    @DisplayName("POST /students Asmaa - Success")
    public void testCreateStudent_MinLastName() throws Exception {
        //Given
    	StudentDTO testStudent = new StudentDTO("id", "Asmaa", "S", "IT");
        given(studentService.createStudent(any())).willReturn(testStudent);
        //when
        mockMvc.perform(post("/api/students")
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D")
        .content(asJsonString(testStudent)))
        //then
        .andExpect(status().is4xxClientError());

    }
    @Test
    @DisplayName("POST /students Asmaa - Success")
    public void testCreateStudent_MaxLastName() throws Exception {
        //Given
    	StudentDTO testStudent = new StudentDTO("id", "Asmaa", "Saad", "ITTTTTTTTTTTTTTTTTTTT");
        given(studentService.createStudent(any())).willReturn(testStudent);
        //when
        mockMvc.perform(post("/api/students")
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D")
        .content(asJsonString(testStudent)))
        //then
        .andExpect(status().is4xxClientError());

    }
    @Test
    @DisplayName("POST /students Asmaa - Success")
    public void testCreateStudent_MinDepartmentName() throws Exception {
        //Given
    	StudentDTO testStudent = new StudentDTO("id", "Asmaa", "Saad", "I");
        given(studentService.createStudent(any())).willReturn(testStudent);
        //when
        mockMvc.perform(post("/api/students")
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D")
        .content(asJsonString(testStudent)))
        //then
        .andExpect(status().is4xxClientError());

    }
    @Test
    @DisplayName("POST /students Asmaa - Success")
    public void testCreateStudent_MaxDepartmentName() throws Exception {
        //Given
    	StudentDTO testStudent = new StudentDTO("id", "Asmaa", "Saad", "ITTTTTTTTTTTTTTTTTTTT");
        given(studentService.createStudent(any())).willReturn(testStudent);
        //when
        mockMvc.perform(post("/api/students")
        .contentType(MediaType.APPLICATION_JSON)
        .header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D")
        .content(asJsonString(testStudent)))
        //then
        .andExpect(status().is4xxClientError());

    }
    


    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
