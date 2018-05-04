package ict.com.expensemanager.ui.custom;

/**
 * Created by nguyenanhtrung on 24/01/2018.
 */

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by aldo on 21/08/16.
 */
public class AutoFormatUtil {

    private static final String FORMAT_NO_DECIMAL = "###,###";

    private static final String FORMAT_WITH_DECIMAL = "###,###.###";

    public static int getCharOccurance(String input, char c) {
        int occurance = 0;
        char[] chars = input.toCharArray();
        for (char thisChar : chars) {
            if (thisChar == c) {
                occurance++;
            }
        }
        return occurance;
    }

    public static String extractDigits(String input) {
        return input.replaceAll("\\D+", "");
    }

    public static String formatToStringWithoutDecimal(double value) {
        NumberFormat formatter = new DecimalFormat(FORMAT_NO_DECIMAL, getDecimalFormatSymbol());
        return formatter.format(value);
    }

    public static DecimalFormatSymbols getDecimalFormatSymbol() {
        Locale locale = new Locale("vi");
        return new DecimalFormatSymbols(locale);
    }

    public static String formatToStringWithoutDecimal(String value) {
        return formatToStringWithoutDecimal(Double.parseDouble(value));
    }

    public static String formatWithDecimal(String price) {
        return formatWithDecimal(Double.parseDouble(price));
    }

    public static String formatWithDecimal(double price) {
        NumberFormat formatter = new DecimalFormat(FORMAT_WITH_DECIMAL, getDecimalFormatSymbol());
        return formatter.format(price);
    }
}
