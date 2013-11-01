/**
 * @(#) ShoppingDotComAnalyzer.java	1.0 13th, Sep 2013
 * 
 */
package edu.cmu.boyuanli.business;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;
import java.net.MalformedURLException;
import java.net.URL;

import edu.cmu.boyuanli.parser.*;
import edu.cmu.boyuanli.model.Product;
import edu.cmu.boyuanli.common.ParserException;
import edu.cmu.boyuanli.common.URLContentReader;

/**
 * 
 * The class ShoppingDotComAnalyzer contains the core logic of Shopping.com scrapper.
 * It will answer the following two queries:
 * 
 * Query 1: Total number of results
 * Given a keyword, such as "digital camera", return the total number of results found.
 * 
 * Query 2: Result Object
 * Given a keyword (e.g. "digital camera") and page number (e.g. "3"), return the results in a result object and then print results on screen. For each result, return the following information:
 * Title/Product Name (e.g. "Sony T7 Digital Camera")
 * Price of the product
 * Shipping Price (e.g. "Free Shipping", "$5.00")
 * Vendor (e.g. "Amazon", "5 stores")
 * 
 * For "digital cameras", there should be either 40 or 80 results that return for page 1.
 * 
 * @author Boyuan Li, CMU
 * @version 1.00 13th, Sep 2013
 * @since JDK6.0
 */
public class ShoppingDotComAnalyzer {
	/**
	 * all the patterns used for shopping.com text scraper
	 */
	final static String NUMBER_OF_RESULT_PATTERN = "<span class=\"numTotalResults\">Results ([0-9]|,)* - ([0-9]|,)* of (.*?)</span>";
	final static String SLICING_PRODUCTS_PATTERN = "id=\"quickLookItem-[0-9]*\">(.*?)<div id=\"descQA[0-9]*\"";
	final static String PRODUCT_NAME_PATTERN = "span class=\"quickLookGridItemFullName hide\">(.*?)</span>";
	final static String PRODUCT_PRICE_PATTERN1 = "\">&#36;(.*?)</";
	final static String PRODUCT_SHIPPING_PRICE_PATTERN1 = "class=\"freeShip\">(.*?)</";
	final static String PRODUCT_SHIPPING_PRICE_PATTERN2 = "class=\"missCalc\">(.*?)</";
	final static String PRODUCT_SHIPPING_PRICE_PATTERN3 = "class=\"calc\">(.*?)</span>";
	final static String PRODUCT_VENDOR_PATTERN1 = "id=\"numStoresQA[0-9]*\">(.*?)</span>";
	final static String PRODUCT_VENDOR_PATTERN2 = "class=\"newMerchantName\">(.*?)</";
	
    /**
     * numberOfResults holds the result of Query1  
     */
	private String numberOfResults;
	
	/**
     * result holds a List of Product, which is the result of Query2 
     */
	private List<Product> result = new ArrayList<Product>();
	
    /**
     * parser holds Parser instance of the class implements IRegexParser
     */
	private IRegexParser parser;
	
    /**
     * pageContent stores the page content as a single line of String 
     */
	private String pageContent;
	
    /**
     * keyword will be defined by the user in constructor 
     */
	private String keyword;
	
    /**
     * pageNumber will be defined by the user in constructor 
     */
	private int pageNumber;
	
    /**
     * ShoppingDotComAnalyzer Constructor will initialize the analyzer
     * by initializing pageNumber, keyword, getting Parser instance, and
     * getting pageContent as String
     * 
     * @param keyword is the keyword will be searched
     * @param pagenumber is the page number will be searched
     */
	public ShoppingDotComAnalyzer(String keyword, int pagenumber) {
		this.pageNumber = pagenumber;
		this.keyword = keyword;
		this.parser = new RegexParser();
		this.pageContent = URLContentReader.readContentFromURL(formURL());
	}
	
	/**
	 * formURL will form url for shopping.com given keyword and pageNumber has been
	 * initialized
	 * 
	 * @return legal URL for shopping.com
	 */
	private URL formURL() {
		System.out.println("Forming URL ...");
		if(keyword != null){
			String url1 = keyword.replace(" ", "-");
			String url2 = keyword.replace(" ", "+");
			URL result = null;
			try {
				result = new URL("http://www.shopping.com/" + url1 
						+ "/products~PG-" + pageNumber + "?KW=" + url2);
				System.out.println("URL is "+result.toString());
			} catch (MalformedURLException e) {
				return result;
			}
			return result;
		} else {
			return null;
		}	
	}	

	/**
	 * printResult will print result for Query 1 or Query 2 based on the pageFlag
	 * 
	 * @param pageFlag is a boolean flag, true means pageNumber is given, false means not given
	 */
	public void printResult(boolean pageFlag){
		if(pageFlag){
			this.getResultObject();
			System.out.println("Product Inforamtion with Keyword <"+keyword + "> on page "+ pageNumber+": ");
			System.out.println(result);
		} else {
			this.getTotalNumberOfresults();
			System.out.println("Total Product Numbers with Keyword <"+ keyword+">: ");
			System.out.println(numberOfResults);
		}
	}
	
	/**
	 * getTotalNumberOfresults will complete
	 * Query 1: Total number of results Given a keyword, such as "digital camera".
	 */
	public void getTotalNumberOfresults() {
		System.out.println("getting total number of results ...");
		String temp = null;
		try {
			Pattern pattern = Pattern
					.compile(NUMBER_OF_RESULT_PATTERN);
			Matcher matcher = pattern.matcher(pageContent);
			if (matcher.find()) {
				temp = matcher.group(3);
				// deal with + sign &#43;
				if (temp.contains("&#43;")) {
					temp = temp.substring(0, temp.indexOf('&')) + "+";
				}
			} else {
				//not find case
				numberOfResults = "0";
			}
			numberOfResults = temp;
		} catch (Exception e) {
			numberOfResults = "0";	
		}
	}

