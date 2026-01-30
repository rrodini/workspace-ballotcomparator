package com.rodini;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.ole.win32.OLE;
//import org.eclipse.swt.ole.win32.
import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.OleControlSite;
import org.eclipse.swt.ole.win32.OleFrame;
import org.eclipse.swt.ole.win32.Variant;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SWTLaunchAcrobatCOM {

    public static void main(String[] args) throws InterruptedException {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Embedded PDF Viewer (Adobe ActiveX)");
        shell.setLayout(new FillLayout());
        // Create an OLE frame
        OleFrame oleFrame = new OleFrame(shell, SWT.NONE);
//        System.out.println("Before sleep.");
//        // Let go of processor, so app can display window.
//        Thread.sleep(5000);
//        System.out.println("After  sleep.");
        
        try {
            // Create the Adobe PDF ActiveX control
            OleControlSite controlSite = new OleControlSite(oleFrame, SWT.NONE, "AcroPDF.PDF");
            controlSite.doVerb(OLE.OLEIVERB_INPLACEACTIVATE);
//
//            // Wrap the ActiveX control in OleAutomation
//            OleAutomation pdf = new OleAutomation(controlSite);
//
//            // --- LoadFile("C:\\test.pdf") ---
//            int[] dispIdLoadFile = pdf.getIDsOfNames(new String[]{"LoadFile"});
//            pdf.invoke(dispIdLoadFile[0], new Variant[] {
//                new Variant(".\\005_ATGLEN.pdf")
//            });
//
//            // --- setShowToolbar(false) ---
//            int[] dispIdShowToolbar = pdf.getIDsOfNames(new String[]{"setShowToolbar"});
//            pdf.invoke(dispIdShowToolbar[0], new Variant[] {
//                new Variant(false)
//            });
//
//            // --- setZoom(100) ---
//            int[] dispIdZoom = pdf.getIDsOfNames(new String[]{"setZoom"});
//            pdf.invoke(dispIdZoom[0], new Variant[] {
//                new Variant(100)
//            });
//
//
        } catch (Exception e) {
            e.printStackTrace();
        }
        shell.setSize(100,100);
        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }
}

