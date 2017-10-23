/**
 * 
 */
package uk.stocks.entities;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.Test;

import uk.stocks.entities.ActionType;
import uk.stocks.entities.StockSymbol;
import uk.stocks.entities.Trade;

/**
 * @author luigiUbezio
 *
 */
public class TradeTest {

	@Test
	public void testValidity() {
		// a null trade should be not valid
		Trade trade = null;
		assertFalse(Trade.isValid(trade));
		// every members are not null -> the object is valid
		trade = new Trade();
		// a trade wit null members is invalid
		assertFalse(Trade.isValid(trade));
		// setting fields
		trade.setTimestamp(Instant.now());
		trade.setPrice(new BigDecimal("100"));
		trade.setQuantity(new BigDecimal("100"));
		trade.setStockSymbol(StockSymbol.GIN);		
		trade.setActionType(ActionType.BUY);
		assertTrue(Trade.isValid(trade));
		// a trade with a null member is invalid
		trade.setTimestamp(null);
		assertFalse(Trade.isValid(trade));
	}
}
