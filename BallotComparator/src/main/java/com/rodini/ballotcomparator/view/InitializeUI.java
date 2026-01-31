/** 
 * InitializeUI builds the initial user interface out of 
 * Standard Widget Toolkit (SWT) widgets and a custom 
 * pagination widget.  SWT was chosen because it supports
 * MS Word Component Object Model APIs to open a read-only
 * view of a Word document. It also has a built-in browser
 * with a plugin that can display PDF file.
 * 
 * Notes:
 * - InitializeUI has some general purpose methods e.g.
 *   displayMwssage().
 */
package com.rodini.ballotcomparator.view;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.rodini.ballotcomparator.model.InitializeData;

public class InitializeUI {
	private final static Logger logger = LogManager.getRootLogger();

	private static CompareView compView;
	private static PaginationBar bar;
	public static String APPLICATION_NAME = "Ballot Comparator";
	private static Shell shell;
	/**
	 * init creates the needed UI components.
	 * 
	 * @param shell SWT Shell object.
	 */
	public static void init(Shell shell) {
		logger.debug("Enter InitializeUI.init");
		InitializeUI.shell = shell;
		shell.setLayout(new GridLayout(1, false));
		// Set title bar element.
        shell.setText(APPLICATION_NAME);	
		// Create menu bar
		MenuBar menuBar = new MenuBar(shell);
		// Create compare views.
		compView = new CompareView(shell);
		// create pagination bar.
		bar = new PaginationBar(shell, SWT.NONE);
		bar.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
		// Listen for the Exit event
		shell.addListener(SWT.Close, event -> {
			InitializeData.term();
		});
	}
	/**
	 * Get the CompareView object.
	 * @return CompareView object.
	 */
	public static CompareView getCompareView() {
		return compView;
	}
	/**
	 * Get the PaginationBar object.
	 * @return PaginationBar object.
	 */
	public static PaginationBar getPaginationBar() {
		return bar;
	}
	// Update the Title Bar element.
	// Unfortunately, Windows will not center justify the text.
	public static void updateTitleBar() {
		String pdfFileName = "";
		String pdfFilePath = InitializeData.spdf == null? null: InitializeData.spdf.getPath();
		if (pdfFilePath == null) {
			pdfFilePath = "";
		}
		
		if (!pdfFilePath.isEmpty()) {
	        Path path = Paths.get(pdfFilePath);
	        Path fileNamePath = path.getFileName();
	        pdfFileName = fileNamePath.toString();
		}
		String docxFileName = InitializeData.bdocx == null? null : InitializeData.bdocx.fileName;
		if (docxFileName == null) {
			docxFileName = "";
		}
		// Skip the APPLICATION_NAME as it is distracting.
		// Below is a kludge since Windows will not center justify the title bar text.
		String spacer = "'" + " ".repeat(120);
		String fullTitle =  spacer + pdfFileName + " :: " + docxFileName;
		InitializeUI.shell.setText(fullTitle);
	}
	/**
	 * Display a modal Message Box.  Currently only used for errors.
	 * 
	 * @param msg - Error message.
	 */
	public static void displayMessage(String msg) {
		MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
		mb.setText(String.format("%s - %s%n", APPLICATION_NAME, "ERROR"));
		mb.setMessage("msg");
		mb.open();
	}
}
