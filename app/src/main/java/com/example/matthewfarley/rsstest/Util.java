package com.example.matthewfarley.rsstest;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by matthewfarley on 17/06/15.
 */
public class Util {

    public static String base64StringFromString(String startingString){
        byte[] data = null;
        try {
            data = startingString.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return Base64.encodeToString(data, Base64.NO_WRAP | Base64.NO_PADDING);
    }

    public static String StringFromBase64String(String base64String){
        byte[] data1 = Base64.decode(base64String, Base64.NO_WRAP | Base64.NO_PADDING);
        String result = null;
        try {
            result = new String(data1, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
