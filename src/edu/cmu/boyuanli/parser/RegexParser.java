/**
 * @(#) RegexParser.java 1.0 13th, Sep 2013
 * 
 */
package edu.cmu.boyuanli.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.cmu.boyuanli.common.ParserException;

/**
 * 
 * The class RegexParser is the implementation of IRegexParser
 * 
 * @author Boyuan Li, CMU
 * @version 1.0 13th, Sep 2013
 * @since JDK6.0
 */
public class RegexParser implements IRegexParser {

	/**
	 * extractSingleContentFromHTML will return single result based on given
	 * html content, regex and target group if it can't find the right
	 * information ParserException will be thrown
	 */
	public String extractSingleContentFromHTML (String htmlContent,
			String regex, int target) throws ParserException{
		String result = null;
		try {
			Matcher matcher = matchRegex(htmlContent, regex);
			if (matcher.find())
				result = matcher.group(target);
		} catch (Exception e) {
			throw new ParserException("Error: " + e.getStackTrace()[1].getMethodName());
		}
		return result;
	}

	/**
	 * matchRegex will return a Matcher wrapped with the result 
	 * if it can't find the right information ParserException will be thrown
	 */
	public Matcher matchRegex(String content, String regex) throws ParserException{
		try {
			Pattern pattern = Pattern.compile(regex);
			return pattern.matcher(content);
		} catch (Exception e) {
			throw new ParserException("Error: " + e.getStackTrace()[1].getMethodName());
		}
	}
}
