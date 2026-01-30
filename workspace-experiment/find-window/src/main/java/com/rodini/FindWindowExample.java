package com.rodini;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser;

public class FindWindowExample {

    public static void main(String[] args) {
        // The title of the window you want to find (e.g., "Untitled - Notepad")
        String windowTitle = "Untitled - Notepad"; 
        
        // Get an instance of the User32 interface
        User32 user32 = User32.INSTANCE;

        // Call FindWindow: 
        // The first parameter (lpClassName) is null to search by title only.
        // The second parameter (lpWindowName) is the window title string.
        HWND hWnd = user32.FindWindow(null, windowTitle);

        if (hWnd != null) {
            System.out.println("Window found! Handle: " + hWnd);

            // Optional: Restore the window if minimized and bring it to the front
            user32.ShowWindow(hWnd, WinUser.SW_RESTORE);
            user32.SetForegroundWindow(hWnd);
        } else {
            System.out.println("Window not found! Make sure an application with the title \"" + windowTitle + "\" is running.");
        }
    }
}
