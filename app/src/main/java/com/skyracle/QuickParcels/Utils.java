package com.skyracle.QuickParcels;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


public class Utils {
    static Bitmap photoBitmap;
    public static String imagePath = "";
    public static String encodedbar = "encodedbar";
    public static String imageup = "";
    private final static String PREF_KEY = "My_Pref";
    public static String numone = "0";

    public static boolean savePreferences(Context c, String key, String value) {
        SharedPreferences sp = initializeSharedPreferences(c);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static String getSavedPreferences(Context c, String key, String defValue) {
        SharedPreferences sp = initializeSharedPreferences(c);
        return sp.getString(key, defValue);
    }

    private static SharedPreferences initializeSharedPreferences(Context c) {
        return c.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
    }

    public static boolean clearSavedPreferences(Context c, String key) {
        SharedPreferences sp = initializeSharedPreferences(c);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        return editor.commit();
    }
    public static void write(String msg) {
        try {

            System.out.println(msg);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            if (activity != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) activity
                        .getSystemService(Activity.INPUT_METHOD_SERVICE);
                View v = activity.getCurrentFocus();
                if (v != null) {
                    IBinder binder = activity.getCurrentFocus()
                            .getWindowToken();
                    if (binder != null) {
                        inputMethodManager.hideSoftInputFromWindow(binder, 0);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}