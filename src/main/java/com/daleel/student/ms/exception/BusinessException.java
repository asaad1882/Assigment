package com.daleel.student.ms.exception;

public class BusinessException extends Exception{
	
	private static final long serialVersionUID = 4795681272949828246L;
	private int errorCode;
	   private String message;
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	@Override
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public BusinessException(int errorCode, String message) {
		super();
		this.errorCode = errorCode;
		this.message = message;
	}
}
