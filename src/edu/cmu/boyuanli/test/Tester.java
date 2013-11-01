/**
 * @(#) Tester.java	1.0 13th, Sep 2013
 * 
 */
package edu.cmu.boyuanli.test;

import edu.cmu.boyuanli.business.ShoppingDotComAnalyzer;

/**
 * 
 * The class Tester is a simple tester based on the following description
 * 
 * Encapsulate your assignment inside an executable jar (e.g. java -jar Assignment.jar ...)
 * Handle the two queries above:
 * 
 * Query 1: (requires a single argument)
 * java -jar Assignment.jar <keyword> (e.g. java -jar Assignment.jar "baby strollers")
 * 
 * Query 2: (requires two arguments)
 * java -jar Assignment.jar <keyword> <page number> (e.g. java -jar Assignment.jar "baby strollers" 2)
 * 
 * @author Boyuan Li, CMU
 * @version 1.0 13th, Sep 2013
 * @since JDK6.0
 */
public class Tester {
	public static void main(String[] args) {
		String keyword = null;
		int pageNo;
		boolean pageFlag = true;
		try {
			keyword = args[0];
		} catch (Exception e){
			System.out.println("keyword illegal, please retry with correct command");
		}
		try {
			pageNo = Integer.parseInt(args[1]);
		} catch (Exception e){
			System.out.println("Page Number illegal, we will set 1 as page number, the program is running: ");
			pageNo = 1;
			pageFlag = false;
		}
		ShoppingDotComAnalyzer s = new ShoppingDotComAnalyzer(keyword, pageNo);
		s.printResult(pageFlag);
	}
}
