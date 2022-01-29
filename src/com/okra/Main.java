package com.okra;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static int n         = 1;
    private static int batchSize = 0;
    private static int delay     = 0;
    final static String addressFile = "address_list.txt";
    final static String configFile  = "config.txt";
    static String[] settings = new String[7];
    static ArrayList<String> dropList = new ArrayList<>();



    public static void main(String[] args) throws IOException, InterruptedException {

           try {
               Scanner scanner = new Scanner(new File(configFile));
               scanner.nextLine();
               String set = scanner.next();
               settings = set.split(",");
           } catch (FileNotFoundException fe) {
               System.out.println("ERROR NO CONFIG FILE FOUND!");
               System.exit(1);
           } catch (Exception e) {
               System.out.println("Config Load Error!");
               e.printStackTrace();
               System.exit(1);
           }
           Send sConfig = new Send(
                   settings[0],
                   settings[1],
                   settings[2],
                   settings[3],
                   settings[4]
                   );
           batchSize = Integer.parseInt(settings[5]);
           delay = Integer.parseInt(settings[6]);

       System.out.println("Config Loaded.");

       try (BufferedReader reader = new BufferedReader(new FileReader(addressFile))) {
           while (reader.ready()) {
               dropList.add(reader.readLine());
            }
        } catch (FileNotFoundException e) {
           System.out.println("ERROR NO ADDRESS FILE FOUND!");
           System.exit(1);
       }

        System.out.println("Addresses Loaded.\nCommencing drop.");

        for(String s: dropList) {
            if (n % batchSize == 0) Thread.sleep(delay);
            Send send = new Send(s);
            boolean rtn = send.drop();

          if (rtn == false) {
              Logger.failed(s);
              System.out.println("Failed on: " + s);
          }
          n++;
        }
        System.out.println("Reached end of address list! Drop Finished!\nPress Enter To Exit");
        System.in.read();
    }
}
