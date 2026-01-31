/** 
 * CompareView creates the two views that allow visual comparison
 * between the Voter Services specimen PDF and the precinct level
 * DOCX sample ballots.
 * 
 * Notes:
 * - future enhancement may be to bust the specimen into individual
 *   PDF ballots. This is extra work because the precinct ballots
 *   may vary between one or two pages within the specimen PDF.
 */
package com.rodini.ballotcomparator.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class CompareView {
	private final SashForm sashForm;
	private Composite leftView;
	private Composite rightView;
	/**
	 * CompareView creates the SWT widget with the SashForm widget.
	 * PDF view on left, Docx view on right.
	 * 
	 * @param shell SWT Shell object.
	 */
	CompareView(Shell shell) {
		sashForm = new SashForm(shell, SWT.HORIZONTAL); // Or SWT.VERTICAL
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		//
		// Left View (e.g. Acrobat)
		//
		leftView = new Composite(sashForm, SWT.BORDER);
		leftView.setLayout(new FillLayout());
        //
		// Right View (e.g., Word)
        //
		rightView = new Composite(sashForm, SWT.BORDER);
		rightView.setLayout(new FillLayout());	
	}
	/**
	 * Get the left view Composite object.
	 * @return SWT Composite object
	 */
	public Composite getLeftView() {
		return leftView;
	}
	/**
	 * Get the right view Composite object.
	 * @return SWT Composite object
	 */
	public Composite getRightView() {
		return rightView;
	}
	
	
	
}
