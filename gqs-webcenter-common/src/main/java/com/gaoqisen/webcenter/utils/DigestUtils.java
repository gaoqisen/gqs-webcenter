package com.gaoqisen.webcenter.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestUtils {

    // md5摘要
    public static String getDigest(String str) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes());
            BigInteger bigInt = new BigInteger(1, md5.digest());
            String resultStr = bigInt.toString(16);
            return resultStr;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
