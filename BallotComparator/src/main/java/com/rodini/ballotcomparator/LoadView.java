package com.rodini.ballotcomparator;

import org.eclipse.swt.widgets.Composite;

import com.rodini.ballotcomparator.view.CompareView;
import com.rodini.ballotcomparator.Views;

public abstract class LoadView {
	
	protected CompareView views;
	protected Composite myView;		
	protected Views which;	

	public LoadView(CompareView views, Views which) {
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
