/** 
 * BallotComparator is a Windows based utility for the preparers 
 * of the Chester County Democratic Sample Ballots.  It opens two
 * "views" - one on the Voter Services specimen PDF (which contains
 * all 231 ballots in one pdf), and one on the output docx files from the
 * BallotGen program. This facilitates visual inspection of the 
 * Democratic sample ballots.
 * 
 * Notes:
 * - This program runs on Windows only.
 */
package com.rodini.ballotcomparator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.rodini.ballotcomparator.model.InitializeData;
import com.rodini.ballotcomparator.view.InitializeUI;

public class BallotComparator {
	private final static Logger logger = LogManager.getRootLogger();
	// Display acts are the Windows application.
	final static Display display = new Display();
	// Shell acts as the main window.
	final static Shell shell = new Shell(display);
	/**
	 * main is the starting point. It is typical of SWT applications.
	 * 
	 * @param args not used.
	 */
	public static void main(String[] args) {
		Utils.logAppTimeStamp(logger, "started");
		int swtVersion =  SWT.getVersion();
		logger.debug("BallotComparator SWT version: " + swtVersion);
		// Some controller stuff here...
		shell.setSize(900, 700);
		
		InitializeUI.init(shell);
		InitializeData.init();
		shell.open();
		// Windows message loop.
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		// Update the properties file.
		InitializeData.term();
		display.dispose();
		Utils.logAppTimeStamp(logger, "terminated");
	}
	
	public static void shutdown() {
	    try {
	        if (shell != null && !shell.isDisposed()) {
	            shell.close();
	        }
	    } catch (Exception e) {
	    }

	    try {
	        if (display != null && !display.isDisposed()) {
	            display.dispose();
	        }
	    } catch (Exception e) {
	    }
		Utils.logAppTimeStamp(logger, "terminated");
	}
	

}
