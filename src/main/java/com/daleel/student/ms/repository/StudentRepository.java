package com.daleel.student.ms.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.daleel.student.ms.model.Student;

public interface StudentRepository extends MongoRepository<Student, String> {
	List<Student> findByLastname(String lastname);

	List<Student> findByFirstname(String fisrtname);

	List<Student> findByDepartmentName(String departmentName);
	Page<Student> findByFirstname(String firstname, Pageable pageable);
	Page<Student> findByLastname(String lastname, Pageable pageable);
	Page<Student> findByDepartmentName(String departmentName, Pageable pageable);
	Page<Student> findAll( Pageable pageable);
}
