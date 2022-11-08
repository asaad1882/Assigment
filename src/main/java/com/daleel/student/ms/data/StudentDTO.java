package com.daleel.student.ms.data;


import javax.validation.constraints.NotEmpty;

import javax.validation.constraints.Size;

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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public StudentDTO(String id,  String firstname,  String lastname,
			 String departmentName) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.departmentName = departmentName;
	}
	public StudentDTO() {
		
	}

}
