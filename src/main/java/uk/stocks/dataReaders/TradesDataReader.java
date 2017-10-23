/**
 * 
 */
package uk.stocks.dataReaders;

import java.io.File;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.Instant;

import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.stocks.entities.ActionType;
import uk.stocks.entities.GBCETradeList;
import uk.stocks.entities.StockSymbol;
import uk.stocks.entities.Trade;
import uk.stocks.exceptions.ErrorMessages;
import uk.stocks.exceptions.StockException;
import uk.stocks.starter.ApplicationProperties;

/**
 * @author luigiUbezio
 *
 */
public class TradesDataReader extends CSVDataReader {
	/**
	 * log4j initialization
	 */
	private static Logger logger = LogManager.getLogger(TradesDataReader.class); 
	
	/**
	 * enum for trades csv
	 * @author luigiUbezio
	 *
	 */
	public enum TradesHeaders {
		symbol,
		quantity,
		price,
		action
	}
	

	/**
	 * reading trade files
	 * @param file
	 * @return
	 * @throws StockException
	 */
	public GBCETradeList read(File file)  throws StockException {
		GBCETradeList gbceTradeList = new GBCETradeList();
		Reader in = null;
		Iterable<CSVRecord> records = openStream(file, in, TradesHeaders.class);
		for (CSVRecord record : records) {
			Trade trade = readTrade(record);
			// assigning timestamp
			trade.setTimestamp(Instant.now());
			if (Trade.isValid(trade)) {
				gbceTradeList.addTrade(trade);
				try {
					Thread.sleep(ApplicationProperties.getInstance().getDelay() * 1000);
				} catch (InterruptedException e) {
					logger.warn(ErrorMessages.WARN_08);
				}
			} else {
				logger.warn(ErrorMessages.WARN_06, trade);
			}
		}
		// closing stream
		closeStream(in);
		return gbceTradeList;
	}
	
	/**
	 * reading a trade
	 * @param record
	 * @return
	 */
	private Trade readTrade(CSVRecord record) {
		Trade trade = new Trade();
		String symbol = record.get(TradesHeaders.symbol);
		try {
			trade.setStockSymbol(StockSymbol.valueOf(symbol));
		} catch (IllegalArgumentException e) {
			logger.warn(ErrorMessages.WARN_03, symbol);
		}
		String quantity = record.get(TradesHeaders.quantity);
		try {
			trade.setQuantity(new BigDecimal(quantity));
		} catch (IllegalArgumentException e) {
			logger.warn(ErrorMessages.WARN_05, quantity);
		}
		String price = record.get(TradesHeaders.price);
		try {
			trade.setPrice(new BigDecimal(price));
		} catch (IllegalArgumentException e) {
			logger.warn(ErrorMessages.WARN_05, price);
		}
		String action = record.get(TradesHeaders.action);
		try {
			trade.setActionType(ActionType.valueOf(action));
		} catch (IllegalArgumentException e) {
			logger.warn(ErrorMessages.WARN_07, action);
		}
		return trade;
	}


}