	/**
	 * getResultObject will complete
	 * Query 2: Result Object Given a keyword (e.g. "digital cameras") and page
	 * number (e.g. "1"), return the results in a result object and then print
	 * results on screen. For each result, return the following information:
	 * Title/Product Name (e.g. "Sony T7 Digital Camera") Price of the
	 * product Shipping Price (e.g. "Free Shipping", "$3.50") Vendor (e.g.
	 * "Amazon", "5 stores") For "digital cameras", there should be either 40 or
	 * 80 results that return for page 1.
	 * 
	 * if we search for "thereisnosuchathing", this method will result in null
	 * if we search for a not exist page number, this method will result in null
	 */
	public void getResultObject() {
		System.out.println("getting result object ...");
		Matcher matcher = null;
		try{
			matcher = parser.matchRegex(pageContent, SLICING_PRODUCTS_PATTERN);
		} catch (Exception e) {
			result = null;
		}
		while (matcher.find()) {
			try{
				String innerHTML = matcher.group(1);
				String productName = extractProductName(innerHTML);
				String productPrice = extractproductPrice(innerHTML);
				String productShippingPrice = extractProductShippingPrice(innerHTML);
				String productVendor = extractProductVendor(innerHTML);
				result.add(new Product(productName, productPrice,
						productShippingPrice, productVendor));
			} catch (ParserException e){
				result.add(new Product("Error Product", "", "", ""));
			}
		}
	}

	/**
	 * extractProductName extract product name using PRODUCT_NAME_PATTERN
	 * 
	 * if it fails to do so, it will throws ParserException
	 * 
	 * @param htmlContent
	 * @return product name as String
	 * @throws ParserException
	 */
	private String extractProductName(String htmlContent) throws ParserException{
		int target = 1;
		return parser.extractSingleContentFromHTML(htmlContent, PRODUCT_NAME_PATTERN, target);
	}

	/**
	 * extractproductPrice extract product price using 
	 * PRODUCT_PRICE_PATTERN1, PRODUCT_PRICE_PATTERN2 
	 * 
	 * if it fails to do so, it will throws ParserException
	 * 
	 * @param htmlContent
	 * @return product price as String
	 * @throws ParserException
	 */
	private String extractproductPrice(String htmlContent) throws ParserException{
		// dealing with "cheaper" price case, which is two prices exist, but we should follow the cheaper one
		int target = 1;
		String temp = parser.extractSingleContentFromHTML(htmlContent, PRODUCT_PRICE_PATTERN1, target);
//		if (temp == null) {
//			// dealing with normal case
//			target = 1;
//			temp = parser.extractSingleContentFromHTML(htmlContent, PRODUCT_PRICE_PATTERN2,	target);
//		}
//		if (temp.contains("</a>")) {
//			// dealing with price with link
//			temp = temp.substring(0, temp.indexOf("</a>"));
//		}
		return "$" + temp;
	}

	/**
	 * 
	 * extractProductShippingPrice extract product shipping price using 
	 * PRODUCT_SHIPPING_PRICE_PATTERN1, PRODUCT_SHIPPING_PRICE_PATTERN2, PRODUCT_SHIPPING_PRICE_PATTERN3 
	 * 
	 * if it fails to do so, it will throws ParserException
	 * 
	 * @param htmlContent
	 * @return product shipping price as String
	 * @throws ParserException
	 */
	private String extractProductShippingPrice(String htmlContent) throws ParserException{
		int target = 1;
		String temp = parser.extractSingleContentFromHTML(htmlContent, PRODUCT_SHIPPING_PRICE_PATTERN1, target);
		if (temp != null) {
			// "Free Shipping"
			return temp;
		} else {
			// Not "Free Shipping"
			target = 1;
			temp = parser.extractSingleContentFromHTML(htmlContent, PRODUCT_SHIPPING_PRICE_PATTERN2, target);
			if (temp != null) {
				// No Shipping Info
				return temp;
			} else {
				// Cost
				target = 1;
				temp = parser.extractSingleContentFromHTML(htmlContent, PRODUCT_SHIPPING_PRICE_PATTERN3,
						target);
				if (temp != null) {
					// return the cost description, dealing with $
					if (temp.contains("&#36;")) {
						return temp.replace("&#36;", "$");
					} else {
						return temp;
					}
				} else {
					// Shipping Info Not Available
					return "Shipping Info Not Available";
				}
			}
		}
	}

	/**
	 * 
	 * extractProductVendor extract product vendor using 
	 * PRODUCT_VENDOR_PATTERN1, PRODUCT_VENDOR_PATTERN2, PRODUCT_VENDOR_PATTERN3 
	 * 
	 * if it fails to do so, it will throws ParserException
	 * 
	 * @param htmlContent
	 * @return product vendor as String
	 * @throws ParserException
	 */
	private String extractProductVendor(String htmlContent) throws ParserException {
		int target = 1;
		String temp = parser.extractSingleContentFromHTML(htmlContent, PRODUCT_VENDOR_PATTERN1, target);
		if (temp != null) {
			// vendor is in the format of number
			return temp;
		} else {
			// not a number, not a graph
			target = 1;
			temp = parser.extractSingleContentFromHTML(htmlContent, PRODUCT_VENDOR_PATTERN2, target);
			if (temp != null) {
				return temp;
			}
			else {
					// Vendor Info Not Available
					return "Vendor Info Not Available";
				}
			}
		}
}
