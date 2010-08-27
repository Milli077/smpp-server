/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.logica.smscsim.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author przetyczce
 */
public class ConfReader {

    private String spoolMODir;
    private String spoolMTDir;
    private String fileName;

    public ConfReader(String fileName) throws FileNotFoundException, IOException {
        //check for config file and get the MODir and MTDir
        this.fileName = fileName;
        if(checkConf()){
            readConfig();
        } else {
            System.out.println("Config File not Found");
            System.exit(0);
        }

    }

    private boolean checkConf() {
        File file = new File(this.fileName);
        try {
            FileInputStream fis = new FileInputStream(file);
            fis.close();
            return true;
        } catch (FileNotFoundException ex) {
            return false;
        }  catch (IOException ex) {
            return false;
        }
    }

    public String getSpoolMODir() {
        return this.spoolMODir;
    }

    public String getSpoolMTDir() {
        return this.spoolMTDir;
    }

    private void readConfig() throws FileNotFoundException, IOException {
        String temp;
        File file = new File(this.fileName);
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        DataInputStream dis = new DataInputStream(bis);
        while(dis.available() !=0 ) {
            temp = dis.readLine();
                String removed = temp.replace(" ", "");
                if(!removed.startsWith("#")) {
                    if(removed.toLowerCase().contains("modir")){
                        this.spoolMODir = removed.split("=")[1];
                    }
                    if(removed.toLowerCase().contains("mtdir")){
                        this.spoolMTDir = removed.split("=")[1];
                    }
                }
            
        }
        dis.close();
        bis.close();
        fis.close();
    }

}
