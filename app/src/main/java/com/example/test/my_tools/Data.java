package com.example.test.my_tools;

import android.graphics.Bitmap;

public class Data {
    // 定义一个静态的二维数组，初始值为空
    public static final String str[][] = new String[2][2];
    // 获取 str 数组
    public String[][] getStr() {
        return str;
    }

    public static int id;

    public static String username;

    public static  String[] first;
    public static  String[] second;

    public static void setId(int newStr) {
        id=newStr;
    }

    public static String Score_Scl_90;

    public static void setScore_Scl_90(String Sc_Scl_90) {
        Score_Scl_90=Sc_Scl_90;
    }

    public static String Score_SDS;

    public static void setScore_SDS(String Sc_SDS) {
        Score_SDS=Sc_SDS;
    }

    private static Bitmap profileImage;

    // 修改 str 数组中的内容
    public void setStr(String[][] newStr) {
        // 修改 str 数组的内容
        if (newStr != null && newStr.length == str.length) {
            for (int i = 0; i < str.length; i++) {
                System.arraycopy(newStr[i], 0, str[i], 0, str[i].length);
            }
        }
    }

    // 设置用户头像
    public static void setProfileImage(Bitmap newProfileImage) {
        profileImage = newProfileImage;
    }

    // 获取用户头像
    public static Bitmap getProfileImage() {
        return profileImage;
    }
}
