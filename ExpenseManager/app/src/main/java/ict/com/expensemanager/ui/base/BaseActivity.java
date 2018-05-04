package ict.com.expensemanager.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import ict.com.expensemanager.data.database.AppDatabase;

/**
 * Created by nguyenanhtrung on 22/01/2018.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private AppDatabase appDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appDatabase = AppDatabase.getInstance(getApplicationContext());
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
