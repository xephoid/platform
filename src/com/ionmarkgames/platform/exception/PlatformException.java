package com.ionmarkgames.platform.exception;

public class PlatformException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PlatformException(String msg) {
		super(msg);
	}
	
	public PlatformException(String msg, Throwable t) {
		super(msg, t);
	}
}