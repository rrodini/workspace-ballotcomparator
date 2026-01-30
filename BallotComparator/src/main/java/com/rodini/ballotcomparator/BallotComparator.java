/** 
 * BallotComparator is a Windows based utility for the preparers 
 * of the Chester County Democratic Sample Ballots.  It opens two
 * "views" - one on the Voter Services specimen PDF (which contains
 * all 231 ballots in one pdf, and the output docx files from the
 * BallotGen program. This facilitates rapid validation of the 
 * Democratic sample ballots.
 * 
 * Notes:
 * - This program runs on Windows only.
 */
package com.rodini.ballotcomparator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.rodini.ballotcomparator.view.InitializeUI;
import com.rodini.ballotcomparator.model.InitializeData;

public class BallotComparator {
	// Display acts are the Windows application.
	final static Display display = new Display();
	// Shell acts as the main window.
	final static Shell shell = new Shell(display);

	public static void main(String[] args) {
		System.out.printf("SWT version: %d%n", SWT.getVersion());
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
	}
	
	public static void shutdown() {
	    try {
	        if (shell != null && !shell.isDisposed()) {
	            shell.close();
	        }
	    } catch (Exception e) {
//	        e.printStackTrace();
	    }

	    try {
	        if (display != null && !display.isDisposed()) {
	            display.dispose();
	        }
	    } catch (Exception e) {
//	        e.printStackTrace();
	    }
	}
	

}
