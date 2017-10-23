/**
 * 
 */
package uk.stocks.entities;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * @author luigiUbezio
 * class for trades
 */
public class Trade {
	/**
	 * stock symbol
	 */
	private StockSymbol stockSymbol;
	
	/**
	 * timestamp
	 */
	private Instant timestamp;
	
	/**
	 * quantity
	 */
	private BigDecimal quantity;
	
	/**
	 * price
	 */
	private BigDecimal price;
	
	/**
	 * action indicator
	 */
	private ActionType actionType;

	/**
	 * @return the stockSymbol
	 */
	public StockSymbol getStockSymbol() {
		return stockSymbol;
	}

	/**
	 * @param stockSymbol the stockSymbol to set
	 */
	public void setStockSymbol(StockSymbol stockSymbol) {
		this.stockSymbol = stockSymbol;
	}

	/**
	 * @return the timestamp
	 */
	public Instant getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the quantity
	 */
	public BigDecimal getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * @return the actionIndicator
	 */
	public ActionType getActionIndicator() {
		return actionType;
	}

	/**
	 * @param actionType the actionIndicator to set
	 */
	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}
	
	/**
	 * check whether a trade object is correct 
	 * @param trade
	 * @return
	 */
	public static boolean isValid(Trade trade) {
		if (trade == null) {
			return false;
		}
		// everything shall be not null
		// this can be done using the reflection as well
		if (trade.getStockSymbol() != null 
			&& trade.getTimestamp() != null 
			&& trade.getQuantity() != null
			&& trade.getPrice() != null
			&& trade.getActionIndicator() != null
		)  {
			return true;
		} else {
			return false;
		}
	}
}
