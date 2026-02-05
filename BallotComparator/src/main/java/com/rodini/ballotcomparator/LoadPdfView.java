package com.rodini.ballotcomparator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;

import com.rodini.ballotcomparator.view.CompareView;
/**
 * LoadPdfView displays the current specimen PDF into the view.
 * 
 * Notes:
 * - Much effort was put into loading the Acrobat reader using OLE
 *   APIs (like MS Word). Could not get it to work. Copilor suggested
 *   using SWT's (Chrome's) browser with its built in PDF reader.
 */
public class LoadPdfView extends LoadView {
	private Logger logger = LogManager.getLogger(LoadPdfView.class);
	//	private static String viewPage = new File("resources/pdfjs/web/viewer.html").getAbsolutePath().replace("\\","/");
//	private static String FILE_PROTOCOL = "file:///";
//	private static String HTTP_PROTOCOL = "http://";
//	private static String fileQueryKey = "?file=";
	private static Browser browser;

	public LoadPdfView(CompareView views, VIEWS which) {
		super(views, which);
		if (browser == null) {
			// Create a single instance.
			browser = new Browser(myView, SWT.NONE);
			logger.debug("PDF browser created.");
		}
	}

//	@Override
	public void load(String pdfPath) {
		Composite view = myView;
		// Simplicity - no tracking of PDF page. Copilot states
		// that this requires a local HTTP server so that the PDF
    	// uri can be referenced w/o a CORS error.
		browser.setUrl(filePathToFileUrl(pdfPath));
	    view.layout(true, true);

// Complexity - the Chrome browser's CORS polity prevents below from working.
// NEED TO URL ENCODE QUERY STRING
//	String urlQueryValue = "file:///C:/Users/rrodi/Documents/BallotComparator/workspace-comparator/BallotComparator/General-2025.pdf";
//      //                              C:\Users\rrodi\Documents\BallotComparator\workspace-comparator\BallotComparator\General-2025.pdf
//      String urlViewPage = filePathToFileUrl(viewPage);
//      String urlComplete = urlViewPage + fileQueryKey + urlQueryValue;
//      System.out.printf("urlComplete: %s%n", urlComplete);
//      browser.setUrl(urlComplete);
// ENCODE THE QUERY STRING
//      String urlQueryValue = new File(pdfPath).getAbsolutePath().replace("\\", "/");
//      System.out.printf("urlQueryValue: %s%n", urlQueryValue);
//      String encodedUrlQueryValue = URLEncoder.encode(urlQueryValue, StandardCharsets.UTF_8);
//      System.out.printf("encodedUrlQueryValue: %s%n", encodedUrlQueryValue);
//      String urlComplete = FILE_PROTOCOL + viewPage + fileQueryKey + FILE_PROTOCOL + encodedUrlQueryValue;
//      System.out.printf("urlComplete: %s%n", urlComplete);
//      browser.setUrl(urlComplete);
//      view.layout(true, true);
	}
	/**
	 * filePathToFileUrl converts an absolute file path to a URI. The string
	 * value of the URI is returned.
	 * @param filePath abs. file path to PDF.
	 * @return URI of file path.
	 */
	private String filePathToFileUrl(String filePath) {
        //  Example:
		//  input:  C:\Users\rrodi\Documents\BallotComparator\workspace-experiment\swt-launch-adobe-word\General-2025.pdf
        //  output: file:///C:/Users/rrodi/Documents/BallotComparator/workspace-experiment/swt-launch-adobe-word/General-2025.pdf
		try {
			Path path = Paths.get(filePath);
			// Ensure the path is absolute; otherwise, this may lead to unexpected results.
			if (!path.isAbsolute()) {
				Utils.fatalError(logger, String.format("path is not absolute: %s", filePath));
				return null;
			}
			URI correctUri = path.toFile().toURI();
			String uriString = correctUri.toString();
			return uriString;

		} catch (Exception e) {
			Utils.fatalError(logger, String.format("cannot convert to URI: %s", e.getMessage()));
		}
		return "";
	}
}
