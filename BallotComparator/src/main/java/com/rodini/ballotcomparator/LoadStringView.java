package com.rodini.ballotcomparator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.rodini.ballotcomparator.view.CompareView;


/**
 * LoadStringView is mainly for debugging.
 * 
 * Notes:
 * - Not used in production application.
 */
public class LoadStringView extends LoadView {
	

	public LoadStringView(CompareView views, VIEWS which) {
		super(views, which);
	}

	public void load(String string) {
//		System.out.printf("LoadStringView:load(%s, %s)%n", which, string);
		Composite view = getMyView();
		// Remove old content.
	    for (Control child : view.getChildren()) {
	        child.dispose();
	    }
		Text text = new Text(view, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		text.setText(string);
		view.layout(true, true);
	}
	
}
