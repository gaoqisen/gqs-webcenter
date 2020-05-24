package com.github.gaoqisen.webcenter.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestUtils {

    public static String getDigest(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md5.update(str.getBytes());
        BigInteger bigInt = new BigInteger(1, md5.digest());
        String resultStr = bigInt.toString(16);
        return resultStr;
    }

}
