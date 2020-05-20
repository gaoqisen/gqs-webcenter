package com.gaoqisen.webcenter.utils;

import java.util.Random;

public class SecretKeyUtils {

    //获取密匙方法
    public static String getKeyCreate() {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        return random(base, 32);
    }

    private static String random(String base, Integer size) {
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random();
        // 生成32位数的随机秘钥字符串
        for (int i = 0; i < size; i++) {
            int number = random.nextInt(base.length());
            stringBuffer.append(base.charAt(number));
        }
        return stringBuffer.toString();
    }

    public static String getClientId() {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return random(base, 12);
    }

}
