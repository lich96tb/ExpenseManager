package ict.com.expensemanager.ui.wallet;


import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ict.com.expensemanager.R;
import ict.com.expensemanager.data.database.AppDatabase;
import ict.com.expensemanager.data.database.entity.User;
import ict.com.expensemanager.data.database.entity.Wallet;
import ict.com.expensemanager.ui.category.ListCategoryFragment;
import ict.com.expensemanager.ui.custom.AutoFormatUtil;
import ict.com.expensemanager.ui.slide_menu.Home;
import ict.com.expensemanager.util.AppKey;

public class EditWalletFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.image_back_wallet_edit)
    ImageView imageBack;
    @BindView(R.id.text_save_wallet_edit)
    TextView textSave;
    @BindView(R.id.edittext_edit_wallet)
    EditText edittextWallet;
    @BindView(R.id.edittext_edit_money)
    EditText edittextMoney;
    Unbinder unbinder;

    private Wallet walletEdit;
    private FragmentManager manager;
    private FragmentTransaction transaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_wallet, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        manager = getFragmentManager();
        Bundle bundle = getArguments();
        walletEdit = (Wallet) bundle.getSerializable("Wallet");
        String balance = AutoFormatUtil.formatToStringWithoutDecimal(walletEdit.getBalance());
        edittextWallet.setText(walletEdit.getWalletName());
        edittextMoney.setText(balance);
        //events
        imageBack.setOnClickListener(this);
        textSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_save_wallet_edit:
                editWallet();
                break;
            case R.id.image_back_wallet_edit:
                manager.popBackStack();
                break;
            default:
                break;
        }
    }


    private void editWallet() {
        AppDatabase db = Room.databaseBuilder(getContext(),
                AppDatabase.class, AppKey.NAME_DATABASE)
                .allowMainThreadQueries()
                .build();

        String wallet = edittextWallet.getText().toString().trim();

        String money = (edittextMoney.getText().toString().replace(".", "").replace(",",""));

        if (TextUtils.isEmpty(wallet) || TextUtils.isEmpty(money)) {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        } else {
            int checkWWallet = db.walletDao().getCountByName(wallet);
            if (checkWWallet != 0) {
                Toast.makeText(getActivity(), "Tên ví đã được sử dụng", Toast.LENGTH_SHORT).show();
            } else {
                double balance = Double.parseDouble(money);
                db.walletDao().updateByID(walletEdit.getIdWallet(), wallet, balance);
//                transaction = manager.beginTransaction();
//                transaction.replace(R.id.Frame_Content, new ListWalletFragment());
//                transaction.commit();
//                if (((Home) getActivity()).homeToolbar.getVisibility() == View.GONE) {
//                    ((Home) getActivity()).homeToolbar.setVisibility(View.VISIBLE);
//                }
                manager = getFragmentManager();
                manager.popBackStack();
            }
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
