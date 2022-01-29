package com.okra;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class Logger {

    public static void success(String address, String tID) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new FileOutputStream(new File("success.txt"), true));
        pw.append(address +"\n" + tID + "\n\n");
        pw.close();
    }

    public static void failed(String address) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new FileOutputStream(new File("failed.txt"), true));
        pw.append(address + "\n");
        pw.close();
    }
}
