package ict.com.expensemanager.ui.register;


import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ict.com.expensemanager.R;
import ict.com.expensemanager.data.database.AppDatabase;
import ict.com.expensemanager.data.database.entity.User;
import ict.com.expensemanager.ui.signin.SignIn;
import ict.com.expensemanager.util.AppKey;


public class SignUp extends Fragment {
    TextInputLayout txt;

    @BindView(R.id.edtSignUpName)
    EditText edtSignUpName;
    @BindView(R.id.edtSignUpPassWord)
    EditText edtSignUpPassWord;
    @BindView(R.id.edtSignUpEnterThePassWord)
    EditText edtSignUpEnterThePassWord;
    @BindView(R.id.btnSignUpSignUp)
    Button btnSignUpSignUp;
    Unbinder unbinder;
    @BindView(R.id.txtSignUpSignIn)
    TextView txtSignUpSignIn;

    String signUpName, signUpPassWord, signUpEnterThePassWord;
    private static final String mau = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).(?=.*[!@#$%^&*()_=]).{5,})";
    @BindView(R.id.txtSignUpError)
    TextView txtSignUpError;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        unbinder = ButterKnife.bind(this, view);
        txtSignUpError.setVisibility(View.GONE);
        return view;
    }


    private void signIn() {
        SignIn signIn = new SignIn();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FragmentMain, signIn);
        fragmentTransaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btnSignUpSignUp, R.id.txtSignUpSignIn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSignUpSignUp:
                signUpName = edtSignUpName.getText().toString().trim();
                signUpPassWord = edtSignUpPassWord.getText().toString().trim();
                signUpEnterThePassWord = edtSignUpEnterThePassWord.getText().toString();


                if (signUpPassWord.equals(signUpEnterThePassWord) && signUpName != "" && signUpPassWord != "" && signUpPassWord.matches(mau)) {

                    final AppDatabase db = Room.databaseBuilder(getContext(),
                            AppDatabase.class, AppKey.NAME_DATABASE).build();
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (db.userDao().CheckName(signUpName).size() == 0) {
                                db.userDao().insertUser(new User(signUpName, signUpPassWord));
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "Thêm tài khoản thành công", Toast.LENGTH_SHORT).show();
                                        SignIn signIn = new SignIn();
                                        FragmentManager fragmentManager = getFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.replace(R.id.FragmentMain, signIn);
                                        fragmentTransaction.commit();
                                    }
                                });
                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "trùng tên với tài khoản có sẵn", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }
                    });
                    thread.start();
                } else if (signUpName.trim().equals("") || signUpPassWord.trim().equals("") || signUpEnterThePassWord.trim().equals("")) {
                    Toast.makeText(getContext(), "Xin nhập đủ dữ liệu", Toast.LENGTH_SHORT).show();
                } else if (signUpPassWord.equals(signUpEnterThePassWord) == false) {
                    Toast.makeText(getContext(), "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                } else if (signUpPassWord.matches(mau) == false) {
                    Toast.makeText(getContext(), "Mật khẩu không đúng quy chuẩn", Toast.LENGTH_SHORT).show();
                    txtSignUpError.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.txtSignUpSignIn:
                signIn();
                break;
        }
    }
}
