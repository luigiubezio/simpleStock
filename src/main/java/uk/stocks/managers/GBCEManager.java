/**
 * 
 */
package uk.stocks.managers;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.stocks.entities.Stock;
import uk.stocks.entities.StockSymbol;
import uk.stocks.entities.StockType;
import uk.stocks.entities.Trade;
import uk.stocks.exceptions.ErrorMessages;
import uk.stocks.exceptions.StockException;

/**
 * @author luigiUbezio
 * a simple GBCE Manager class to performs all business logic actions
 */
public class GBCEManager {
	/**
	 * log4j initialization
	 */
	private static Logger logger = LogManager.getLogger(GBCEManager.class);
	
	/**
	 * MathContext
	 */
	private final MathContext MATH_CONTEXT = MathContext.DECIMAL32;
	
	
	/**
	 * dividend yeld calculator
	 * @param stock
	 * @return
	 */
	public BigDecimal calculateDividendYeld(Stock stock, BigDecimal tickerPrice) throws StockException {
		if (Stock.isValid(stock) && tickerPrice != null) {
			if (StockType.COMMON.equals(stock.getType())) {
				return getCommonDY(tickerPrice, stock.getLastDividend());
			} else if (StockType.PREFERRED.equals(stock.getType()))  {
				return getPreferredDY(tickerPrice, stock.getFixedDividend(), stock.getParValue());
			} else {
				logger.error(ErrorMessages.ERR_01);
				throw new StockException(ErrorMessages.ERR_01);
			}
		} else {
			logger.error(ErrorMessages.ERR_02);
			throw new StockException(ErrorMessages.ERR_02);
		}		
	}
	
	/**
	 * calculating the dividend yeld for common type stocks
	 * @param tickerPrice
	 * @param lastDividend
	 * @return
	 */
	private BigDecimal getCommonDY(BigDecimal tickerPrice, BigDecimal lastDividend) {
		return lastDividend.divide(tickerPrice, MATH_CONTEXT);
	}
	

	/**
	 * calculating the dividend yeld for preferred type stocks
	 * @param tickerPrice
	 * @param fixedDividend
	 * @param parValue
	 * @return
	 */
	private BigDecimal getPreferredDY(BigDecimal tickerPrice, BigDecimal fixedDividend, BigDecimal parValue) {
		return fixedDividend.multiply(parValue).divide(tickerPrice, MATH_CONTEXT);
	}
	
	/**
	 * calculates the P/E Ratio
	 * @param tickerPrice
	 * @param dividend
	 * @return
	 */
	public BigDecimal calculatePERatio(BigDecimal tickerPrice, BigDecimal dividend) throws StockException {
		if (tickerPrice != null && dividend != null && !dividend.equals(BigDecimal.ZERO)) {
			return tickerPrice.divide(dividend, MATH_CONTEXT);
		} else {
			throw new StockException(ErrorMessages.ERR_03);
		}
	}
	
	/**
	 * this method calculate the stock price only checking whether the passed trades are valid
	 * stocks symbols are checked to be homogeneous
	 * time constraint, if any, should be guaranteed by the caller 
	 * @param trades
	 * @return
	 * @throws StockException launched when trades is null or empty and when stocks symbols are not homogeneous
	 */
	public BigDecimal calculateStockPrice(List<Trade> trades) throws StockException {
		BigDecimal sumOfQuantities = BigDecimal.ZERO;
		BigDecimal sumOfProducts = BigDecimal.ZERO;	
		if (trades != null && trades.size() > 0) {
			if (checkTradesHomogeneity(trades)) {
				for (Trade trade : trades) {
					if (Trade.isValid(trade)) {
						sumOfQuantities = sumOfQuantities.add(trade.getQuantity());
						sumOfProducts = sumOfProducts.add(trade.getPrice().multiply(trade.getQuantity()));
					} else {
						logger.error(ErrorMessages.ERR_04);
						throw new StockException(ErrorMessages.ERR_04);		
					}
				}
				return sumOfProducts.divide(sumOfQuantities, MATH_CONTEXT);
			} else {
				throw new StockException(ErrorMessages.ERR_06);
			}
		} else {
			throw new StockException(ErrorMessages.ERR_05);	
		}
	}
	
	
	/**
	 * 
	 * @param prices
	 * @return index related to list of shares in input
	 * @throws StockException: throw when list of prices is null or empty
	 */
	public BigDecimal calculateShareIndex(List<BigDecimal> prices) throws StockException {
		if (prices == null || prices.size() == 0) {
			throw new StockException(ErrorMessages.ERR_05);
		} else {
			BigDecimal product = new BigDecimal("1");
			for (BigDecimal price : prices) {
				if (price != null && price.signum() > 0) {
				product = product.multiply(price);
				} else {
					throw new StockException(ErrorMessages.ERR_07);
				}
			}
			BigDecimal power = BigDecimal.ONE.divide(new BigDecimal(prices.size()), MATH_CONTEXT);
			return new BigDecimal(Math.pow(product.doubleValue(), power.doubleValue()), MATH_CONTEXT);
		}
	}
	
	/**
	 * checking the List homogeneity
	 * @param trades
	 * @throws StockException
	 */
	private boolean checkTradesHomogeneity(List<Trade> trades) throws StockException {
		// target symbol used to check the homogeneity
		List<StockSymbol> stockSymbolList = trades.stream().map(s -> s.getStockSymbol()).distinct().collect(Collectors.toList());
		if (stockSymbolList.size() > 1) {
			return false;
		} else {
			return true;
		}
	}
}
