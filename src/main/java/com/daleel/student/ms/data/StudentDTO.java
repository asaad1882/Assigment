package com.daleel.student.ms.data;


import javax.validation.constraints.NotEmpty;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
	private String id;
	@NotEmpty(message = "The firstname is required.") 
	@Size(min = 2, max = 20, message = "The length of firstname must be between 2 and 20 characters.")
	private String firstname;
	@NotEmpty(message = "The lastname is required.") 
	@Size(min = 2, max = 20, message = "The length of lastname must be between 2 and 20 characters.")
	private String lastname;
	@NotEmpty(message = "The departmentName is required.") 
	@Size(min = 2, max = 20, message = "The length of departmentName must be between 2 and 20 characters.")
	private String departmentName;
	
	

}
