package com.rodini;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.browser.*;

public class SWTLaunchBrowserPDF {

    public static void main(String[] args) {
        // Top-level SWT objects
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("SWT PDF Viewer");
        shell.setLayout(new FillLayout());   // Parent layout

        // Parent composite for the Browser
        Composite parent = new Composite(shell, SWT.NONE);
        parent.setLayout(new FillLayout());

        // Browser widget (uses WebView2 on modern Windows)
        Browser browser = new Browser(parent, SWT.NONE);

        // Load a PDF file (local or remote)
//        browser.setUrl("file:///C|/Users/rrodi/Documents/BallotComparator/workspace-experiment/swt-launch-adobe/005_ATGLEN_VS.pdf");
        browser.setUrl("file:///C|/Users/rrodi/Documents/BallotComparator/workspace-experiment/swt-launch-adobe/General-2025.pdf");

        // Show the window
        shell.setSize(900, 700);
        shell.open();

        // Event loop
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }
}