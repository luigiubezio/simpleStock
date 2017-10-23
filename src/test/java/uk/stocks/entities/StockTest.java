/**
 * 
 */
package uk.stocks.entities;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

import uk.stocks.entities.Stock;
import uk.stocks.entities.StockSymbol;
import uk.stocks.entities.StockType;

/**
 * @author luigiUbezio
 * Stock test class
 *
 */
public class StockTest {
	@Test
	public void testCreateAStock() {
		Stock teaStock = Stock.getValuedInstance(StockSymbol.TEA, StockType.COMMON, BigDecimal.ZERO, null, new BigDecimal("100"));
		assertEquals(teaStock.getSymbol(), StockSymbol.TEA);
		assertEquals(teaStock.getType(), StockType.COMMON);
		assertEquals(teaStock.getLastDividend(), BigDecimal.ZERO);
		assertEquals(teaStock.getFixedDividend(), null);
		assertEquals(teaStock.getParValue(), new BigDecimal("100"));
	}
	
	@Test
	public void testValidCommon() {
		Stock teaStock = Stock.getValuedInstance(StockSymbol.TEA, StockType.COMMON, BigDecimal.ZERO, null, new BigDecimal("100"));
		assertNotNull(teaStock);
		assertTrue(Stock.isValid(teaStock));
	}
	
	/**
	 * a preferred stock shall have 
	 */
	@Test
	public void testValidPreferred() {
		Stock ginStock = Stock.getValuedInstance(StockSymbol.GIN, StockType.COMMON, new BigDecimal("8"), new BigDecimal("0.02"), new BigDecimal("100"));
		assertNotNull(ginStock);
		assertTrue(Stock.isValid(ginStock));
	}
	
	/**
	 * a null stock is not valid
	 */
	@Test
	public void testNullStock() {
		Stock nullStock = null;
		assertNull(nullStock);
		assertFalse(Stock.isValid(nullStock));
	}
	
	/**
	 * a stock with null stock type in not valid
	 */
	@Test
	public void testNullStockType() {
		Stock stock = Stock.getValuedInstance(StockSymbol.GIN, null, new BigDecimal("8"), new BigDecimal("0.02"), new BigDecimal("100"));
		assertNotNull(stock);
		assertFalse(Stock.isValid(stock));
	}
	
	/**
	 * a common stock cannot have a null Last Dividend
	 */
	@Test
	public void testNullStockLastDividend() {
		Stock stock = Stock.getValuedInstance(StockSymbol.GIN, StockType.COMMON, null, new BigDecimal("0.02"), new BigDecimal("100"));
		assertNotNull(stock);
		assertFalse(Stock.isValid(stock));		
	}
	
	/**
	 * a preferred stock cannot have a null fixed dividend
	 */
	@Test
	public void testNullStockFixedDividend() {
		Stock stock = Stock.getValuedInstance(StockSymbol.GIN, StockType.PREFERRED, new BigDecimal("12"), null, new BigDecimal("100"));
		assertNotNull(stock);
		assertFalse(Stock.isValid(stock));		
	}
	
	/**
	 * a preferred stock cannot have a null par value
	 */
	@Test
	public void testNullStockFixedDividend2() {
		Stock stock = Stock.getValuedInstance(StockSymbol.GIN, StockType.PREFERRED, new BigDecimal("12"), new BigDecimal("100"), null);
		assertNotNull(stock);
		assertFalse(Stock.isValid(stock));		
	}

}
