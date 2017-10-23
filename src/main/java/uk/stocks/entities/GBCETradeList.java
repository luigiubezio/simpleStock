/**
 * 
 */
package uk.stocks.entities;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.stocks.exceptions.ErrorMessages;
import uk.stocks.exceptions.StockException;

/**
 * @author luigiUbezio
 * List that contains the recorded trades
 */
public class GBCETradeList {
	/**
	 * log4j initialization
	 */
	private static Logger logger = LogManager.getLogger(GBCETradeList.class);
	
	/**
	 * constant for in scope check
	 */
	private final static long IN_SCOPE_PERIOD = 15;
	
	/**
	 * duration of 15 minutes
	 */
	private Duration duration = Duration.ofMinutes(IN_SCOPE_PERIOD);
	
	/**
	 * the list
	 */
	private ArrayList<Trade> trades = new ArrayList<Trade>();

	/**
	 * adding a trade to the list
	 * @param trade
	 * @throws StockException
	 */
	public void addTrade(Trade trade) throws StockException {
		if (Trade.isValid(trade)) {
			trades.add(trade);
		} else {
			logger.error(ErrorMessages.ERR_04);
			throw new StockException(ErrorMessages.ERR_04);
		}
	}
	
	/**
	 * inspecting size of the list
	 * @return
	 */
	public int availableTrades() {
		return trades.size();
	}
	
	/**
	 * trades are in scope if recorded in the past 15 minutes
	 * @return
	 */
	public List<Trade> getTradesInScope() {
		Instant now = Instant.now();
		return trades.stream().filter(p -> isInScope(p.getTimestamp(), now)).collect(Collectors.toList());
	}
	
	/**
	 * trades are in scope if recorded in the past 15min
	 * for the same stock symbol (other not included)
	 * @param stockSymbol
	 * @return
	 */
	public List<Trade> getTradesInScope(StockSymbol stockSymbol) {
		Instant now = Instant.now();
		return trades.stream().filter(p -> isInScope(p.getTimestamp(), now)).filter(p -> p.getStockSymbol().equals(stockSymbol)).collect(Collectors.toList());
	}

	/**
	 * 
	 * @param instant
	 * @param now
	 * @return if the first instant is not older than 15 minutes
	 */
	private boolean isInScope(Instant instant, Instant now) {
		// we are sure that instant (and now ) are not null
		return Duration.between(instant, now).compareTo(duration) <= 0;	
	}
}
