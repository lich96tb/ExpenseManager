package ict.com.expensemanager.ui.base;

import android.content.Context;
import android.support.v4.app.DialogFragment;

import ict.com.expensemanager.data.database.AppDatabase;

/**
 * Created by nguyenanhtrung on 22/01/2018.
 */

public class BaseDialogFragment extends DialogFragment {

    private BaseActivity baseActivity;
    private AppDatabase appDatabase;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof BaseActivity) {
            baseActivity = (BaseActivity) context;
            appDatabase = baseActivity.getAppDatabase();
        }

    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }

    public void showMessage(String message) {
        if (baseActivity != null) {
            baseActivity.showMessage(message);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        baseActivity = null;
    }


}
