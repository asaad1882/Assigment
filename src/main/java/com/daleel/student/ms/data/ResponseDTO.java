package com.daleel.student.ms.data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class ResponseDTO {
	private String timestamp;

    @Schema(required = true, description = "HTTP Status Code of the error")
    @JsonProperty("error-code")
    private int errorCode;

    @Schema(required = true, description = "Description of the Error")
    @JsonProperty("error-message")
    private String error;
    public ResponseDTO(int errorCode,String error) {
        this.setTimestamp(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()));
        this.errorCode = errorCode;
        this.error = error;
    }
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

}
