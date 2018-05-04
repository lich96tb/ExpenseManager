package ict.com.expensemanager.ui.signin;


import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ict.com.expensemanager.data.preferences.SharePreferencesManager;
import ict.com.expensemanager.ui.register.SignUp;
import ict.com.expensemanager.ui.slide_menu.Home;
import ict.com.expensemanager.R;
import ict.com.expensemanager.data.database.AppDatabase;
import ict.com.expensemanager.util.AppKey;

public class SignIn extends Fragment implements View.OnClickListener {
    TextView txtDangKy;
    Button btnDangNhap;
    CheckBox chkSignInAutoSignIn;
    @BindView(R.id.edtSignInName)
    EditText edtName;
    @BindView(R.id.edtSignInPassWord)
    EditText edtPassWord;
    Unbinder unbinder;
    String name;
    String pass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        init(view);
        return view;
    }

    private void init(View view) {
        txtDangKy = view.findViewById(R.id.txtSignInSignUp);
        btnDangNhap = view.findViewById(R.id.btnSignIn);
        chkSignInAutoSignIn = view.findViewById(R.id.chkSignInAutoSignIn);
        btnDangNhap.setOnClickListener(this);
        txtDangKy.setOnClickListener(this);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onClick(View view) {
        name = edtName.getText().toString().trim();
        pass = edtPassWord.getText().toString().trim();
        switch (view.getId()) {
            case R.id.btnSignIn:
                if (name.equals("")&&pass.equals("")){
                    Toast.makeText(getContext(), "Xin vui lòng nhập tên và mật khẩu", Toast.LENGTH_SHORT).show();
                }
                else if (chkSignInAutoSignIn.isChecked()) {
                    SaveData();
                    access();


                } else {
                    access();
                }

                break;
            case R.id.txtSignInSignUp:
                Fragment fragment = new SignUp();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.FragmentMain, fragment);
                fragmentTransaction.commit();
                break;
        }

    }

    private void access() {
        final AppDatabase db = Room.databaseBuilder(getContext(), AppDatabase.class, AppKey.NAME_DATABASE).build();
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                db.userDao().loadAllUsers();
                if (db.userDao().SignInSql(name, pass).size() != 0) {
                    Intent intent = new Intent(getContext(), Home.class);
                    Bundle bundle = new Bundle();
                    int id =db.userDao().SignInSql(name, pass).get(0).getIdUser();
                    bundle.putInt(AppKey.KEY_USER_ID,id );
                    SharedPreferences sharedPreferences = SharePreferencesManager.getInstance(getActivity().getApplicationContext());
                    bundle.putString(AppKey.USER_NAME, db.userDao().SignInSql(name, pass).get(0).getUserName());
                    bundle.putString(AppKey.USER_PASS, db.userDao().SignInSql(name, pass).get(0).getPassword());
                        sharedPreferences.edit().putInt(AppKey.KEY_USER_ID,id).apply();
                    Log.d("IDUSER", "run: "+id);

                    intent.putExtra(AppKey.KEY_BUNDLE,bundle);
                    startActivity(intent);
                }
                else if (db.userDao().CheckName(name).size()!=0){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                   getActivity().runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           Toast.makeText(getContext(), "Tài khoản không tồn tại.", Toast.LENGTH_SHORT).show();
                       }
                   });
                }
            }
        });
        thread.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void SaveData() {
        SharedPreferences sharedPreferences = SharePreferencesManager.getInstance(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AppKey.USER_NAME, name);
        editor.putString(AppKey.USER_PASS, pass);
        editor.commit();
    }



    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(AppKey.PREFERENCES_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(AppKey.USER_NAME, "") != "") {
            display();
            name=edtName.getText().toString();
            pass=edtPassWord.getText().toString();
            access();
        }
    }

    private void display() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(AppKey.PREFERENCES_NAME, Context.MODE_PRIVATE);
        edtName.setText(sharedPreferences.getString(AppKey.USER_NAME, ""));
        edtPassWord.setText(sharedPreferences.getString(AppKey.USER_PASS, ""));
    }
}
