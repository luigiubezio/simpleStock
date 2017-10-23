/**
 * 
 */
package uk.stocks.dataReaders;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.Instant;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.stocks.entities.ActionType;
import uk.stocks.entities.GBCEStockList;
import uk.stocks.entities.GBCETradeList;
import uk.stocks.entities.Stock;
import uk.stocks.entities.StockSymbol;
import uk.stocks.entities.StockType;
import uk.stocks.entities.Trade;
import uk.stocks.exceptions.ErrorMessages;
import uk.stocks.exceptions.StockException;
import uk.stocks.managers.GBCEManager;

/**
 * @author luigiUbezio
 * utility to read csv files in input based on apache csv library
 *
 */
public abstract class CSVDataReader {
	/**
	 * log4j initialization
	 */
	private static Logger logger = LogManager.getLogger(CSVDataReader.class);
	
	/**
	 * opening a stock file
	 * @param file
	 * @return
	 * @throws StockException
	 */
	protected Iterable<CSVRecord> openStream(File file, Reader in, Class<? extends Enum<?>> header) throws StockException {
		Iterable<CSVRecord> records = null;
		try {
			if (file != null) {
				in = new FileReader(file);
				records = CSVFormat.RFC4180.withHeader(header).parse(in);
			} else {
				throw new StockException(ErrorMessages.ERR_08);
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(ErrorMessages.ERR_08);
		} 
		return records;
	}
	
	/**
	 * closing a file
	 */
	protected void closeStream(Reader in) {
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				logger.warn(ErrorMessages.WARN_01);
			}
		}
	}
}
