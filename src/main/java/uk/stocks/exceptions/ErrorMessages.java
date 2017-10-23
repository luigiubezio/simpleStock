/**
 * 
 */
package uk.stocks.exceptions;

/**
 * @author luigiUbezio
 * utility class to hold all error messages
 * it can be on a properties file as well
 */
public class ErrorMessages {
	// errors
	public static final String ERR_01 = "The stock type is not managed";
	public static final String ERR_02 ="The dividend yeld cannot be calculated";
	public static final String ERR_03 = "The P/E Ratio cannot be calculated";
	public static final String ERR_04 =  "The trade instance is not valid";
	public static final String ERR_05 =  "The trade list cannot be null or empty";
	public static final String ERR_06 =  "The trade list is not homogeneous: i.e. all stocks shall have the same symbol";
	public static final String ERR_07 =  "The stock prices shall be defined and positive";
	public static final String ERR_08 = "CSV input file not properly defined";
	public static final String ERR_09 = "It is not possible to read configuration file, default values are applied";
	
	
	// warnings
	public static final String WARN_01 = "Error in closing input stream";
	public static final String WARN_02 = "The stock is not valid";	
	public static final String WARN_03 = "'{}' is not a valid Stock Symbol";
	public static final String WARN_04 = "'{}' is not a valid Stock Type";
	public static final String WARN_05 = "'{}' is not a valid Decimal";	
	public static final String WARN_06 = "The trade is not valid {}";
	public static final String WARN_07 = "'{}' is not a valid Action Type";
	public static final String WARN_08 = "Interrupted Exception on sleep";
	public static final String WARN_09 = "Returning default price";
}	
