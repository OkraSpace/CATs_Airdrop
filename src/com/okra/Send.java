package com.okra;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Send {

    private static String chiaPath    = "";
    private static String fingerprint = "";
    private static String walletID    = "";
    private static String amount      = "";
    private static String fee         = "";
    private String address            = "";

    public Send(String chiaPath, String fingerprint, String walletID, String amount, String fee) {
        this.chiaPath = chiaPath;
        this.fingerprint = fingerprint;
        this.walletID = walletID;
        this.amount = amount;
        this.fee = fee;
    }

    public Send(String address) {
        this.address = address;
    }

    private int send() throws FileNotFoundException {

    String[] commandList = {
            "cmd.exe",
            "/c",
            "chia",
            "wallet",
            "send",
            "-f",
            fingerprint,
            "-i",
            walletID,
            "-a",
            amount,
            "-m",
            fee,
            "-t",
            address,
    };

    ProcessBuilder processBuilder = new ProcessBuilder(commandList);
    processBuilder.directory(new File(chiaPath));

    ArrayList<String> output = new ArrayList();

    try {
        String response;

        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        while ((response = reader.readLine()) != null) {
            output.add(response);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

            for (String s: output) {
                if (s.contains("Exception")) {
                    if (s.contains("Invalid")) return -1;
                    if (s.contains("Can")) return 0;
                }
                if (s.contains("Transaction submitted"))
                    System.out.println(s);
                if(s.contains("Do")) {
                    Logger.success(address, s);
                    return 1;
                }
            }
            return -2;
    }

    public boolean drop() throws InterruptedException, FileNotFoundException {

        int rtn = send();

        if (rtn == 0) {
            for (int i = 0; i < 6; ++i) {
                System.out.println("Retrying send to: " + address);
                Thread.sleep(20000);
                if (send() == 1) return true;
                if (i == 5) return false;
            }
        }

        if (rtn == -1) return false;
        else if (rtn == 1) return true;

        return false;
    }
}
