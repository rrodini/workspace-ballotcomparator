/**
 * MenuBar builds the main menu bar and processes 
 * events generated from the menu dialogs.
 * 
 * Notes: 
 * - The File menu is frequently hidden by the PDF
 *   browser view message. To see the File menu
 *   the user must X out that message.
 */
package com.rodini.ballotcomparator.view;

import org.eclipse.swt.widgets.Shell;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.rodini.ballotcomparator.BallotComparator;
import com.rodini.ballotcomparator.model.InitializeData;

public class MenuBar {
	private final static Logger logger = LogManager.getRootLogger();

	private final Menu m;
	public static final int SPECIMEN_PDF_PATH_CHANGE = 5000;
	public static final int DOCX_FOLDER_PATH_CHANGE = 5001;

	MenuBar(Shell shell) {
		m = new Menu(shell, SWT.BAR);
		// create a File menu and add an Exit item
		final MenuItem file = new MenuItem(m, SWT.CASCADE);
		file.setText("File");
		final Menu filemenu = new Menu(shell, SWT.DROP_DOWN);
		file.setMenu(filemenu);
		final MenuItem openPdfItem = new MenuItem(filemenu, SWT.PUSH);
		openPdfItem.setText("Open VS Specimen PDF");
		openPdfItem.addListener(SWT.Selection, event -> {
			pdfFileDialog(shell);
		});
		final MenuItem openDocxFolderItem = new MenuItem(filemenu, SWT.PUSH);
		openDocxFolderItem.setText("Open DOCX folder");
		openDocxFolderItem.addListener(SWT.Selection, event -> {
			docxFolderDialog(shell);
		});
		final MenuItem separator = new MenuItem(filemenu, SWT.SEPARATOR);
		final MenuItem exitItem = new MenuItem(filemenu, SWT.PUSH);
		exitItem.setText("Exit");
		exitItem.addListener(SWT.Selection, event -> {
			BallotComparator.shutdown();
		});
		shell.setMenuBar(m);
	}
	/**
	 * pdfFileDialog is the menu event handler.
	 * 
	 * @param shell
	 */
	private void pdfFileDialog(Shell shell) {
		logger.debug("pdfFileDialog started.");
		FileDialog dialog = new FileDialog(shell, SWT.OPEN);
		dialog.setText("Open the VS specimen pdf");
		dialog.setFilterExtensions(new String[] { "*.pdf" });
		String currentDirectory = new File("").getAbsolutePath();
		dialog.setFilterPath(currentDirectory);

		String specimenPdfPath = dialog.open();
		if (specimenPdfPath != null) {
			logger.debug("pdfFileDialog folder: " + specimenPdfPath);
			InitializeData.updateSpecimenPdfPath(specimenPdfPath);
		}
	}

//	private void docxFolderDialog1(Shell shell) {
//		FileDialog dialog = new FileDialog(shell);
//		dialog.setText("Open the docx folder e.g. chester-output");
//		dialog.setFilterExtensions(new String[] { "*.docx" });
//		String currentDirectory = new File("").getAbsolutePath();
//		dialog.setFilterPath(currentDirectory);
//		String first = dialog.open();
//		if (first != null) {
//			String docxFolderPath = dialog.getFilterPath();
//			System.out.println("Selected directory: " + docxFolderPath);
//			InitializeData.updateDocxFolderPath(docxFolderPath);
//		}
//	}
	/**
	 * docxFolderDialog is the menu event handler.
	 * 
	 * @param shell
	 */
	private void docxFolderDialog(Shell shell) {
		logger.debug("docxFolderDialog started.");
		DirectoryDialog dialog = new DirectoryDialog(shell);
		dialog.setText("Open the docx folder e.g. chester-output");
		String currentDirectory = new File("").getAbsolutePath();
		dialog.setFilterPath(currentDirectory);
		String dir = dialog.open();
		if (dir != null) {
			logger.debug("docxFolderDialog folder: " + dir);
			InitializeData.updateDocxFolderPath(dir);
		}
	}

}
