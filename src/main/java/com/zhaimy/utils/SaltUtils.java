package com.zhaimy.utils;

import java.util.Random;

public class SaltUtils {


    /**
     * 生成salt的静态方法
     * @param n salt的长度
     * @return
     */
    public static String getSalt(int n){
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%^&*()".toCharArray();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < n; i++) {
            char c = chars[new Random().nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }




    public static void main(String[] args) {
        System.out.println(SaltUtils.getSalt(10));
    }
}
