package org.apache.commons.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements a bridge from pseudo-commons-logging LogFactory
 * to slf4j LoggerFactory
 * 
 * @author ala schneider   Aug 29, 2018
 *
 */
public class LogFactory {
	
	/**
	 * the http logs may be activated, if a concrete slf4j logger is assigned to the root logger.
	 * 
	 * The debug logs settings is less flexible from the original,
	 * it does not supports several log-contexts, still it may be helpful 
	 * for debugging purposes.
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
	 */
	private static Logger ROOT_LOG;
	
	public static void setRoot(Logger rootLogger) {
		ROOT_LOG = rootLogger;
	}
	
	public static Logger getRoot() {
		return ROOT_LOG;
	}
	
	public static Log getLog(Class clazz) {
		return new Log(LoggerFactory.getLogger(clazz));
	}
	
	public static Log getLog(String name) {
		return new Log(LoggerFactory.getLogger(name));
	}
}
