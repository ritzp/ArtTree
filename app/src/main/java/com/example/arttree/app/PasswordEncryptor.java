package com.example.arttree.app;

import java.security.MessageDigest;

public class PasswordEncryptor {
    public static String encrypt(String str){
        String MD5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes("UTF-8"));
            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();
            for(int i = 0 ; i < byteData.length ; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            MD5 = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            MD5 = null;
        }
        return MD5;
    }
}
