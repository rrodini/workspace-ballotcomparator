package com.rodini;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.win32.W32APIOptions;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Event;

import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.OleFrame;
import org.eclipse.swt.ole.win32.Variant;
import static org.eclipse.swt.ole.win32.Variant.*;

public class SWTLaunchWordReadWrite {

    // Minimal User32 interface for reparenting and resizing
    public interface MyUser32 extends User32 {
        MyUser32 INSTANCE = Native.load("user32", MyUser32.class, W32APIOptions.DEFAULT_OPTIONS);

        HWND SetParent(HWND hWndChild, HWND hWndNewParent);
        boolean MoveWindow(HWND hWnd, int X, int Y, int nWidth, int nHeight, boolean bRepaint);
        boolean IsWindow(HWND hWnd);
        HWND FindWindow(String lpClassName, String lpWindowName);
        HWND FindWindowEx(HWND hwndParent, HWND hwndChildAfter, String lpszClass, String lpszWindow);
    }

    static HWND wordHwnd = null;

    public static void main(String[] args) {

        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Word Automation Host");
        shell.setLayout(new FillLayout());
        shell.setSize(1000, 800);

        // Composite that will host Word's window
        Composite wordHost = new Composite(shell, SWT.EMBEDDED);
        wordHost.setLayout(new FillLayout());

        shell.open();

        // --- STEP 1: Start Word via automation ---
        OleFrame frame = new OleFrame(wordHost, SWT.NONE);

        // Create Word.Application as an automation server
        OleAutomation app = new OleAutomation("Word.Application");
        // Make Word visible
        int[] visibleId = app.getIDsOfNames(new String[]{"Visible"});
        app.setProperty(visibleId[0], new Variant(true));

        // --- STEP 2: Open a document ---
        int[] docsId = app.getIDsOfNames(new String[]{"Documents"});
        Variant docsVariant = app.getProperty(docsId[0]);
        OleAutomation docs = docsVariant.getAutomation();

        int[] openId = docs.getIDsOfNames(new String[]{"Open"});
        // C:\\Users\\rrodi\\Documents\\BallotComparator\\workspace-experiment\\swt-launch-adobe\\005_ATGLEN.docx

        Variant[] openArgs = { new Variant("C:\\Users\\rrodi\\Documents\\BallotComparator\\workspace-experiment\\swt-launch-adobe-word\\005_ATGLEN.docx") };
        // C:\\Users\\rrodi\\Documents\\BallotComparator\\workspace-experiment\\swt-launch-adobe\\005_ATGLEN.docx
        docs.invoke(openId[0], openArgs);

        // --- STEP 3: Get the main Word window handle (HWND) ---

        // Word's main window class is usually "OpusApp"
        // We may need to wait a bit for it to appear
//        HWND wordHwnd = null;
// ATTEMPT 1 : Find Word window.
//        for (int i = 0; i < 50; i++) {
//            wordHwnd = MyUser32.INSTANCE.FindWindow("OpusApp", null);
//            if (wordHwnd != null && MyUser32.INSTANCE.IsWindow(wordHwnd)) {
//                break;
//            }
//            if (!display.readAndDispatch()) display.sleep();
//        }
// ATTEMPT 2
        for (int i = 0; i < 50; i++) {
            HWND top = MyUser32.INSTANCE.FindWindow("OpusApp", null);

            if (top != null && MyUser32.INSTANCE.IsWindow(top)) {
                HWND child = MyUser32.INSTANCE.FindWindowEx(top, null, null, null);
                wordHwnd = (child != null) ? child : top;
                break;
            }

            if (!display.readAndDispatch()) display.sleep();
        }

        
        if (wordHwnd == null) {
            System.out.println("Could not find Word main window.");
        } else {
            System.out.println("Found Word HWND: " + wordHwnd);

            // --- STEP 4: Reparent Word into our Composite ---

            // Get the HWND of the SWT Composite
            long hostHandle = wordHost.handle; // SWT handle is HWND on Windows
            HWND hostHwnd = new HWND(new Pointer(hostHandle));

            MyUser32.INSTANCE.SetParent(wordHwnd, hostHwnd);

            // Initial sizing
            Rectangle clientArea = wordHost.getClientArea();
            MyUser32.INSTANCE.MoveWindow(
                    wordHwnd,
                    0,
                    0,
                    clientArea.width,
                    clientArea.height,
                    true
            );

            // --- STEP 5: Resize Word when the Composite resizes ---
            wordHost.addListener(SWT.Resize, new Listener() {
                @Override
                public void handleEvent(Event event) {
                    if (wordHwnd != null && MyUser32.INSTANCE.IsWindow(wordHwnd)) {
                        Rectangle area = wordHost.getClientArea();
                        MyUser32.INSTANCE.MoveWindow(
                                wordHwnd,
                                0,
                                0,
                                area.width,
                                area.height,
                                true
                        );
                    }
                }
            });

         // --- STEP 6: Get ActiveDocument (defensive version) ---

         // 1. Try ActiveDocument directly
         int[] activeDocId = app.getIDsOfNames(new String[]{"ActiveDocument"});
         Variant activeDocVariant = null;

         // Word may not have finished opening the file yet.
         // Wait up to ~1 second (50 × 20ms)
         for (int i = 0; i < 50; i++) {
             activeDocVariant = app.getProperty(activeDocId[0]);
             if (activeDocVariant != null && activeDocVariant.getType() != 0) {
                 System.out.println("ActiveDocument is ready.");
                 break;
             }
             if (!display.readAndDispatch()) display.sleep();
         }
      // 2. If ActiveDocument is still null, fall back to Documents.Item(1)
         if (activeDocVariant == null || activeDocVariant.getType() == 0) {

             System.out.println("ActiveDocument not ready. Trying Documents.Item(1)...");

             docsId = app.getIDsOfNames(new String[]{"Documents"});
             docsVariant = app.getProperty(docsId[0]);
             docs = docsVariant.getAutomation();

             int[] itemId = docs.getIDsOfNames(new String[]{"Item"});
             Variant[] itemArgs = { new Variant(1) };
             Variant docVariant = docs.invoke(itemId[0], itemArgs);

             if (docVariant == null || docVariant.getType() == 0) {
                 System.out.println("ERROR: Could not retrieve any open document.");
                 return; // or handle gracefully
             }

             activeDocVariant = docVariant;
         }

      // 3. Convert to OleAutomation
      OleAutomation activeDoc = activeDocVariant.getAutomation();
      if (activeDoc == null) {
          System.out.println("ERROR: activeDocVariant.getAutomation() returned null.");
          return;
      }

      System.out.println("Successfully retrieved ActiveDocument automation object.");
//
//         OleAutomation activeDoc = null;
//            // Saved property
//            int[] savedId = activeDoc.getIDsOfNames(new String[]{"Saved"});
//
//            // ExecuteMso
//            int[] execId = app.getIDsOfNames(new String[]{"ExecuteMso"});
//
//            display.timerExec(1000, new Runnable() {
//                @Override
//                public void run() {
//                    if (shell.isDisposed()) return;
//
//                    Variant savedVariant = activeDoc.getProperty(savedId[0]);
//                    boolean isSaved = savedVariant.getBoolean();
//
//                    if (!isSaved) {
//                        Variant[] args = { new Variant("FileSave") };
//                        app.invoke(execId[0], args);
//                        System.out.println("Auto-saved at " + System.currentTimeMillis());
//                    }
//
//                    display.timerExec(1000, this);
//                }
//            });
        }

        // --- Event loop ---
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }

        // --- Cleanup: close Word ---
        try {
            int[] quitId = app.getIDsOfNames(new String[]{"Quit"});
            app.invoke(quitId[0]);
        } catch (Exception e) {
            // ignore
        }

        display.dispose();
    }
}