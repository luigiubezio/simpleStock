/**
 * 
 */
package uk.stocks.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.junit.Test;

import uk.stocks.entities.ActionType;
import uk.stocks.entities.GBCETradeList;
import uk.stocks.entities.StockSymbol;
import uk.stocks.entities.Trade;
import uk.stocks.exceptions.StockException;

/**
 * @author luigiUbezio
 *
 */
public class GBCETradeListTest {

	/**
	 * adding a trade
	 */
	@Test
	public void testAddingATrade() {
		GBCETradeList gbceTradeList = new GBCETradeList();
		assertNotNull(gbceTradeList);
		assertEquals(0, gbceTradeList.availableTrades());
		Trade trade = generateSampleTrade();
		try {
			gbceTradeList.addTrade(trade);
		} catch (StockException e) {
			e.printStackTrace();
			fail("This exception should not be thrown");
		}
		assertEquals(1, gbceTradeList.availableTrades());
	}
	
	/**
	 * 'massive' trade insertion
	 */
	@Test
	public void testMassiveTradeInsertion() {
		GBCETradeList gbceTradeList = new GBCETradeList();
		assertNotNull(gbceTradeList);
		assertEquals(0, gbceTradeList.availableTrades());
		Trade trade = generateSampleTrade();
		for (int i = 0; i < 100; i++) {
			try {
				gbceTradeList.addTrade(trade);
			} catch (StockException e) {
				e.printStackTrace();
				fail("Erron Insertion: iteration " + i);
			}
		}
		assertEquals(100, gbceTradeList.availableTrades());
	}
	
	/**
	 * testing trades in scope
	 */
	@Test
	public void testTradesInScope() {
		GBCETradeList gbceTradeList = new GBCETradeList();
		assertNotNull(gbceTradeList);
		// if there are no trades, the list of trades in scope is empty
		assertEquals(0, gbceTradeList.availableTrades());
		List<Trade> trades = gbceTradeList.getTradesInScope();
		assertEquals(0, trades.size());
		// if the list contains trades added now, they are all in scope
		for (int i = 0; i < 100; i++) {
			try {
				gbceTradeList.addTrade(generateSampleTrade());
			} catch (StockException e) {
				e.printStackTrace();
				fail("Erron Insertion: iteration " + i);
			}
		}
		trades = gbceTradeList.getTradesInScope();
		assertEquals(100, gbceTradeList.availableTrades());
		assertEquals(100, trades.size());
		// if trades are added with an old (older than 15 minutes) timestamp
		// they are all out of scope
		gbceTradeList = new GBCETradeList();
		for (int i = 0; i < 100; i++) {
			try {
				Trade trade = generateSampleTrade();
				// subtracting 15 minutes = 900 seconds
				trade.setTimestamp(trade.getTimestamp().minusSeconds(901L));
				gbceTradeList.addTrade(trade);
			} catch (StockException e) {
				e.printStackTrace();
				fail("Erron Insertion: iteration " + i);
			}
		}
		trades = gbceTradeList.getTradesInScope();
		assertEquals(100, gbceTradeList.availableTrades());
		assertEquals(0, trades.size());
	}
	
	/**
	 * testing trades in scope, given a StockSymbol
	 */
	@Test
	public void testTradesPerStockInScope() {
		GBCETradeList gbceTradeList = new GBCETradeList();
		assertNotNull(gbceTradeList);
		// if there are no trades of a given stock symbol, the list of trades in scope is empty
		Trade trade = generateSampleTrade();
		trade.setStockSymbol(StockSymbol.GIN);
		try {
			gbceTradeList.addTrade(trade);
		} catch (StockException e) {
			e.printStackTrace();
			fail("Unexpectd Insertion Error");
		}
		assertEquals(1, gbceTradeList.availableTrades());
		assertEquals(1, gbceTradeList.availableTrades());
		assertEquals(0, gbceTradeList.getTradesInScope(StockSymbol.ALE).size());
		// trades half and half
		gbceTradeList = new GBCETradeList();
		assertNotNull(gbceTradeList);
		// GIN insertion
		trade = generateSampleTrade();
		trade.setStockSymbol(StockSymbol.GIN);
		try {
			for (int i = 0; i < 50; i++) {
				gbceTradeList.addTrade(trade);
			}
		} catch (StockException e) {
			e.printStackTrace();
			fail("Unexpectd Insertion Error on iteration");
		}
		assertEquals(50, gbceTradeList.availableTrades());
		// ALE insertion
		trade = generateSampleTrade();
		trade.setStockSymbol(StockSymbol.ALE);
		try {
			for (int i = 0; i < 50; i++) {
				gbceTradeList.addTrade(trade);
			}
		} catch (StockException e) {
			e.printStackTrace();
			fail("Unexpectd Insertion Error on iteration");
		}
		assertEquals(100, gbceTradeList.availableTrades());
		assertEquals(100, gbceTradeList.getTradesInScope().size());
		assertEquals(50, gbceTradeList.getTradesInScope(StockSymbol.ALE).size());
		assertEquals(50, gbceTradeList.getTradesInScope(StockSymbol.GIN).size());
	}
	
	/**
	 * utility method to generate a trade
	 * @return
	 */
	private Trade generateSampleTrade() {
		Trade trade = new Trade();
		trade.setTimestamp(Instant.now());
		trade.setPrice(new BigDecimal("100"));
		trade.setStockSymbol(StockSymbol.ALE);
		trade.setQuantity(new BigDecimal("100"));
		trade.setActionType(ActionType.BUY);
		return trade;
	}
}
