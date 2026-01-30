package com.rodini.ballotcomparator;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Control;
//import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.ole.win32.OLE;
import org.eclipse.swt.ole.win32.OleClientSite;
import org.eclipse.swt.ole.win32.OleFrame;
//import org.eclipse.swt.internal.win32.SWTFlags;


import com.rodini.ballotcomparator.view.CompareView;

public class LoadDocxView extends LoadView {
	// frame must be reused between Word files.
	private static OleFrame frame;
	private static OleClientSite site;

	public LoadDocxView(CompareView views, Views which) {
		super(views, which);
		if (frame == null || frame.isDisposed()) {
			// Create single instance.
			frame = new OleFrame(getMyView(), SWT.NONE);
			System.out.println("OleFrame created.");
			getMyView().layout(true, true);

		}
	}

	public void load(String docxFilePath) {
		// Recommended to conserve resources. The exact sequence of calls was 
		// prescribed by MS Copilot to prevent each new Word document view
		// from creeping to the right of the Composite (child) window.
		if (site != null && !site.isDisposed()) {
	        site.doVerb(OLE.OLEIVERB_HIDE);
			site.deactivateInPlaceClient();
			site.dispose();
		}
		System.out.printf("LoadDoxcView::load(%s)%n", docxFilePath);
		try {
			// Create a new client site.
			site = new OleClientSite(frame, SWT.NONE, "Word.Document", new File(docxFilePath));
			// Activate the document in-place to make it visible and interactive.
			site.doVerb(OLE.OLEIVERB_INPLACEACTIVATE);
			System.out.printf("OleSite activated%n");
		} catch (Exception e) {
            System.err.println("Failed to embed Word document: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
