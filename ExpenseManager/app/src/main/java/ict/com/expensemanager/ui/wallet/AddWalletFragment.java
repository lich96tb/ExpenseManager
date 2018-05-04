package ict.com.expensemanager.ui.wallet;


import android.arch.persistence.room.Room;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ict.com.expensemanager.R;
import ict.com.expensemanager.data.database.AppDatabase;
import ict.com.expensemanager.data.database.entity.Wallet;
import ict.com.expensemanager.data.preferences.SharePreferencesManager;
import ict.com.expensemanager.ui.custom.AutoFormatEditText;
import ict.com.expensemanager.ui.slide_menu.Home;
import ict.com.expensemanager.util.AppKey;

public class AddWalletFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.image_back_add_wallet)
    ImageView imageBack;
    @BindView(R.id.text_save_wallet)
    TextView textSave;
    @BindView(R.id.edittext_add_wallet)
    EditText edittextWallet;
    @BindView(R.id.edittext_add_money)
    AutoFormatEditText edittextMoney;
    Unbinder unbinder;

    private int idUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_wallet, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        ((Home) getActivity()).homeToolbar.setVisibility(View.GONE);
        imageBack.setOnClickListener(this);
        textSave.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_save_wallet:
                addWallet();
                break;
            case R.id.image_back_add_wallet:

                getActivity().finish();
                break;
            default:
                break;
        }
    }

    private void addWallet() {
        AppDatabase db = Room.databaseBuilder(getContext(),
                AppDatabase.class, AppKey.NAME_DATABASE)
                .allowMainThreadQueries()
                .build();
        SharedPreferences preferences = SharePreferencesManager.getInstance(getActivity().getApplicationContext());
        idUser = preferences.getInt(AppKey.KEY_USER_ID,0);
        Log.d("TESTTTTT", "addWallet:" + edittextMoney.getText().toString() );
        String wallet = edittextWallet.getText().toString().trim();
        String money = edittextMoney.getText().toString().trim().replace(".", "").replace(",","");

        if (TextUtils.isEmpty(wallet) || TextUtils.isEmpty(money)) {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        } else {
            int checkWWallet = db.walletDao().getCountByName(wallet);
            if (checkWWallet != 0) {
                Toast.makeText(getActivity(), "Tên ví đã được sử dụng", Toast.LENGTH_SHORT).show();
            } else {
                double balance = Double.valueOf(money);
//                if(balance < 0 ) {
//                    Toast.makeText(getActivity(), "Số tiền còn lại không được âm", Toast.LENGTH_SHORT).show();
//                }
                db.walletDao().insertAll(new Wallet(wallet, balance, idUser));
                getActivity().finish();
//                if (((Home) getActivity()).homeToolbar.getVisibility() == View.GONE) {
//                    ((Home) getActivity()).homeToolbar.setVisibility(View.VISIBLE);
//                }
            }
        }
    }
}
