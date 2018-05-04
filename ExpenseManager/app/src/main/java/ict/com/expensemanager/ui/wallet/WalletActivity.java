package ict.com.expensemanager.ui.wallet;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ict.com.expensemanager.R;
import ict.com.expensemanager.ui.category.AddCategoryFragment;

public class WalletActivity extends AppCompatActivity {
    private FragmentManager manager;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.Frame_Wallet, new AddWalletFragment());
        transaction.commit();
    }
}
