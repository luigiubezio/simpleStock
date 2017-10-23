package uk.stocks.entities;

import java.math.BigDecimal;

/**
 * 
 * @author luigiUbezio
 * the Stock entity
 *
 */
public class Stock {
	private StockSymbol symbol;
	private StockType type;
	private BigDecimal lastDividend;
	private BigDecimal fixedDividend;
	private BigDecimal parValue;
	
	/**
	 * utility to create a stock
	 * @param symbol
	 * @param type
	 * @param lastDividend
	 * @param fixedDividend
	 * @param parValue
	 */
	public static Stock getValuedInstance(StockSymbol symbol, StockType type, BigDecimal lastDividend,
			BigDecimal fixedDividend, BigDecimal parValue) {
		Stock stock = new Stock();
		stock.setSymbol(symbol);
		stock.setType(type);
		stock.setLastDividend(lastDividend);
		stock.setFixedDividend(fixedDividend);
		stock.setParValue(parValue);
		return stock;
	}
	
	/**
	 * @return the symbol
	 */
	public StockSymbol getSymbol() {
		return symbol;
	}
	/**
	 * @param symbol the symbol to set
	 */
	public void setSymbol(StockSymbol symbol) {
		this.symbol = symbol;
	}
	/**
	 * @return the type
	 */
	public StockType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(StockType type) {
		this.type = type;
	}
	/**
	 * @return the lastDividend
	 */
	public BigDecimal getLastDividend() {
		return lastDividend;
	}
	/**
	 * @param lastDividend the lastDividend to set
	 */
	public void setLastDividend(BigDecimal lastDividend) {
		this.lastDividend = lastDividend;
	}
	/**
	 * @return the fixedDividend
	 */
	public BigDecimal getFixedDividend() {
		return fixedDividend;
	}
	/**
	 * @param fixedDividend the fixedDividend to set
	 */
	public void setFixedDividend(BigDecimal fixedDividend) {
		this.fixedDividend = fixedDividend;
	}
	/**
	 * @return the parValue
	 */
	public BigDecimal getParValue() {
		return parValue;
	}
	/**
	 * @param parValue the parValue to set
	 */
	public void setParValue(BigDecimal parValue) {
		this.parValue = parValue;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Symbol: ");
		sb.append(symbol);
		sb.append("\r\n");
		sb.append("Type: ");
		sb.append(type);
		sb.append("\r\n");
		sb.append("Fixed Dividend: ");
		sb.append(fixedDividend);
		sb.append("\r\n");
		sb.append("Last Dividend: ");
		sb.append(lastDividend);
		sb.append("\r\n");
		sb.append("Par Value: ");
		sb.append(parValue);
		sb.append("\r\n");
		return sb.toString();
	}
	
	/**
	 * validation of a stock instance
	 * a not null stock is valid when
	 * (type is common AND last dividend is not null) OR
	 * (type is preferred AND fixed dividend is not null)
	 */
	public static boolean isValid(Stock stock) {
		if (stock == null) {
			return false;
		}
		if (stock.type == null) {
			return false;
		}
		if (StockType.COMMON.equals(stock.type) && stock.lastDividend != null) {
			return true;
		}
		if (StockType.PREFERRED.equals(stock.type) && stock.fixedDividend != null && stock.parValue != null) {
			return true;
		}
		return false;
	}
}
