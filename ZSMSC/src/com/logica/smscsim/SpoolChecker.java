/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.logica.smscsim;

import com.logica.smscsim.util.ConfReader;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author X
 */
public class SpoolChecker implements Runnable {
    private boolean isrun = true;
    private String spoolMODir;


    public SpoolChecker() {
        ConfReader conf;
        try {
            conf = new ConfReader(Simulator.usersFileName);
            //System.out.println(Simulator.usersFileName);
            if(conf.getSpoolMODir() != null) {
                this.spoolMODir = conf.getSpoolMODir();
            }
            
        } catch (FileNotFoundException ex) {
            this.spoolMODir = Simulator.instance().spoolMODir;
        } catch (IOException ex) {
            Logger.getLogger(SpoolChecker.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    private void copyfile(String srFile, String dtFile){
        try{
          File f1 = new File(srFile);
          File f2 = new File(dtFile);
          InputStream in = new FileInputStream(f1);

          //For Overwrite the file.
          OutputStream out = new FileOutputStream(f2);

          byte[] buf = new byte[1024];
          int len;
          while ((len = in.read(buf)) > 0){
            out.write(buf, 0, len);
          }
          in.close();
          out.close();
          System.out.println("File copied.");
        }
        catch(FileNotFoundException ex){
          System.out.println(ex.getMessage() + " in the specified directory.");
          System.exit(0);
        }
        catch(IOException e){
          System.out.println(e.getMessage());
        }
    }

    public void run() {
        while(isrun) {
            try {
                //System.out.println("Resting 5 seconds");
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SpoolChecker.class.getName()).log(Level.SEVERE, null, ex);
            }

            File folder = new File(this.spoolMODir);

            File[] listOfFiles = folder.listFiles();

            try {
                if(listOfFiles.length>0) {
                    //System.out.println("There's "+listOfFiles.length+" file(s)");

                    for (int i = 0; i < listOfFiles.length; i++) {
                        String[] temp = new String[5];
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
                              int counter = 0;
                              // dis.available() returns 0 if the file does not have more lines.
                              while (dis.available() != 0) {

                              // this statement reads the line from the file and print it to
                                // the console.
                                //System.out.println(dis.readLine());
                                  if(counter < temp.length) {
                                    temp[counter] = dis.readLine();
                                    counter++;
                                  }
                                  //content = dis.readLine();
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

                            if(temp[3] != null) {
                                System.out.println("content : "+temp[3]);
                                //StringTokenizer st = new StringTokenizer(content, "|");
                                String toSMPP = "Smsc2001";
                                String shortCode = temp[0];
                                String msisdn = temp[1];
                                String encoding = temp[2];
                                String msg = temp[3];

                                try {
                                    String ret = "";
                                    if(toSMPP!=null && msg!=null && shortCode!=null && msisdn!=null) {
                                        msisdn = msisdn.replaceAll("\\+", "");
                                        System.out.println("Send message from "+msisdn);
                                        ret = Simulator.instance().sendMessage(toSMPP, shortCode, msisdn, encoding, msg);
                                        System.out.println("Send message : "+ret);
                                    }

                                    if(ret.startsWith("Message sent"))
                                        listOfFiles[i].delete();
                                    else if(ret.startsWith("Message sending failed")) {
                                        copyfile(Simulator.instance().spoolMODir + listOfFiles[i].getName() , Simulator.instance().spoolMODir + "failed/" + listOfFiles[i].getName());
                                        listOfFiles[i].delete();
                                    }

                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                } catch(Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                      }
                    }
                }
            } catch(Exception e){
                //System.out.println("There's 0 file(s)");
            }
        }
    }

}
