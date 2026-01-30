package com.rodini;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
//import com.sun.jna.*;

/** Simple example of JNA interface mapping and usage. 
 * Use the C library API "printf"
 * */

public class HelloWorld {

    // This is the standard, stable way of mapping, which supports extensive
    // customization and mapping of Java to native types.

    public interface CLibrary extends Library {
        CLibrary INSTANCE = (CLibrary)
            Native.load((Platform.isWindows() ? "msvcrt" : "c"),
                                CLibrary.class);

        void printf(String format, Object... args);
    }

    public interface Kernel32 extends Library { 
        // Method declarations, constant and structure definitions go here
    	Kernel32 INSTANCE = (Kernel32)
    		    Native.load("kernel32", Kernel32.class);
//    	Kernel32 SYNC_INSTANCE = (Kernel32)
//    		    Native.synchronizedLibrary(INSTANCE);
    	
    }
    
    public static void main(String[] args) {
        CLibrary.INSTANCE.printf("Hello, World\n");
        for (int i=0;i < args.length;i++) {
            CLibrary.INSTANCE.printf("Argument %d: %s\n", i, args[i]);
        }
    }
}