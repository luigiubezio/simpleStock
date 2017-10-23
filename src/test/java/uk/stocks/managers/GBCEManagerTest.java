/**
 * 
 */
package uk.stocks.managers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import uk.stocks.entities.ActionType;
import uk.stocks.entities.Stock;
import uk.stocks.entities.StockSymbol;
import uk.stocks.entities.StockType;
import uk.stocks.entities.Trade;
import uk.stocks.exceptions.ErrorMessages;
import uk.stocks.exceptions.StockException;
import uk.stocks.managers.GBCEManager;

/**
 * @author luigiUbezio
 *
 */
public class GBCEManagerTest {

	/**
	 * testing dividend yeld calculation
	 */
	@Test
	public void testDividendYeld() {
		GBCEManager gbceManager = new GBCEManager();
		assertNotNull(gbceManager);
		// a common stock
		Stock stock = Stock.getValuedInstance(StockSymbol.POP, StockType.COMMON, new BigDecimal("8"), null, new BigDecimal("100"));
		try {
			BigDecimal result = gbceManager.calculateDividendYeld(stock, new BigDecimal("100"));
			// using string contructor to get a precise representation of the number
			// using compareTo to ignore scale
			assertTrue(new BigDecimal("0.08").compareTo(result) == 0);
		} catch (StockException e) {
			e.printStackTrace();
			fail("Exception ' + e.getMessage() + ' is not expected");
		}
		// a preferred stock
		stock = Stock.getValuedInstance(StockSymbol.GIN, StockType.PREFERRED, new BigDecimal("8"), new BigDecimal("0.02"), new BigDecimal("100"));
		try {
			BigDecimal result = gbceManager.calculateDividendYeld(stock, new BigDecimal("100"));
			assertTrue(new BigDecimal("0.02").compareTo(result) == 0);
		} catch (StockException e) {
			e.printStackTrace();
			fail("Exception ' + e.getMessage() + ' is not expected");
		}
		// a not well defined stock
		stock = Stock.getValuedInstance(StockSymbol.JOE, StockType.COMMON, null, null, null);
		try {
			gbceManager.calculateDividendYeld(stock, new BigDecimal("100"));
			fail("An exception should be thrown");
		} catch (StockException e) {
			// the exception is expected to be thrown
		}
	}
	
	/**
	 * testing P/E Ratio
	 */
	@Test
	public void testPERatio() {
		GBCEManager gbceManager = new GBCEManager();
		assertNotNull(gbceManager);
		// a PE ratio that can be calculated
		try {
			BigDecimal result = gbceManager.calculatePERatio(BigDecimal.ZERO, new BigDecimal("0.08"));
			// using the right way to compare a BigDecimal (it does not take into account the scale)
			assertTrue(result.compareTo(BigDecimal.ZERO) == 0);
		} catch (StockException e) {
			e.printStackTrace();
			fail("This exception should not be thrown");
		}
		// another PE ratio that can be calculated
		try {
			BigDecimal result = gbceManager.calculatePERatio(new BigDecimal("100"), new BigDecimal("0.08"));
			assertTrue(new BigDecimal("1250").compareTo(result) == 0);
		} catch (StockException e) {
			e.printStackTrace();
			fail("This exception should not be thrown");
		}
		// a PE ratio with divide by zero error
		try {
			gbceManager.calculatePERatio(new BigDecimal("100"), BigDecimal.ZERO);
			fail("An exception should be thrown");
		} catch (StockException e) {
			// the exception is expected to be thrown
		}	
	}
	
	/**
	 * testing Stock Price
	 */
	@Test
	public void testStockPrice() {
		GBCEManager gbceManager = new GBCEManager();
		assertNotNull(gbceManager);
		// a null list get an exception
		ArrayList<Trade> trades = null;
		try {
			gbceManager.calculateStockPrice(trades);
			fail("An exception should be thrown");
		} catch (StockException e) {
			// expected exception
			assertEquals(ErrorMessages.ERR_05, e.getMessage());
		}
		// an empty List in input get an exception
		trades = new ArrayList<Trade>();
		try {
			gbceManager.calculateStockPrice(trades);
			fail("An exception should be thrown");
		} catch (StockException e) {
			// expected exception
			assertEquals(ErrorMessages.ERR_05, e.getMessage());
		}
		// for this trades is expected a specific result
		trades.add(generateTrade(StockSymbol.ALE, new BigDecimal("10"), new BigDecimal("12.3"), ActionType.BUY));
		trades.add(generateTrade(StockSymbol.ALE, new BigDecimal("20"), new BigDecimal("12.4"), ActionType.BUY));
		trades.add(generateTrade(StockSymbol.ALE, new BigDecimal("15"), new BigDecimal("12.5"), ActionType.BUY));
		trades.add(generateTrade(StockSymbol.ALE, new BigDecimal("100"), new BigDecimal("12.6"), ActionType.BUY));
		trades.add(generateTrade(StockSymbol.ALE, new BigDecimal("1000"), new BigDecimal("12.8"), ActionType.BUY));
		trades.add(generateTrade(StockSymbol.ALE, new BigDecimal("50"), new BigDecimal("11.9"), ActionType.BUY));
		trades.add(generateTrade(StockSymbol.ALE, new BigDecimal("25"), new BigDecimal("11"), ActionType.BUY));
		assertEquals(7, trades.size());
		try {
			// rounded up to six decimal values
			assertEquals(new BigDecimal("12.69549"), gbceManager.calculateStockPrice(trades));
		} catch (StockException e) {
			e.printStackTrace();
			fail("Exception should not be thrown");
		}
		// trades are not homogeneous, i.e. the stock name is different
		trades.add(generateTrade(StockSymbol.GIN, new BigDecimal("25"), new BigDecimal("11"), ActionType.BUY));
		assertEquals(8, trades.size());
		try {
			gbceManager.calculateStockPrice(trades);
			fail("An exception should be thrown");
		} catch (StockException e) {
			// exception expected
			assertEquals(ErrorMessages.ERR_06, e.getMessage());
		}
	}
	
