package com.daleel.student.ms.model;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "students")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student {
	@Id
	private String id;
	@NotBlank
	@Indexed(unique = false)
	private String firstname;
	@NotBlank
	@Indexed(unique = false)
	private String lastname;
	@NotBlank
	@Indexed(unique = false)
	private String departmentName;
	
}
