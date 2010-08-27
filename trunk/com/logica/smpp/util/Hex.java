/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.logica.smpp.util;

/**
 *
 * @author X
 */
public class Hex {
    public static String byteArrayToHexString(byte in[]) {
        byte ch = 0x00;
        int i = 0;
        if (in == null || in.length <= 0)
            return null;

        String pseudo[] = {"0", "1", "2",
                "3", "4", "5", "6", "7", "8",
                "9", "A", "B", "C", "D", "E",
                "F"};

        StringBuffer out = new StringBuffer(in.length * 2);

        while (i < in.length) {
            ch = (byte) (in[i] & 0xF0); // Strip off high nibble
            ch = (byte) (ch >>> 4); // shift the bits down

            ch = (byte) (ch & 0x0F);    // must do this is high order bit is on!

            out.append("%"); // convert the nibble to a String Character
            out.append(pseudo[ (int) ch]); // convert the nibble to a String Character

            ch = (byte) (in[i] & 0x0F); // Strip off low nibble

            out.append(pseudo[ (int) ch]); // convert the nibble to a String Character

            i++;

        }

        String rslt = new String(out);
        return rslt;
    }

    public static String encodeHexString(String sourceText) {

        byte[] rawData = sourceText.getBytes();
        StringBuffer hexText= new StringBuffer();
        String initialHex = null;
        int initHexLength=0;

        for(int i=0; i<rawData.length; i++) {
            int positiveValue = rawData[i] & 0x000000FF;
            initialHex = Integer.toHexString(positiveValue);
            initHexLength=initialHex.length();
            while(initHexLength++ < 2) {
                hexText.append("0");
            }

            hexText.append(initialHex);
        }
        return hexText.toString();
    }

    public static String decodeHexString(String hexText) {
        String decodedText=null;
        String chunk=null;

        if(hexText!=null && hexText.length()>0) {
            int numBytes = hexText.length()/2;
            byte[] rawToByte = new byte[numBytes];
            int offset=0;
            int bCounter=0;
            for(int i =0; i <numBytes; i++) {
                chunk = hexText.substring(offset,offset+2);
                offset+=2;
                rawToByte[i] = (byte) (Integer.parseInt(chunk,16) & 0x000000FF);
            }
            decodedText= new String(rawToByte);
        }
        return decodedText;
    }
}
