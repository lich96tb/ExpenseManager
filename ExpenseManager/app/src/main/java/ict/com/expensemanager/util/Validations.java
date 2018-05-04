package ict.com.expensemanager.util;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

import ict.com.expensemanager.ui.custom.AutoFormatEditText;


/**
 * Created by nguyenanhtrung on 22/01/2018.
 */

public final class Validations {

    private Validations() {
    }

    public static boolean isTransactionNameValid(EditText editTextName, TextInputLayout inputLayout) {

        if (editTextName.getText().toString().isEmpty()) {
            inputLayout.setError("Tên giao dịch đang trống, mời nhập lại!");
            return false;
        }

        if (editTextName.getText().length() > 100) {
            inputLayout.setError("Tên giao dịch đã vượt quá 100 kí tự, mời nhập lại!");
            return false;
        }

        if (editTextName.getText().length() < 3) {
            inputLayout.setError("Tên giao dịch phải có độ dài ít nhất là 3 kí tự");
            return false;
        }

        return true;
    }


    public static boolean isTransactionPriceValid(AutoFormatEditText editTextPrice, TextInputLayout inputLayout) {

        String price = editTextPrice.getText().toString();

        if (price.isEmpty()) {
            inputLayout.setError("Giá giao dịch đang trống, mời nhập lại!");
            return false;
        }

        if (price.length() > 15) {
            inputLayout.setError("Giá giao dịch đã vượt quá 15 chữ số, mời nhập lại!");
            return false;
        }

        if (Long.parseLong(price.replace(".","")) == 0) {
            inputLayout.setError("Giá giao dịch phải lớn hơn 0, mời nhập lại!");
            return false;
        }

        if (price.length() < 3) {
            inputLayout.setError("Giá giao dịch phải có ít nhất  3 chữ số trở lên!");
            return false;
        }


        return true;
    }


}
