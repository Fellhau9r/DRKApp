package com.example.d062434.drkapp.helper;

import java.util.ArrayList;

/**
 * Created by D062434 on 02.11.2015.
 */
public class StringOperator {

    public static String[] extractBetweenSpaces(String data){
        String[] rueck = data.split(";", -1);
        return rueck;
    }
}
