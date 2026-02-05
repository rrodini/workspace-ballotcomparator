package com.rodini.ballotcomparator;

import org.eclipse.swt.widgets.Composite;

import com.rodini.ballotcomparator.view.CompareView;
import com.rodini.ballotcomparator.VIEWS;
/**
 * LoadView is the parent class for LoadPdfView and LoadDocxView.
 */
public abstract class LoadView {
	
	protected CompareView views;
	protected Composite myView;		
	protected VIEWS which;	

	public LoadView(CompareView views, VIEWS which) {
		this.views = views;
		this.which = which;
		myView = getPreferredView();		
	}
	
	public abstract void load(String s);
	
	private Composite getPreferredView() {
		Composite view = null;
		switch (which) {
		case LEFT_VIEW -> view = views.getLeftView(); 
		case RIGHT_VIEW -> view = views.getRightView(); 
		}
		return view;
	}
	
	protected Composite getMyView() {
		return myView;
	}
	
}
