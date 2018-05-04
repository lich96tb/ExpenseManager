package ict.com.expensemanager;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import ict.com.expensemanager.ui.signin.SignIn;


import ict.com.expensemanager.ui.event.EventFragment;

import android.view.View;
import android.widget.Button;

import ict.com.expensemanager.data.database.AppDatabase;
import ict.com.expensemanager.data.database.entity.User;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SignIn signIn = new SignIn();
        fragmentTransaction.add(R.id.FragmentMain, signIn);
        fragmentTransaction.commit();
    }

    }





