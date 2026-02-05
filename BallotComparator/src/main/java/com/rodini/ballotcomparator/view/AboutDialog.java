package com.rodini.ballotcomparator.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.rodini.ballotcomparator.Utils;
/** 
 * AboutDialog builds the About box with application
 * name and version.  Version is the Maven version.
 * 
 * Notes:
 * - Code was written by MS Copilot. Not perfect at
 *   first but we got there.
 */
public class AboutDialog extends Dialog {

    private String appName = InitializeUI.APPLICATION_NAME;
    private String version = Utils.getAppVersion();

    public AboutDialog(Shell parent) {
        super(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        setText("About");
    }


    public void open() {
        Shell parent = getParent();
        Shell shell = new Shell(parent, getStyle());
        shell.setText(getText());
        shell.setLayout(new GridLayout(1, false));
        // Create a white background composite
        Composite content = new Composite(shell, SWT.NONE);
        Color white = shell.getDisplay().getSystemColor(SWT.COLOR_WHITE);
        content.setBackground(white);
        content.setLayout(new GridLayout(1, false));        
        content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        // App name
        Label nameLabel = new Label(content, SWT.NONE);
        nameLabel.setBackground(white);
        nameLabel.setText(appName);
        nameLabel.setFont(content.getDisplay().getSystemFont());
        nameLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
        // Version
        Label versionLabel = new Label(content, SWT.NONE);
        versionLabel.setBackground(white);
        versionLabel.setText("Version: " + version);
        versionLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
        // Spacer
        Label spacer = new Label(content, SWT.NONE);
        spacer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        spacer.setBackground(white);
        // Close button
        Button closeButton = new Button(content, SWT.PUSH);
        closeButton.setText("Close");
        closeButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
        closeButton.addListener(SWT.Selection, e -> shell.close());
        shell.pack();
        shell.setLocation(
            parent.getLocation().x + (parent.getSize().x - shell.getSize().x) / 2,
            parent.getLocation().y + (parent.getSize().y - shell.getSize().y) / 2
        );
        // Below is absolutely necessary.
        shell.open();
        // Need another message loop.
        Display display = parent.getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

    }
}