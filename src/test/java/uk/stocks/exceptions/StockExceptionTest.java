/**
 * 
 */
package uk.stocks.exceptions;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.stocks.exceptions.StockException;

/**
 * @author luigiUbezio
 *
 */
public class StockExceptionTest {
	private static final String ERR_MSG = "Error message";
	
	/**
	 * testing the error message
	 */
	@Test
	public void testExceptionMessage() {
		StockException se = new StockException(ERR_MSG);
		assertEquals(ERR_MSG, se.getMessage());
	}
}
