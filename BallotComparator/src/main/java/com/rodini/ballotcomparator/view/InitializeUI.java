/** 
 * InitializeUI builds the initial user interface out of 
 * Standard Widget Toolkit (SWT) widgets and a custom 
 * pagination widget.  SWT was chosen because it supports
 * MS Word Component Object Model APIs to open a read-only
 * view of a Word document.
 */
package com.rodini.ballotcomparator.view;

import org.eclipse.swt.widgets.Shell;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

import com.rodini.ballotcomparator.model.BallotDocx;
import com.rodini.ballotcomparator.model.InitializeData;
import com.rodini.ballotcomparator.model.SpecimenPdf;

public class InitializeUI {
	
	private static CompareView compView;
	private static PaginationBar bar;
	private static String APPLICATION_NAME = "Ballot Comparator";
	private static Shell shell;
	
	public static void init(Shell shell) {
		InitializeUI.shell = shell;
		shell.setLayout(new GridLayout(1, false));
		// set title bar.
        shell.setText(APPLICATION_NAME);	
		// create menu bar
		MenuBar menuBar = new MenuBar(shell);
		// create compare views.
		compView = new CompareView(shell);
		// create pagination bar.
		bar = new PaginationBar(shell, SWT.NONE);
		bar.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
//		bar.setTotalPages(25);
		// Listen for the Exit event
		shell.addListener(SWT.Close, event -> {
			InitializeData.term();
//			boolean ok = saveDocxPosition();
//		    if (!ok) {
//		        event.doit = false;   // cancel exit
//		    }
		});
	}
	
	public static CompareView getCompareView() {
		return compView;
	}
	
	public static PaginationBar getPaginationBar() {
		return bar;
	}
	
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
}
