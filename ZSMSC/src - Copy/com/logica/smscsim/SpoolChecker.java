/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.logica.smscsim;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author X
 */
public class SpoolChecker implements Runnable {
    private boolean isrun = true;

    public void run() {
        while(isrun) {
            try {
                System.out.println("Resting 5 seconds");
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SpoolChecker.class.getName()).log(Level.SEVERE, null, ex);
            }

            File folder = new File(Simulator.instance().spoolMODir);
            //File folder = new File("d:\\spool\\");
            File[] listOfFiles = folder.listFiles();

            System.out.println("There's "+listOfFiles.length+" file(s)");

            for (int i = 0; i < listOfFiles.length; i++) {
              if (listOfFiles[i].isFile()) {
                  System.out.println("Processing "+listOfFiles[i].getName());
                    File file = listOfFiles[i];
                    FileInputStream fis = null;
                    BufferedInputStream bis = null;
                    DataInputStream dis = null;
                    String content = null;

                    try {
                      fis = new FileInputStream(file);

                      // Here BufferedInputStream is added for fast reading.
                      bis = new BufferedInputStream(fis);
                      dis = new DataInputStream(bis);

                      // dis.available() returns 0 if the file does not have more lines.
                      while (dis.available() != 0) {

                      // this statement reads the line from the file and print it to
                        // the console.
                        //System.out.println(dis.readLine());
                          content = dis.readLine();
                      }

                      // dispose all the resources after using them.
                      fis.close();
                      bis.close();
                      dis.close();

                    } catch (FileNotFoundException e) {
                      e.printStackTrace();
                    } catch (IOException e) {
                      e.printStackTrace();
                    }

                    if(content!=null) {
                        System.out.println("Content : "+content);
                        StringTokenizer st = new StringTokenizer(content, "|");
                        String toSMPP = null;
                        String shortCode = null;
                        String msisdn = null;
                        String msg = null;

                        if(st.hasMoreTokens())
                            toSMPP = st.nextToken();

                        if(st.hasMoreTokens())
                            shortCode = st.nextToken();

                        if(st.hasMoreTokens())
                            msisdn = st.nextToken();

                        if(st.hasMoreTokens())
                            msg = st.nextToken();

                        try {
                            if(toSMPP!=null && msg!=null && shortCode!=null && msisdn!=null) {
                                System.out.println("Send message");
                                String ret = Simulator.instance().sendMessage(toSMPP, shortCode, msisdn, msg);
                                System.out.println("Send message : "+ret);
                            }
                            listOfFiles[i].delete();
                        } catch (IOException ex) {
                            Logger.getLogger(SpoolChecker.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
              }
            }
        }
    }

}
