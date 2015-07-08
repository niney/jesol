package com.coupang.common.exception;

/**
 * 준비과정이 필요할 때 Exception
 * @author niney
 *
 */
public class PrepareException extends Exception {
	
	private static final long serialVersionUID = -7931471585130935234L;

	public PrepareException() {
		super("need search");
	}

}
