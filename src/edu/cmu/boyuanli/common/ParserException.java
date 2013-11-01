/**
 * @(#) ParserException.java 1.0 13th, Sep 2013
 * 
 */
package edu.cmu.boyuanli.common;

/**
 * 
 * The class ParserException will be used to deal with all kinds of Exception in the Parser layer,
 * in business layer, we will control ParserException, and try to maintain good user experience
 * 
 * @author Boyuan Li, CMU
 * @version 1.0 13th, Sep 2013
 * @since JDK6.0
 */
public class ParserException extends Exception {
	private static final long serialVersionUID = 1L;
	private String message;

	public ParserException(String message) {
		this.message = message;
	}

	public String toString() {
		return message;
	}
}
