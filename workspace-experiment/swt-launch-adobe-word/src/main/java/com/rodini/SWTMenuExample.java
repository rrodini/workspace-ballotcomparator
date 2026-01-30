package com.rodini;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.ole.win32.*;
//import org.eclipse.swt.

public class SWTMenuExample {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Menu Example");
//        shell.setImage(new Image(display, "c:\\icons\\JavaCup.ico"));

		Menu m = new Menu(shell, SWT.BAR);

		// create a File menu and add an Exit item
		final MenuItem file = new MenuItem(m, SWT.CASCADE);
		file.setText("File");
		final Menu filemenu = new Menu(shell, SWT.DROP_DOWN);
		file.setMenu(filemenu);
		final MenuItem openItem = new MenuItem(filemenu, SWT.PUSH);
		openItem.setText("Open");
		final MenuItem separator = new MenuItem(filemenu, SWT.SEPARATOR);
		final MenuItem exitItem = new MenuItem(filemenu, SWT.PUSH);
		exitItem.setText("Exit");

		// create an Edit menu and add Cut, Copy, and Paste items
		final MenuItem edit = new MenuItem(m, SWT.CASCADE);
		edit.setText("Edit");
		final Menu editmenu = new Menu(shell, SWT.DROP_DOWN);
		edit.setMenu(editmenu);
		final MenuItem cutItem = new MenuItem(editmenu, SWT.PUSH);
		cutItem.setText("Cut");
		final MenuItem copyItem = new MenuItem(editmenu, SWT.PUSH);
		copyItem.setText("Copy");
		final MenuItem pasteItem = new MenuItem(editmenu, SWT.PUSH);
		pasteItem.setText("Paste");

		shell.setMenuBar(m);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}