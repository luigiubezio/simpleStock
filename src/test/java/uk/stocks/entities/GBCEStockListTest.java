/**
 * 
 */
package uk.stocks.entities;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

import uk.stocks.entities.GBCEStockList;
import uk.stocks.entities.Stock;
import uk.stocks.entities.StockSymbol;
import uk.stocks.entities.StockType;

/**
 * @author luigiUbezio
 *
 */
public class GBCEStockListTest {
	@Test
	public void testAddingAStock() {
		Stock teaStock = Stock.getValuedInstance(StockSymbol.TEA, StockType.COMMON, BigDecimal.ZERO, null, new BigDecimal("100"));
		assertNotNull(teaStock);
		GBCEStockList gbcStockList = new GBCEStockList();
		assertEquals(0, gbcStockList.availableStocks());
		gbcStockList.addStock(teaStock);
		assertEquals(1, gbcStockList.availableStocks());
	}
}
