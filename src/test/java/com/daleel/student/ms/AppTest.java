package com.daleel.student.ms;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


import com.daleel.student.ms.controller.StudentController;
@SpringBootTest
@RunWith(SpringRunner.class)

@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class AppTest {
	@Autowired
	private StudentController controller;
	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}
	  @Test
	     void main() {
	        App.main(new String[] {});
	    }

}
