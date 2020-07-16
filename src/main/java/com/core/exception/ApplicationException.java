package com.core.exception;

/**
 * 应用异常
 */
public class ApplicationException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public ApplicationException(Throwable e) {
		super(e);
	}
	
	public ApplicationException(String message) {
		super(message);
	}
	
}