/**
 * 
 */
package uk.stocks.starter;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.stocks.dataReaders.StockPricesDataReader;
import uk.stocks.dataReaders.TradesDataReader;
import uk.stocks.entities.GBCEStockList;
import uk.stocks.entities.GBCETradeList;
import uk.stocks.entities.Stock;
import uk.stocks.entities.StockSymbol;
import uk.stocks.entities.Trade;
import uk.stocks.exceptions.ErrorMessages;
import uk.stocks.exceptions.StockException;
import uk.stocks.managers.GBCEManager;

/**
 * @author luigiUbezio
 *
 */
public class Starter {
	/**
	 * log4j initialization
	 */
	private static Logger logger = LogManager.getLogger(Starter.class);
	
	/**
	 * stock list
	 */
	private GBCEStockList gbceStockList;
	
	/**
	 * trade list
	 */
	private GBCETradeList gbceTradeList;
	
	/**
	 * gbce manager
	 */
	private GBCEManager gbceManager = new GBCEManager();
	
	private List<BigDecimal> prices = new ArrayList<BigDecimal>();
	
	/**
	 * main method
	 * @param args
	 */
	public static void main(String[] args) {
		logger.info("START");
		Starter starter = new Starter();
		System.out.println("**************************");
		starter.calcDividendYeld();
		starter.calcPERatio();
		starter.calcStockPrice();
		starter.calcGBCEIndex();
		System.out.println("**************************");
		logger.info("END");
	}
	
	/**
	 * default constructor
	 */
	public Starter() {
		init();
	}
	
	/**
	 * init method
	 * loading input files
	 */
	private void init() {
		StockPricesDataReader stockPricesDataReader = new StockPricesDataReader();
		// loading stock prices
		File file = new File("./in/stockPrices.csv");
		try {
			gbceStockList = stockPricesDataReader.read(file);
		} catch (StockException e) {
			e.printStackTrace();
			logger.error("Stock Input file not found");
			logger.error("The program will terminate ...");
			System.exit(-1);
		}
		// loading trades
		file = new File("./in/test/trades.csv");
		TradesDataReader tradesDataReader = new TradesDataReader();
		try {
			gbceTradeList = tradesDataReader.read(file);
		} catch (StockException e) {
			e.printStackTrace();
			logger.error("Trades Input file not found");
			logger.error("The program will terminate ...");
			System.exit(-1);
		}
	}
	
	/**
	 * dividend yeld for all stocks
	 */
	public void calcDividendYeld() {
		for (Stock stock : gbceStockList.getStocks()) {
			try {
				BigDecimal divYeld = gbceManager.calculateDividendYeld(stock, ApplicationProperties.getInstance().getPrice(stock.getSymbol()));
				System.out.println("The dividend yeld for " + stock.getSymbol() +" is " + divYeld );
			} catch (StockException e) {
				System.out.println("The dividend yeld for " + stock.getSymbol() +" cannot be evaluated");
			}
		}
	}
	
	/**
	 * P/E ratio for all Stocks
	 */
	public void calcPERatio() {
		for (Stock stock : gbceStockList.getStocks()) {
			try {
				BigDecimal peRatio = gbceManager.calculatePERatio(ApplicationProperties.getInstance().getPrice(stock.getSymbol()), stock.getLastDividend());
				System.out.println("The P/E Ratio for " + stock.getSymbol() +" is " + peRatio );
			} catch (StockException e) {
				System.out.println("The P/E Ratio for " + stock.getSymbol() +"  cannot be evaluated");
			}
		}
	}
	
	/**
	 * Stock Price for all Stocks in the list (if a ticker price)
	 */
	public void calcStockPrice() {
		for (StockSymbol stockSymbol : StockSymbol.values()) {
			List<Trade> trades = gbceTradeList.getTradesInScope(stockSymbol);
			try {
				BigDecimal price = gbceManager.calculateStockPrice(trades);
				prices.add(price);
				System.out.println("The Stock price for " + stockSymbol +" is " + price);
			} catch (StockException e) {
				System.out.println("The Stock price for " + stockSymbol +" cannot be evaluated");
			}
		}
	}
	
	/**
	 * GBCEIndex based on available Stocks
	 */
	public void calcGBCEIndex() {
		try {
			BigDecimal gbceIndex = gbceManager.calculateShareIndex(prices);
			System.out.println("The GBCE Index is " + gbceIndex);
		} catch (StockException e) {
			System.out.println("The GBCE Index cannot be evaluated");
		}
	}
}
