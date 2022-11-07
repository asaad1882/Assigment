package com.daleel.student.ms;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;



@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Students API", version = "1.0", description = "Students Information"), security = { @SecurityRequirement(name = "api_key")})
@SecurityScheme(name = "X-API-KEY",  type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.HEADER)
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
