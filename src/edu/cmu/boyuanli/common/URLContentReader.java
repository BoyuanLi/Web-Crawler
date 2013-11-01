/**
 * @(#) URLContentReader.java 1.0 13th, Sep 2013
 * 
 */
package edu.cmu.boyuanli.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.URL;

/**
 * URLContentReader is single source of page content in a single line, in the format of String
 * In runtime, it will deal with IOException, it will return null if nothing can be found
 * 
 * @author Boyuan Li, CMU
 * @version 1.0 13th, Sep 2013
 * @since JDK6.0
 */

public class URLContentReader {
	public static String readContentFromURL(URL url) {
		StringBuilder temp = new StringBuilder();
		InputStream input = null;
		System.out.println("Reading HTML Content ...");
		try {
			input = url.openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				if (!inputLine.equals("") && 
					!inputLine.equals("\t") && 
					!inputLine.equals(" ")) {
					temp.append(inputLine);
				}
			}
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return temp.toString();
	}
}
