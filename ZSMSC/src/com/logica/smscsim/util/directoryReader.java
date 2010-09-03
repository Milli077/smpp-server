/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.logica.smscsim.util;

import java.io.File;

/**
 *
 * @author Przetyczce
 */
public class directoryReader {
    private String dirName;
    private File directory;
    private File[] files;
    private String Counter;
    private String last = null;

    public directoryReader(String dirName) {
        this.dirName = dirName;
        directory = new File(this.dirName);
        if(directory.exists() && directory.isDirectory()){
            this.files = directory.listFiles();
            if(this.files.length>0){
                this.last = this.files[this.files.length-1].getName();
            }
        }
    }

    public void printListFiles() {
        for(int i=0; i<files.length; i++) {
            System.out.println(files[i]);
        }
    }
   
    public String getCounterPattern(String dateString) {
        if(this.last == null) {
            this.Counter = "000001";
        }
        return this.Counter;
    }




}
