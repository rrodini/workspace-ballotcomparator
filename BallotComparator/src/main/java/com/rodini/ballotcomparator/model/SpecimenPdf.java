package com.rodini.ballotcomparator.model;

import static com.rodini.ballotcomparator.Views.LEFT_VIEW;

import com.rodini.ballotcomparator.LoadPdfView;
import com.rodini.ballotcomparator.view.InitializeUI;

public class SpecimenPdf {

	// TODO: Change all to static
	private String path;
	private LoadPdfView lpv;

	
	SpecimenPdf(String pdfPath) {
		path = pdfPath;
		LoadPdfView lpv = new LoadPdfView(InitializeUI.getCompareView(), LEFT_VIEW);
		lpv.load(path);
	}
	
	public String getPath() {
		return path; 
	}
	// NOT currently used.
	public void load() {
		// Show the file
		System.out.println("SpecimenPdf:load() called.");
		pdfChange(0);
	}
	
	// NOT currently called from PaginationBar
	public void pdfChange(int index) {
		InitializeUI.updateTitleBar();
	}

}
