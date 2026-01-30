package com.rodini;

//import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT.HANDLE;
//import com.sun.jna.platform.win32.Winsvc;
import com.sun.jna.platform.win32.WinBase.PROCESS_INFORMATION;
import com.sun.jna.platform.win32.WinBase.STARTUPINFO;
import static com.sun.jna.platform.win32.WinBase.STARTF_USESHOWWINDOW;
import static com.sun.jna.platform.win32.WinBase.STARTF_USEPOSITION;
import static com.sun.jna.platform.win32.WinBase.STARTF_USESIZE;
import static com.sun.jna.platform.win32.WinUser.SWP_SHOWWINDOW;
import static com.sun.jna.platform.win32.WinBase.INFINITE;
import static com.sun.jna.platform.win32.WinNT.BOOLEAN_TRUE;
import com.sun.jna.platform.win32.WinDef.WORD;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.Win32Exception;
//import com.sun.jna.platform.win32.WinError;

public class LaunchWordWindowSize {

    public static void main(String[] args) throws InterruptedException {
        String commandLine = "c:\\Program Files\\Microsoft Office\\root\\Office16\\winword.exe"; // Command to execute
        String docFolder = "C:\\Users\\rrodi\\Documents\\BallotComparator\\workspace-experiment\\launch-word-window-size";
        String docName = "005_ATGLEN.docx";
        String commandLineArgs = docFolder + "\\" + docName; // Word args[0]
//        String commandLine = "notepad.exe"; // Command to execute
        String windowTitle = "005_ATGLEN.docx - Word";

        // Initialize STARTUPINFO and PROCESS_INFORMATION structures
        STARTUPINFO startupInfo = new STARTUPINFO();
        // https://java-native-access.github.io/jna/5.18.1/javadoc/com/sun/jna/platform/win32/WinBase.STARTUPINFO.html
        // dwFlags = STARTF_USEPOSITION | STARTF_USESIZE
        // dwX - X coordinate
        // dwXSize - X width in pixels
        // dwY - Y coordinate
        // dwYSize - Y height in pixels
//        startupInfo.dwFlags = STARTF_USEPOSITION | STARTF_USESIZE;
//        startupInfo.dwX = new DWORD(00);
//        startupInfo.dwXSize = new DWORD(300);
//        startupInfo.dwY = new DWORD(00);
//        startupInfo.dwYSize = new DWORD(300);
        PROCESS_INFORMATION processInfo = new PROCESS_INFORMATION();
        String commandLineWithArgs = commandLine + " " + commandLineArgs;
        System.out.println("commandLineWithArgs: " + commandLineWithArgs);
        // Set the size of the structures (a common Win32 requirement)
//      startupInfo.dwFlags = new DWORD(WinBase.STARTF_USESHOWWINDOW);
        startupInfo.dwFlags = WinBase.STARTF_USESHOWWINDOW;
//      startupInfo.wShowWindow = WinUser.SW_SHOWNORMAL;
        startupInfo.wShowWindow = new WORD(WinUser.SW_SHOWNORMAL);
        boolean success = Kernel32.INSTANCE.CreateProcess(
            null,               // lpApplicationName
            commandLineWithArgs,// lpCommandLine (must be a mutable buffer/pointer if not null)
            null,               // lpProcessAttributes
            null,               // lpThreadAttributes
            false,              // bInheritHandles
            new DWORD(0),       // dwCreationFlags
            Pointer.NULL,       // lpEnvironment
            null,               // lpCurrentDirectory
            startupInfo,        // lpStartupInfo
            processInfo         // lpProcessInformation
        );

        if (!success) {
            int errorCode = Kernel32.INSTANCE.GetLastError();
            System.err.println("Failed to create process. Error code: " + errorCode);
            throw new RuntimeException("CreateProcess failed with error: " + new Win32Exception(errorCode).getMessage());
        } else {
            try {
            	int errorCode;
                System.out.println("Process started successfully!");
                System.out.println("Process ID: " + processInfo.dwProcessId.intValue());
                System.out.println("Main thread ID: " + processInfo.dwThreadId.intValue());

                HANDLE processHandle = processInfo.hProcess;
                System.out.println("Process handle: " + processHandle);
                System.out.println("Before sleep.");
                // Let go of processor, so app can display window.
                Thread.sleep(5000);
                System.out.println("After  sleep.");
                // Get handle to main window.
                HWND windowHandle = User32.INSTANCE.FindWindow(null, windowTitle);
                
                System.out.println("Window handle: " + windowHandle);
                if (windowHandle == null) {
                	System.err.println("Could not find window handle.");
                	errorCode = Kernel32.INSTANCE.GetLastError();
                    System.err.println("Error code finding window handle: " + errorCode);
                } else {
                	boolean result = User32.INSTANCE.MoveWindow(windowHandle,10, 10, 300, 400,  true);
//                	result = User32.INSTANCE.SetWindowPos(windowHandle, null, 10, 10, 300, 400, SWP_SHOWWINDOW);
//                	errorCode = Kernel32.INSTANCE.GetLastError();
//                    System.err.println("Failed to move window. Error code: " + errorCode);
                	if (!result) {
                    	System.err.println("Could not move window.");
                    	errorCode = Kernel32.INSTANCE.GetLastError();
                        System.err.println("Failed to move window. Error code: " + errorCode);
                	}
                }
                // Wait for the process to finish
                Kernel32.INSTANCE.WaitForSingleObject(processHandle, WinBase.INFINITE);
                System.out.println("Process finished.");

                // Get the exit code
//                DWORD exitCode = new DWORD(0);
                IntByReference exitCode = new IntByReference(0);
                Kernel32.INSTANCE.GetExitCodeProcess(processHandle, exitCode);
                System.out.println("Exit Code: " + exitCode.getValue());

                // Close handles
                Kernel32.INSTANCE.CloseHandle(processInfo.hProcess);
                Kernel32.INSTANCE.CloseHandle(processInfo.hThread);
            } finally {
                // Ensure handles are closed even if an exception occurs
                if (processInfo.hProcess != null) {
                    Kernel32.INSTANCE.CloseHandle(processInfo.hProcess);
                }
                if (processInfo.hThread != null) {
                    Kernel32.INSTANCE.CloseHandle(processInfo.hThread);
                }
            }
        }
    }
}
