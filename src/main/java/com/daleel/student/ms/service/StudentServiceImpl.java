package com.daleel.student.ms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daleel.student.ms.data.StudentDTO;
import com.daleel.student.ms.model.Student;
import com.daleel.student.ms.repository.StudentRepository;
import org.springframework.cache.annotation.CacheEvict;

import org.springframework.cache.annotation.Caching;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
@CacheConfig(cacheNames={"students"})   
public class StudentServiceImpl implements StudentService {
	
	
	private final StudentRepository studentRepository;
	private final ModelMapper modelMapper;

	@Caching(evict = {
            @CacheEvict(value="student", allEntries=true),
            @CacheEvict(value="students", allEntries=true)})
	@Override
	public StudentDTO createStudent(StudentDTO student) {
		return mapToDTO(studentRepository.save(mapToEntity(student)));
	}


	@Override
	public List<StudentDTO> getAllStudents() {
		return mapToDTOs(studentRepository.findAll());
	}
	@Cacheable("student")
	@Override
	public StudentDTO getStudentById(String studentId) {
      Optional<Student> studentDb = this.studentRepository.findById(studentId);
		
		if(studentDb.isPresent()) {
			return mapToDTO(studentDb.get());
		}else {
			log.error("Student not found id:{}",studentId);
			return null;
		}
	}


	@Cacheable("students")
	@Override
	public List<StudentDTO> getAllStudents(String firstname, String lastname, String departmentName) {
		 List<StudentDTO> students = new ArrayList<>();
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
	    	  return mapToDTOs(dbStudents);
	    	  }
		return students;
	}
	@Override
	public List<StudentDTO> getAllStudents(String firstname, String lastname, String departmentName,int page,int size) {
		 List<StudentDTO> students=new ArrayList<>();
		 Page<Student> dbStudents=null;
		 if(size==0) {
			 size=10;
		 }
		 Pageable paging = PageRequest.of(page, size);
	      if (StringUtils.isEmpty(firstname)  && StringUtils.isEmpty(lastname) && StringUtils.isEmpty(departmentName))
	    	  dbStudents= studentRepository.findAll(paging);
	     
	      else if(!StringUtils.isEmpty(firstname) && StringUtils.isEmpty(lastname) && StringUtils.isEmpty(departmentName)) {
	    	  dbStudents=studentRepository.findByFirstname(firstname,paging);
	    	  
	      }
	      else if(StringUtils.isEmpty(firstname) && !StringUtils.isEmpty(lastname) && StringUtils.isEmpty(departmentName))
	    	  dbStudents=studentRepository.findByLastname(lastname,paging);
	      else if(StringUtils.isEmpty(firstname) && StringUtils.isEmpty(lastname) && !StringUtils.isEmpty(departmentName))
	    	  dbStudents=studentRepository.findByDepartmentName(departmentName,paging);
	     if(dbStudents!=null) {
	    	 students=mapToDTOs(dbStudents.getContent());

	     }
		return students;
	}
	private StudentDTO mapToDTO(Student student) {
		return modelMapper.map(student,StudentDTO.class);
	}
	private Student mapToEntity(StudentDTO student) {
		return modelMapper.map(student,Student.class);
	}
	private List<StudentDTO> mapToDTOs(List<Student> students) {
		if(!students.isEmpty()) {
		return students.stream().map(student-> modelMapper.map(student, StudentDTO.class))
				.collect(Collectors.toList());
		}else {
			return new ArrayList<StudentDTO>();
		}
	}

}
