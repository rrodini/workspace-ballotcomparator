package com.rodini;

import com.sun.jna.platform.win32.User32;
import static com.sun.jna.platform.win32.WinUser.SM_CXSCREEN;
import static com.sun.jna.platform.win32.WinUser.SM_CYSCREEN;

//import com.sun.jna.*;

/** Simple example of JNA interface mapping and usage. 
 * Use the C library API "printf"
 * */

public class GetScreenSize {

    // This is the standard, stable way of mapping, which supports extensive
    // customization and mapping of Java to native types.

	

    
    public static void main(String[] args) {
    	int xWidth = User32.INSTANCE.GetSystemMetrics(SM_CXSCREEN);
    	int yHeight = User32.INSTANCE.GetSystemMetrics(SM_CYSCREEN);
    	
    	System.out.printf("Primary monitor: width: %d height: %d%n", xWidth, yHeight);
    }
}