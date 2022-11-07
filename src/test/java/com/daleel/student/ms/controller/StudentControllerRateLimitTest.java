package com.daleel.student.ms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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

import com.daleel.student.ms.model.Student;
import com.daleel.student.ms.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc

public class StudentControllerRateLimitTest {
	@MockBean
	private StudentService studentService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("GET /students by firstname Asmaa - Success")
	public void getStudentByFirstNameExhastLimit() throws Exception {
		// given

		Student testStudent = new Student("id", "Asmaa", "Saad", "IT");
		List<Student> students = new ArrayList<Student>();
		students.add(testStudent);
		given(studentService.getAllStudents(any(), any(), any())).willReturn(students);
		// when
		for (int i = 1; i <= 50; i++) {
			mockMvc.perform(get("/api/students?firstname=Asmaa").header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D"))

					// then
					.andExpect(status().isOk()).andExpect(jsonPath("$.[:1].firstname").value("Asmaa"));
		}
		mockMvc.perform(get("/api/students?firstname=Asmaa").header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D"))

				.andExpect(status().isTooManyRequests());

	}

	@Test
	@DisplayName("GET /students  paged Asmaa - Success")
	public void getAllStudentPagedExhastLimit() throws Exception {
		// given

		Student testStudent = new Student("id", "Asmaa", "Saad", "IT");
		List<Student> students = new ArrayList<Student>();
		students.add(testStudent);
		given(studentService.getAllStudents(null, null, null, 0, 1)).willReturn(students);
		// when
		for (int i = 1; i <= 50; i++) {
			mockMvc.perform(get("/api/students/0/1").header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D"))

					// then
					.andExpect(status().isOk()).andExpect(jsonPath("$.[:1].firstname").value("Asmaa"));
		}
		mockMvc.perform(get("/api/students/0/1").header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D"))

				.andExpect(status().isTooManyRequests());

	}

	@Test
	@DisplayName("POST /students Asmaa - Success")
	public void createStudentSuccess() throws Exception {
		// Given
		Student testStudent = new Student("id", "Asmaa", "Saad", "IT");
		given(studentService.createStudent(any())).willReturn(testStudent);
		// when
		for (int i = 1; i <= 50; i++) {
			mockMvc.perform(post("/api/students").contentType(MediaType.APPLICATION_JSON)
					.header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D").content(asJsonString(testStudent)))
					// then
					.andExpect(status().isCreated());
		}
		mockMvc.perform(post("/api/students").contentType(MediaType.APPLICATION_JSON)
				.header("X-API-KEY", "184DA27F6D8E9181EB44DA79A983D").content(asJsonString(testStudent)))
				.andExpect(status().isTooManyRequests());

	}

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
