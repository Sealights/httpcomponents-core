package custom.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//
/**
 * Implements a bridge from pseudo-commons-logging LogFactory
 * to slf4j LoggerFactory
 * 
 * @author ala schneider   Aug 29, 2018
 *
 */
public class CustomLoggerFactory {
	
	/**
	 * the http logs may be activated, if a system property 'sl.httpDebugLog' is set to 
	 * one of the valid values.
	 * 
	 * The following values may be used for ROOT_LOG initialization	 * 
	 * - Enable logging of all debug messages:
	 * org.apache.http
	 * 
	 * - Enable logging of selected logging context - one of the following values:
	 * org.apache.http.headers
	 * org.apache.http.wire
	 * org.apache.http.impl.conn
	 * org.apache.http.impl.client
	 * org.apache.http.client
	 * 
	 * The debug logs settings is less flexible from the original,
	 * it does not supports several log-contexts, still it may be helpful 
	 * for debugging purposes.
	 * 
	 */
    private static final String APACHE_CLIENT_DEBUG_LOG = "sl.httpDebugLog";      
	
	public static CustomLogger getLogger(Class clazz) {
		return getLogger(clazz.getName());
	}
	
	public static CustomLogger getLogger(String name) {
		return new CustomLogger(LoggerFactory.getLogger(name));
	}
	
	public static boolean isDebugOn(String loggerName) {
		String httpClientRootLog = System.getProperty(APACHE_CLIENT_DEBUG_LOG);
		return (httpClientRootLog != null && loggerName.contains(httpClientRootLog));
	}
}
