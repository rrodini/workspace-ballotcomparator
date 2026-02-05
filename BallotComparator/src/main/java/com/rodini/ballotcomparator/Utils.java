/**
 * Utils contains some useful utility methods.
 */
package com.rodini.ballotcomparator;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rodini.ballotcomparator.view.InitializeUI;

public class Utils {
	
	/**
	 * timeStamp generates a simple, formatted time stamp.
	 * @return Example: Jan. 31, 2026, 10:06am.
	 */
	public static String timeStamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM. dd, yyyy hh:mma");
        String formatted = now.format(formatter);
        return formatted;
	}
	/**
	 * logAppTimeStamp logs an application event with a timestamp. E.g.
	 * BallotComparator: event: start at: Jan. 31, 2026 09:54am
	 * 
	 * @param logger of the caller.
	 * @param msg "started", "blew up", "terminated", etc.
	 */
	public static void logAppTimeStamp(Logger logger, String msg) {
		logger.info(String.format("%s: %s at: %s", InitializeUI.APPLICATION_NAME, msg, timeStamp()));
	}
	/**
	 * fatalError - log a fatal error and shutdown.
	 * @param logger object where error occurred.
	 * @param msg error message.
 	 */
	public static void fatalError(Logger logger, String msg) {
		InitializeUI.displayMessage(msg);
		logger.error(msg);
		BallotComparator.shutdown();
	}
	/**
	 * getAppVersion get the Maven version string.
	 * @return Mavern version.
	 */
	public static String getAppVersion() {
		// This value is due to the misconfiguration of the project on the file system.
		String pomPropsPath =  "C:\\Users\\rrodi\\Documents\\BallotComparator\\BallotComparator\\target\\classes\\META-INF\\maven\\com.rodini\\ballotcomparator\\pom.properties";
		Properties props = new Properties();
		String unknown = "x.y.z";
		String version = unknown;
		try (FileInputStream in = new FileInputStream(pomPropsPath)) {
		    props.load(in);
		    version = props.getProperty("version", unknown);
		} catch (IOException e) {
			// no harm in squelching error.
		}
		return version;
	}
	
}
