package com.rodini.ballotcomparator.model;

import org.eclipse.swt.widgets.Shell;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Event;

import com.rodini.ballotcomparator.BallotComparator;
import com.rodini.ballotcomparator.LoadPdfView;
import com.rodini.ballotcomparator.Views;
import com.rodini.ballotcomparator.view.CompareView;
import com.rodini.ballotcomparator.view.InitializeUI;
import com.rodini.ballotcomparator.model.SpecimenPdf;
import com.rodini.ballotcomparator.model.BallotDocx;

public class InitializeData {

	private static String PROPERTIES_FOLDER = "./resources";
	private static String PROPERTIES_FILE = "ballotcomparator.properties";
	private static String PROP_SPECIMEN_PDF_PATH = "specimen.pdf.path";
	private static String PROP_DOCX_FOLDER_PATH = "docx.folder.path";
	private static String PROP_DOCX_INDEX = "docx.index";
	
	static Properties props = new Properties();
	// Current SpecimentPdf object.
	public static SpecimenPdf spdf;
	// Current BallotDocx object.
	public static BallotDocx bdocx;
	
	public static void init() {
		// Read the properties file first.
		try (FileInputStream in = new FileInputStream(PROPERTIES_FOLDER + File.separator + PROPERTIES_FILE)) {
		    props.load(in);
		} catch (FileNotFoundException e) {
			// fatal error
			System.out.println("FileNotFoundException: " + e.getMessage());
		} catch (IOException e) {
			// fatal error
			System.out.println("IOException: " + e.getMessage());
		}
		// Create SpecimentPdf object (may fail.)
		initSpecimenPdf();
		// Create BallotDocx object (may fail.)
        initBallotDocx();
		InitializeUI.updateTitleBar();
	}

	public static void initSpecimenPdf() {
		spdf = null;
		String specimenPdfPath = props.getProperty(PROP_SPECIMEN_PDF_PATH);
		// read the properties file	
		if (specimenPdfPath == null || !new File(specimenPdfPath).exists()) {
			System.out.printf("Bad %s value: %s%n", PROP_SPECIMEN_PDF_PATH, specimenPdfPath );
		} else {
			System.out.printf("%s value: %s%n", PROP_SPECIMEN_PDF_PATH, specimenPdfPath);
			spdf = new SpecimenPdf(specimenPdfPath);
		}
	}
	
	public static void initBallotDocx() {
		bdocx = null;
		String docxFolderPath = props.getProperty(PROP_DOCX_FOLDER_PATH);
		File dirPath = null;
		// read the properties file	
		if (docxFolderPath != null) {
			dirPath = new File(docxFolderPath);
		}
		if (docxFolderPath == null || !dirPath.exists() || ! dirPath.isDirectory() ) {
			System.out.printf("Bad %s value: %s%n", PROP_DOCX_FOLDER_PATH, docxFolderPath );
		} else {
			System.out.printf("%s value: %s%n", PROP_DOCX_FOLDER_PATH, docxFolderPath );
			bdocx = new BallotDocx(docxFolderPath);
			String docxIndex = props.getProperty(PROP_DOCX_INDEX);
			try {
				int index = Integer.valueOf(docxIndex);
				bdocx.setIndex(index);
			} catch (NumberFormatException e) {
				System.out.printf("Bad %s value: %s%n", PROP_DOCX_INDEX, docxIndex);
				bdocx.setIndex(1);
			}
		}
	}
	// event handler for change to PROPERTIES
	public static void updateSpecimenPdfPath(String path) {
		props.setProperty(PROP_SPECIMEN_PDF_PATH, path);
		// Reinitialize to display the new specimen PDF.
		initSpecimenPdf();
	}
	
	public static void updateDocxFolderPath(String path) {
		props.setProperty(PROP_DOCX_FOLDER_PATH, path);
		props.setProperty(PROP_DOCX_INDEX, "1");
		// Reinitialize to display the new DOCX file.
		initBallotDocx();
	}

	public static void updateDocxIndex(int index) {
		props.setProperty(PROP_DOCX_INDEX, Integer.toString(index));
	}
	
	public static void term() {
		// Write out the properties file.
		try (FileWriter out = new FileWriter(new File(PROPERTIES_FOLDER + File.separator + PROPERTIES_FILE))) {
			updateDocxIndex(bdocx.getIndex());
		    props.store(out, null);
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
		}
	}
}
