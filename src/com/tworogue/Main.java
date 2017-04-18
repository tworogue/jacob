package com.tworogue;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.LibraryLoader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class Main {

    public static void main(String[] args) {

        // we register assembly preliminarily to create .NET classes transparently
        // run following code in windows CMD as administrator
        // regasm /verbose /nologo /codebase %PATH_TO_DLL%

        String libFile = "lib/";
        libFile += System.getProperty("os.arch").equals("amd64") ? "jacob-1.18-x64.dll" : "jacob-1.18-x86.dll";
        try {

            // now we create valid win-32 app from jacob's dll
            InputStream inputStream = new FileInputStream(libFile);
            File temporaryDll = File.createTempFile("jacob", ".dll");
            FileOutputStream outputStream = new FileOutputStream(temporaryDll);
            byte[] array = new byte[8192];
            for (int i = inputStream.read(array); i != -1; i = inputStream.read(array)) {
                outputStream.write(array, 0, i);
            }
            outputStream.close();

            // load jacob's dll
            System.setProperty(LibraryLoader.JACOB_DLL_PATH, temporaryDll.getAbsolutePath());
            LibraryLoader.loadJacobLibrary();

            // do some magic with PassportReaderSdk
            ActiveXComponent compProgramID = new ActiveXComponent(SdkPreferences.DLL_NAME);
            Dispatch.call(compProgramID, "Init");

            // clean up
            temporaryDll.deleteOnExit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}