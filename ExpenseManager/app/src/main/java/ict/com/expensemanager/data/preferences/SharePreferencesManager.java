package ict.com.expensemanager.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import ict.com.expensemanager.util.AppKey;

/**
 * Created by nguyenanhtrung on 26/01/2018.
 */

public class SharePreferencesManager {

    private static SharedPreferences sharedPreferences;


    public SharePreferencesManager() {

    }


    public static SharedPreferences getInstance(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(AppKey.PREFERENCES_NAME, Context.MODE_PRIVATE);
        }

        return sharedPreferences;
    }




}
