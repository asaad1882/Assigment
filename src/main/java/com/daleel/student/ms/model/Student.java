package com.daleel.student.ms.model;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "students")
public class Student {
	public Student(String firstname, String lastname, String departmentName) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.departmentName = departmentName;
	}
	public Student(String id,String firstname, String lastname, String departmentName) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.departmentName = departmentName;
		this.id=id;
	}
	public Student() {
		
	}


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

	

}
