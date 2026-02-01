/**
 * Utils contains some useful utility methods.
 */
package com.rodini.ballotcomparator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
	
	public static void fatalError(Logger logger, String msg) {
		InitializeUI.displayMessage(msg);
		logger.error(msg);
		BallotComparator.shutdown();
	}
}
