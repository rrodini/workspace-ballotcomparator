package com.rodini.ballotcomparator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

import org.eclipse.swt.SWT;
//import org.eclipse.swt.widgets.Control;
//import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.ole.win32.OLE;
import org.eclipse.swt.ole.win32.OleClientSite;
import org.eclipse.swt.ole.win32.OleFrame;
//import org.eclipse.swt.internal.win32.SWTFlags;

import com.rodini.ballotcomparator.view.CompareView;
/**
 * LoadDocxView displays the current docx file into view.
 * 
 * Notes:
 * - Technique is based on MS Object Linking and Embedding (OLE) APIs
 * - Much effort was put into getting Word to save the file following
 *   edits. Could not get it to work.  Alternative, it the Edit
 *   button in the bottom right corner of the status bar.
 */
public class LoadDocxView extends LoadView {
	private Logger logger = LogManager.getLogger(LoadDocxView.class);
	// MS Copilot suggests reusing 
	// frame object between Word files.
	private static OleFrame frame;
	// Same for the sit object;
	private static OleClientSite site;

	public LoadDocxView(CompareView views, VIEWS which) {
		super(views, which);
		if (frame == null || frame.isDisposed()) {
			// Create single instance.
			frame = new OleFrame(getMyView(), SWT.NONE);
			getMyView().layout(true, true);
			logger.debug("OleFrame created.");
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
			logger.debug("Site hidden/deactivated/disposed");
		}
		try {
			// Create a new client site.
			site = new OleClientSite(frame, SWT.NONE, "Word.Document", new File(docxFilePath));
			// Activate the document in-place to make it visible and interactive.
			site.doVerb(OLE.OLEIVERB_INPLACEACTIVATE);
			logger.debug("New site created and activated.");
		} catch (Exception e) {
			Utils.fatalError(logger, "Failed to embed Word document: " + e.getMessage());
		}
	}

}
