package com.rodini.ballotcomparator.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rodini.ballotcomparator.Utils;
import com.rodini.ballotcomparator.view.InitializeUI;

/**
 * InitializeData reads and maintains the properties file
 * (resources/ballotcomparator.properties) which stores
 * file paths to the VS specimen PDF and the BallotGen
 * .docx folder.  Plus one addl. property - the current
 * index into .docx folder so 
 */
public class InitializeData {
	private static Logger logger = LogManager.getLogger(InitializeData.class);
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
	/**
	 * init reads ballotcomparator.properties. The file must be there.
	 * Then it initializes the two data containers.
	 */
	public static void init() {
		String propFilePath = PROPERTIES_FOLDER + File.separator + PROPERTIES_FILE;
		logger.info("Reading props file: " + propFilePath);
		// Read the properties file first.
		try (FileInputStream in = new FileInputStream(propFilePath)) {
		    props.load(in);
		} catch (IOException e) {
			// fatal error
			Utils.fatalError(logger, "Props file read error: " + e.getMessage());
		}
		// Create SpecimentPdf object (may fail.)
		initSpecimenPdf();
		// Create BallotDocx object (may fail.)
        initBallotDocx();
		InitializeUI.updateTitleBar();
	}
	/**
	 * Create the data container for Specimen PDF from path found in
	 * properties file.
	 */
	public static void initSpecimenPdf() {
		spdf = null;
		String specimenPdfPath = props.getProperty(PROP_SPECIMEN_PDF_PATH);
		// read the properties file	
		if (specimenPdfPath == null) {
			// Message box is commented out so user can see application first.
			// InitializeUI.displayMessage(String.format("Use File menu and naviage to the specimen PDF."));
		} else if (! new File(specimenPdfPath).exists()) {
			InitializeUI.displayMessage(String.format("Bad %s value: %s.", PROP_SPECIMEN_PDF_PATH, specimenPdfPath));
		} else {
			logger.info(String.format("%s value: %s.", PROP_SPECIMEN_PDF_PATH, specimenPdfPath));
			spdf = new SpecimenPdf(specimenPdfPath);
		}
	}
	/**
	 * Create the data container for the BallotGen .docx files
	 * from the path found in properties file.
	 */
	public static void initBallotDocx() {
		bdocx = null;
		String docxFolderPath = props.getProperty(PROP_DOCX_FOLDER_PATH);
		File dirPath = null;
		// read the properties file
		if (docxFolderPath == null) {
			// Message box is commented out so user can see application first.
			//InitializeUI.displayMessage(String.format("Use File menu and naviage to the sample ballot DOCX folder."));
		} else {
			dirPath = new File(docxFolderPath);

			if (!dirPath.exists() || !dirPath.isDirectory()) {
				InitializeUI.displayMessage(
						String.format(docxFolderPath, "Bad %s value: %s.", PROP_SPECIMEN_PDF_PATH, docxFolderPath));
			} else {
				logger.info(String.format("%s value: %s.", PROP_DOCX_FOLDER_PATH, docxFolderPath));
				bdocx = new BallotDocx(docxFolderPath);
				String docxIndex = props.getProperty(PROP_DOCX_INDEX);
				try {
					int index = Integer.valueOf(docxIndex);
					bdocx.setIndex(index);
				} catch (NumberFormatException e) {
					logger.info(String.format("Bad %s value: %s.", PROP_DOCX_INDEX, docxIndex));
					bdocx.setIndex(1);
				}
			}
		}
	}

	/**
	 * updateSpecimenPdfPath is the event handler for change to PROP_SPECIMEN_PDF_PATH.
	 * @param path
	 */
	public static void updateSpecimenPdfPath(String path) {
		props.setProperty(PROP_SPECIMEN_PDF_PATH, path);
		// Reinitialize to display the new specimen PDF.
		initSpecimenPdf();
	}
	/**
	 * updateSpecimenPdfPath is theevent handler for change to PROP_DOCX_FOLDER_PATH.
	 * @param path
	 */
	public static void updateDocxFolderPath(String path) {
		props.setProperty(PROP_DOCX_FOLDER_PATH, path);
		props.setProperty(PROP_DOCX_INDEX, "1");
		// Reinitialize to display the new DOCX file.
		initBallotDocx();
	}
	/**
	 * updateSpecimenPdfPath is the event handler for change to PROP_DOCX_INDEX.
	 * @param path
	 */
	public static void updateDocxIndex(int index) {
		props.setProperty(PROP_DOCX_INDEX, Integer.toString(index));
	}
	/**
	 * term writes out the current values to the properties file.
	 */
	public static void term() {
		String propFilePath = PROPERTIES_FOLDER + File.separator + PROPERTIES_FILE;
		logger.info("Writing props file: " + propFilePath);
		// Write out the properties file.
		try (FileWriter out = new FileWriter(new File(propFilePath))) {
			if (bdocx != null) {
				updateDocxIndex(bdocx.getIndex());				
			}
		    props.store(out, null);
		} catch (IOException e) {
			// fatal error
			Utils.fatalError(logger, "Props file write error: " + e.getMessage());
		}
	}
}
