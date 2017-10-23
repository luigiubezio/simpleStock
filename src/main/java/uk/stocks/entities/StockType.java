/**
 * 
 */
package uk.stocks.entities;

/**
 * @author luigiUbezio
 * Stock Type enumeration
 */
public enum StockType {
	COMMON ("Common", 1),
	PREFERRED ("Preferred", 2);
	
	private final String key;
	private final Integer value;
	
	StockType(String key, Integer value) {
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}
	
	public Integer getValue() {
		return value;
	}
}
