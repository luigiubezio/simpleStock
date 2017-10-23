/**
 * 
 */
package uk.stocks.dataReaders;

import java.io.File;
import java.io.Reader;
import java.math.BigDecimal;

import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.stocks.entities.GBCEStockList;
import uk.stocks.entities.Stock;
import uk.stocks.entities.StockSymbol;
import uk.stocks.entities.StockType;
import uk.stocks.exceptions.ErrorMessages;
import uk.stocks.exceptions.StockException;

/**
 * @author luigiUbezio
 *
 */
public class StockPricesDataReader extends CSVDataReader {	
	/**
	 * log4j initialization
	 */
	private static Logger logger = LogManager.getLogger(StockPricesDataReader.class);
	
	/**
	 * enum for prices csv
	 * @author luigiUbezio
	 *
	 */
	public enum PricesHeaders {
		symbol,
		type,
		lastDividend,
		fixedDividend,
		parValue
	}
	
	public GBCEStockList read(File file) throws StockException {
		GBCEStockList gbceStockList = new GBCEStockList();
		Reader in = null;
		Iterable<CSVRecord> records = openStream(file, in, PricesHeaders.class);
		for (CSVRecord record : records) {
			Stock stock = readStock(record);
			if (Stock.isValid(stock)) {
				gbceStockList.addStock(stock);
			} else {
				logger.warn(ErrorMessages.WARN_02);
			}
		}
		// closing stream
		closeStream(in);
		return gbceStockList;
	}
	
	/**
	 * reading a stock value
	 * @param record
	 * @return
	 */
	private Stock readStock(CSVRecord record) {
		Stock stock = new Stock();
		String symbol = record.get(PricesHeaders.symbol);
		try {
			stock.setSymbol(StockSymbol.valueOf(symbol));
		} catch (IllegalArgumentException e) {
			logger.warn(ErrorMessages.WARN_03, symbol);
		}
		String type = record.get(PricesHeaders.type);
		try {
			stock.setType(StockType.valueOf(type.toUpperCase()));
		} catch (IllegalArgumentException e) {
			logger.warn(ErrorMessages.WARN_04, type);
		}	
		String lastDividend = record.get(PricesHeaders.lastDividend);
		try {
			stock.setLastDividend(new BigDecimal(lastDividend));
		} catch (IllegalArgumentException e) {
			logger.warn(ErrorMessages.WARN_05, lastDividend);
		}
		String fixedDividend = record.get(PricesHeaders.fixedDividend);
		try {
			stock.setFixedDividend(new BigDecimal(fixedDividend));
		} catch (IllegalArgumentException e) {
			logger.warn(ErrorMessages.WARN_05, fixedDividend);
		}	
		String parValue = record.get(PricesHeaders.parValue);
		try {
			stock.setParValue(new BigDecimal(parValue));
		} catch (IllegalArgumentException e) {
			logger.warn(ErrorMessages.WARN_05, parValue);
		}	
		return stock;
	}

}
