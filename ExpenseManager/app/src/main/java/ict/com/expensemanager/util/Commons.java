package ict.com.expensemanager.util;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by PHAMHOAN on 1/18/2018.
 */

public final class Commons {

    private Commons() {

    }

    public static void setSizeDialog(Dialog dialog) {
        Window window = dialog.getWindow();
        Point size = new Point();
        if (window != null) {
            Display display = window.getWindowManager().getDefaultDisplay();
            display.getSize(size);

            window.setLayout(size.x, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
        }


    }

    public static void setSizeDialog(Dialog dialog, double height) {
        Window window = dialog.getWindow();
        Point size = new Point();
        if (window != null) {
            Display display = window.getWindowManager().getDefaultDisplay();
            display.getSize(size);

            window.setLayout(size.x, (int) (size.y * height));
            window.setGravity(Gravity.CENTER);
        }


    }



    public static long convertMoneyDoubleToLong(double number) {
        return (long) number;
    }




    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(calendar.getTime());
    }



    public static long convertStringDateToLong(String stringDate) {

        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        long milli = 0;
        try {
            Date d = f.parse(stringDate);
            milli = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return milli;
    }

}