	/**
	 * testing private method checkTradesHomogeneity
	 */
	@Test
	public void testCheckTradesHomogeneity() {
		GBCEManager gbceManager = new GBCEManager();
		assertNotNull(gbceManager);
		
		Class<?>[] parameterTypes = new Class[1];
		parameterTypes[0] = List.class;
		
		Method m = null;
		try {
			m = gbceManager.getClass().getDeclaredMethod("checkTradesHomogeneity", parameterTypes);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			fail("Exception is not expected here");
		}
		m.setAccessible(true);
		Object[] parameters = new Object[1];
		ArrayList<Trade> trades = new ArrayList<Trade>();	
		trades.add(generateTrade(StockSymbol.ALE, new BigDecimal("10"), new BigDecimal("12.3"), ActionType.BUY));
		trades.add(generateTrade(StockSymbol.ALE, new BigDecimal("20"), new BigDecimal("12.4"), ActionType.BUY));
		trades.add(generateTrade(StockSymbol.ALE, new BigDecimal("15"), new BigDecimal("12.5"), ActionType.BUY));
		trades.add(generateTrade(StockSymbol.ALE, new BigDecimal("100"), new BigDecimal("12.6"), ActionType.BUY));
		trades.add(generateTrade(StockSymbol.ALE, new BigDecimal("1000"), new BigDecimal("12.8"), ActionType.BUY));
		trades.add(generateTrade(StockSymbol.ALE, new BigDecimal("50"), new BigDecimal("11.9"), ActionType.BUY));
		trades.add(generateTrade(StockSymbol.ALE, new BigDecimal("25"), new BigDecimal("11"), ActionType.BUY));
		// with this addition trades are not homogeneous, i.e. the stock name is different
		trades.add(generateTrade(StockSymbol.GIN, new BigDecimal("25"), new BigDecimal("11"), ActionType.BUY));
		parameters[0] = trades;
		try {
			Object obj = m.invoke(gbceManager, parameters);
			assertTrue(obj instanceof Boolean);
			assertFalse((Boolean)obj);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * testing all stock index
	 */
	@Test
	public void testAllStockIndex() {
		GBCEManager gbceManager = new GBCEManager();
		assertNotNull(gbceManager);
		// the passed value cannot be null
		try {
			gbceManager.calculateShareIndex(null);
			fail("An exception should be thrown");
		} catch (StockException e) {
			// exception is expected
			assertEquals(ErrorMessages.ERR_05, e.getMessage());
		}
		// the passed value cannot be empty
		List<BigDecimal> prices = new ArrayList<BigDecimal>();
		try {
			gbceManager.calculateShareIndex(prices);
			fail("An exception should be thrown");
		} catch (StockException e) {
			// exception is expected
			assertEquals(ErrorMessages.ERR_05, e.getMessage());
		}
		// prices cannot be negative
		prices.add(new BigDecimal("-1"));
		try {
			gbceManager.calculateShareIndex(prices);
			fail("An exception should be thrown");
		} catch (StockException e) {
			// exception is expected
			assertEquals(ErrorMessages.ERR_07, e.getMessage());
		}
		// prices cannot be zero
		prices = new ArrayList<BigDecimal>();
		prices.add(BigDecimal.ZERO);
		try {
			gbceManager.calculateShareIndex(prices);
			fail("An exception should be thrown");
		} catch (StockException e) {
			// exception is expected
			assertEquals(ErrorMessages.ERR_07, e.getMessage());
		}
		// a well known sample
		prices = new ArrayList<BigDecimal>();
		prices.add(new BigDecimal("10.3"));
		prices.add(new BigDecimal("12.9"));
		prices.add(new BigDecimal("8.5"));
		prices.add(new BigDecimal("3.5"));
		prices.add(new BigDecimal("2.4"));
		try {
			assertEquals(new BigDecimal("6.243456"), gbceManager.calculateShareIndex(prices));
		} catch (StockException e) {
			e.printStackTrace();
			fail("An exception is not expected");
		}
	}	
	
	/**
	 * utility to generate a trade, recording time is added automatically
	 * @param stockSymbol
	 * @param quantity
	 * @param price
	 * @param actionType
	 * @return
	 */
	private Trade generateTrade(StockSymbol stockSymbol, BigDecimal quantity, BigDecimal price, ActionType actionType) {
		Trade trade = new Trade();
		trade.setStockSymbol(stockSymbol);
		trade.setQuantity(quantity);
		trade.setPrice(price);
		trade.setActionType(actionType);
		trade.setTimestamp(Instant.now());
		return trade;
	}
	
}
