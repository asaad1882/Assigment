package com.daleel.student.ms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import com.daleel.student.ms.model.Student;
import com.daleel.student.ms.repository.StudentRepository;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {
	private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);
	@Autowired
	private StudentRepository studentRepository;

	@Override
	public Student createStudent(Student student) {
		return studentRepository.save(student);
	}


	@Override
	public List<Student> getAllStudents() {
		return studentRepository.findAll();
	}

	@Override
	public Student getStudentById(String studentId) {
      Optional<Student> studentDb = this.studentRepository.findById(studentId);
		
		if(studentDb.isPresent()) {
			return studentDb.get();
		}else {
			logger.error("Student not found id:{}",studentId);
			return null;
		}
	}



	@Override
	public List<Student> getAllStudents(String firstname, String lastname, String departmentName) {
		 List<Student> students = new ArrayList<>();
		 List<Student> dbStudents=null;
	      if (StringUtils.isEmpty(firstname)  && StringUtils.isEmpty(lastname) && StringUtils.isEmpty(departmentName))
	    	  dbStudents= studentRepository.findAll();
	     
	      else if(!StringUtils.isEmpty(firstname) && StringUtils.isEmpty(lastname) && StringUtils.isEmpty(departmentName)) {
	    	  dbStudents=studentRepository.findByFirstname(firstname);
	    	  
	      }
	      else if(StringUtils.isEmpty(firstname) && !StringUtils.isEmpty(lastname) && StringUtils.isEmpty(departmentName))
	    	  dbStudents=studentRepository.findByLastname(lastname);
	      else if(StringUtils.isEmpty(firstname) && StringUtils.isEmpty(lastname) && !StringUtils.isEmpty(departmentName))
	    	  dbStudents=studentRepository.findByDepartmentName(departmentName);
	      if(dbStudents!=null) {
	    	  dbStudents.forEach(students::add);
	    	  }
		return students;
	}

}
