/**
 * @(#) IRegexParser.java 1.0 20th, Sep 2013
 * 
 */
package edu.cmu.boyuanli.parser;

import java.util.regex.Matcher;

import edu.cmu.boyuanli.common.ParserException;

/**
 * 
 * The interface IRegexParser is the first level interface,
 * it defines matchRegex, which will return matched result as Matcher.
 * 
 * This is the case we use Regex.
 * 
 * @author Boyuan Li, CMU
 * @version 1.0 13th, Sep 2013
 * @since JDK6.0
 */
public interface IRegexParser {
	Matcher matchRegex(String htmlContent, String regex) throws ParserException;

	String extractSingleContentFromHTML (String content, String pattern, int target) throws ParserException;
}
