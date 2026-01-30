package com.rodini;

//import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT.HANDLE;
//import com.sun.jna.platform.win32.Winsvc;
import com.sun.jna.platform.win32.WinBase.PROCESS_INFORMATION;
import com.sun.jna.platform.win32.WinBase.STARTUPINFO;
import com.sun.jna.platform.win32.WinDef.WORD;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.Win32Exception;
//import com.sun.jna.platform.win32.WinError;

public class JNALaunchProcess {

    public static void main(String[] args) {
        String commandLine = "notepad.exe"; // Command to execute

        // Initialize STARTUPINFO and PROCESS_INFORMATION structures
        STARTUPINFO startupInfo = new STARTUPINFO();
        PROCESS_INFORMATION processInfo = new PROCESS_INFORMATION();

        // Set the size of the structures (a common Win32 requirement)
//      startupInfo.dwFlags = new DWORD(WinBase.STARTF_USESHOWWINDOW);
        startupInfo.dwFlags = WinBase.STARTF_USESHOWWINDOW;
//      startupInfo.wShowWindow = WinUser.SW_SHOWNORMAL;
        startupInfo.wShowWindow = new WORD(WinUser.SW_SHOWNORMAL);
        
        boolean success = Kernel32.INSTANCE.CreateProcess(
            null,               // lpApplicationName
            commandLine,        // lpCommandLine (must be a mutable buffer/pointer if not null)
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
                System.out.println("Process started successfully!");
                System.out.println("Process ID: " + processInfo.dwProcessId.intValue());
                System.out.println("Main thread ID: " + processInfo.dwThreadId.intValue());

                // Wait for the process to finish
                HANDLE processHandle = processInfo.hProcess;
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
