package com.rodini.ballotcomparator.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.rodini.ballotcomparator.VIEWS.LEFT_VIEW;
import com.rodini.ballotcomparator.LoadPdfView;
import com.rodini.ballotcomparator.view.InitializeUI;

/**
 * SpecimenPdf acts as the container for the VS specimen PDF
 * 
 * Notes:
 * - In the past there was a one-to-one correspondence between
 *   precinct level PDFs and DOCX files but VS ruined this and
 *   caused BallotGen to change since there can be a mixture
 *   of 1 page pdfs and 2 page pdfs within the specimen.
 * - If BallotGen every reverts to busting out the precinct
 *   level pdfs, this class should be revised.
 */
public class SpecimenPdf {
	private Logger logger = LogManager.getLogger(SpecimenPdf.class);
	private String path;
	private LoadPdfView lpv;
	// constructor
	SpecimenPdf(String pdfPath) {
		path = pdfPath;
		LoadPdfView lpv = new LoadPdfView(InitializeUI.getCompareView(), LEFT_VIEW);
		lpv.load(path);
		logger.debug("SpecimenPdf object creatd on file: " + path);
	}
	// NOT currently used.
	public String getPath() {
		return path; 
	}
	// NOT currently used.
	public void load() {
		pdfChange(0);
	}
	
	// NOT currently called from PaginationBar
	public void pdfChange(int index) {
		InitializeUI.updateTitleBar();
	}

}
