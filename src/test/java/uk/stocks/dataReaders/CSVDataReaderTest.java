/**
 * 
 */
package uk.stocks.dataReaders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Test;

import uk.stocks.entities.GBCEStockList;
import uk.stocks.entities.GBCETradeList;
import uk.stocks.exceptions.StockException;

/**
 * @author luigiUbezio
 *
 */
public class CSVDataReaderTest {

	/**
	 * reading from stock csv
	 */
	@Test
	public void testLoadingStockCSV() {
		StockPricesDataReader stockPricesDataReader = new StockPricesDataReader();
		assertNotNull(stockPricesDataReader);
		File file = new File("./in/test/stockPrices.csv");
		try {
			GBCEStockList gbceStockList = stockPricesDataReader.read(file);
			assertEquals(5, gbceStockList.availableStocks());
		} catch (StockException e) {
			e.printStackTrace();
			fail("Exception is not expected");
		}
	}
	
	/**
	 * reading from trades csv
	 */
	@Test
	public void testLoadingTradesCSV() {
		TradesDataReader tradesDataReader = new TradesDataReader();
		assertNotNull(tradesDataReader);
		File file = new File("./in/test/trades.csv");
		try {
			GBCETradeList gbceTradeList = tradesDataReader.read(file);
			assertEquals(5, gbceTradeList.availableTrades());
		} catch (StockException e) {
			e.printStackTrace();
			fail("Exception is not expected");
		}
	}
}
