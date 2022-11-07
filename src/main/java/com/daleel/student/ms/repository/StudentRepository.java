package com.daleel.student.ms.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.daleel.student.ms.model.Student;

public interface StudentRepository extends MongoRepository<Student, String> {
	List<Student> findByLastname(String lastname);

	List<Student> findByFirstname(String fisrtname);

	List<Student> findByDepartmentName(String departmentName);

}
