/**
 * 
 */
package uk.stocks.starter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.stocks.dataReaders.TradesDataReader;
import uk.stocks.entities.StockSymbol;
import uk.stocks.exceptions.ErrorMessages;

/**
 * @author luigiUbezio
 *
 */
public class ApplicationProperties {
	/**
	 * log4j initialization
	 */
	private static Logger logger = LogManager.getLogger(TradesDataReader.class); 
	
	/**
	 * application properties filename
	 */
	private static final String APP_PROP_FILE_NAME = "./application.properties";
	
	// properties
	private static final String SEC_LOAD_DELAY = "SEC_LOAD_DELAY";
	private static final String TEA_PRICE = "TEA_PRICE";
	private static final String POP_PRICE = "POP_PRICE";
	private static final String ALE_PRICE = "ALE_PRICE";
	private static final String GIN_PRICE = "GIN_PRICE";
	private static final String JOE_PRICE = "JOE_PRICE";

	/**
	 * application properties instance
	 */
	private static ApplicationProperties _instance = null;
	
	/**
	 * delay (in secs) for loading trades, a default is given
	 */
	private int delay = 10;
	
	// with default
	private BigDecimal teaPrice = BigDecimal.ONE;
	private BigDecimal popPrice = BigDecimal.ONE;
	private BigDecimal alePrice = BigDecimal.ONE;
	private BigDecimal ginPrice = BigDecimal.ONE;
	private BigDecimal joePrice = BigDecimal.ONE;
	
	/**
	 * instance getter
	 * @return
	 */
	public static ApplicationProperties getInstance() {
		if (_instance == null) {
			_instance = new ApplicationProperties();
		}
		return _instance;
	}
	
	/**
	 * private constructor
	 */
	private ApplicationProperties() {
		init();
	}
	
	/**
	 * init method
	 */
	private void init() {
		Properties properties = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(APP_PROP_FILE_NAME);
			// load a properties file
			properties.load(input);
			delay = Integer.parseInt(properties.getProperty(SEC_LOAD_DELAY));
			teaPrice = new BigDecimal(properties.getProperty(TEA_PRICE));
			popPrice = new BigDecimal(properties.getProperty(POP_PRICE));
			alePrice = new BigDecimal(properties.getProperty(ALE_PRICE));
			ginPrice = new BigDecimal(properties.getProperty(GIN_PRICE));
			joePrice = new BigDecimal(properties.getProperty(JOE_PRICE));
		} catch (IOException | IllegalArgumentException e) {
			e.printStackTrace();
			logger.error(ErrorMessages.ERR_09);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					logger.warn(ErrorMessages.WARN_01);
				}
			}
		}
	}
	
	/**
	 * delay in recording trades
	 * @return
	 */
	public int getDelay() {
		return delay;
	}
	
	/**
	 * tea Price
	 * @return
	 */
	public BigDecimal getTeaPrice() {
		return teaPrice;
	}
	
	/**
	 * pop price
	 * @return
	 */
	public BigDecimal getPopPrice() {
		return popPrice;
	}
	
	/**
	 * ale price
	 * @return
	 */
	public BigDecimal getAlePrice() {
		return alePrice;
	}
	
	/**
	 * gin price
	 * @return
	 */
	public BigDecimal getGinPrice() {
		return ginPrice;
	}
	
	/**
	 * joe price
	 * @return
	 */
	public BigDecimal getJoePrice() {
		return joePrice;
	}
	
	/**
	 * getting generic price
	 * @param stockSymbol
	 * @return
	 */
	public BigDecimal getPrice(StockSymbol stockSymbol) {
		switch (stockSymbol) {
		case ALE:
			return alePrice;
		case GIN:
			return ginPrice;
		case JOE:
			return joePrice;
		case POP:
			return popPrice;
		case TEA:
			return teaPrice;
		default:
			logger.warn(ErrorMessages.WARN_09);
			return BigDecimal.ONE;
		} 
	}
}
