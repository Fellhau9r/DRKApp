package com.example.d062434.drkapp.data;

import java.util.ArrayList;

/**
 * Created by D062434 on 02.11.2015.
 */
public class Profile {
    private static String name;
    private static String vname;
    private static String email;
    private static String password;
    private static String id;
    private static ArrayList<String> rechteIds = new ArrayList<>();
    private static boolean not01 = true;
    private static boolean not02 = true;
    private static boolean not03 = true;
    private static boolean not04 = true;
    private static double screenDimension;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        Profile.name = name;
    }

    public static String getVname() {
        return vname;
    }

    public static void setVname(String vname) {
        Profile.vname = vname;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        Profile.email = email;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        Profile.password = password;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        Profile.id = id;
    }

    public static ArrayList<String> getRechteIds() {
        return rechteIds;
    }

    public static void setRechteIds(ArrayList<String> rechteIds) {
        Profile.rechteIds = rechteIds;
    }

    public static boolean isNot01() {
        return not01;
    }

    public static void setNot01(boolean not01) {
        Profile.not01 = not01;
    }

    public static boolean isNot02() {
        return not02;
    }

    public static void setNot02(boolean not02) {
        Profile.not02 = not02;
    }

    public static boolean isNot03() {
        return not03;
    }

    public static void setNot03(boolean not03) {
        Profile.not03 = not03;
    }

    public static boolean isNot04() {
        return not04;
    }

    public static void setNot04(boolean not04) {
        Profile.not04 = not04;
    }

    public static double getScreenDimension() {
        return screenDimension;
    }

    public static void setScreenDimension(double screenDimension) {
        Profile.screenDimension = screenDimension;
    }
}
