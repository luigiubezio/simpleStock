/**
 * 
 */
package uk.stocks.entities;

import java.util.Collection;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author luigiUbezio
 * GBCE Stock List
 */
public class GBCEStockList {
	/**
	 * log4j initialization
	 */
	private static Logger logger = LogManager.getLogger(GBCEStockList.class);

	
	/**
	 * the list of available stocks
	 */
	private HashMap<StockSymbol, Stock> stocks = new HashMap<StockSymbol, Stock>();
	
	/**
	 * adding a stock to the 'list'
	 * @param stock
	 */
	public void addStock(Stock stock) {
		if (stock != null && !stocks.containsKey(stock.getSymbol())) {
			stocks.put(stock.getSymbol(), stock);
		} else {
			logger.debug("Stock {} cannot be added", stock);
		}
	}
	
	/**
	 * inspecting available stock number
	 * @return
	 */
	public int availableStocks() {
		return stocks.size();
	}
	
	/**
	 * retrieving available stocks
	 * @return
	 */
	public Collection<Stock> getStocks() {
		return stocks.values();
	}
}
