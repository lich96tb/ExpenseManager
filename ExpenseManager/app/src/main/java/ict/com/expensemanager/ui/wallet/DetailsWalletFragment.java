package ict.com.expensemanager.ui.wallet;


import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ict.com.expensemanager.R;
import ict.com.expensemanager.data.database.AppDatabase;
import ict.com.expensemanager.data.database.entity.Wallet;
import ict.com.expensemanager.ui.custom.AutoFormatUtil;
import ict.com.expensemanager.ui.report.ReportActivity;
import ict.com.expensemanager.ui.slide_menu.Home;
import ict.com.expensemanager.util.AppKey;


public class DetailsWalletFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.image_back_wallet_details)
    ImageView imageBack;
    @BindView(R.id.image_edit_wallet)
    ImageView imageEdit;
    @BindView(R.id.image_delete_wallet)
    ImageView imageDelete;
    @BindView(R.id.text_wallet_name)
    TextView textWalletName;
    @BindView(R.id.text_wallet_money)
    TextView textWalletMoney;
    @BindView(R.id.text_wallet_transaction)
    TextView textWalletTransaction;
    @BindView(R.id.button_view_report_wallet)
    Button buttonViewReport;
    Unbinder unbinder;
    int idWallet;

    private Wallet wallet;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details_wallet, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //events
        imageDelete.setOnClickListener(this);
        imageEdit.setOnClickListener(this);
        imageBack.setOnClickListener(this);
        buttonViewReport.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (((Home) getActivity()).homeToolbar.getVisibility() == View.VISIBLE) {
            ((Home) getActivity()).homeToolbar.setVisibility(View.GONE);
        }

        manager = getFragmentManager();
        AppDatabase db = Room.databaseBuilder(getContext(),
                AppDatabase.class, AppKey.NAME_DATABASE)
                .allowMainThreadQueries()
                .build();
        bundle = getArguments();
        wallet = (Wallet) bundle.getSerializable("Wallet");
        String balance = AutoFormatUtil.formatToStringWithoutDecimal(db.transactionDao().transactionMoneyByWallet(wallet.getIdWallet()));
        textWalletName.setText(db.walletDao().getWalletNameReport(wallet.getIdWallet()));
        textWalletMoney.setText(balance);
        int countTransaction = db.transactionDao().transactionByWallet(wallet.getIdWallet());
        textWalletTransaction.setText(countTransaction + "");
        idWallet = wallet.getIdWallet();
        wallet = db.walletDao().getWalletByIdWallet(idWallet);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_delete_wallet:
                deleteWallet();
                break;
            case R.id.image_edit_wallet:
                gotoFragmentEditWallet();
                break;
            case R.id.image_back_wallet_details:
                checkOpenToolBar();
                manager.popBackStack();
                break;
            case R.id.button_view_report_wallet:
                callReport();
                break;
            default:
                break;
        }
    }

//    private void gotoFragmentReport() {
//        transaction = manager.beginTransaction();
//        transaction.replace(R.id.Frame_Content, new ReportActivity);
//        transaction.commit();
//    }


    private void gotoFragmentEditWallet() {
        EditWalletFragment editWalletFragment = new EditWalletFragment();
        transaction = manager.beginTransaction();
        bundle.putSerializable("Wallet",wallet);
        editWalletFragment.setArguments(bundle);
        transaction.replace(R.id.Frame_Content, editWalletFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void deleteWallet() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Xóa Ví");
        builder.setMessage("Bạn chắc chắn muốn xóa ví này không ? \n (Các giao dịch của ví cũng sẽ bị xóa)");
        builder.setCancelable(false);
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AppDatabase db = Room.databaseBuilder(getContext(),
                        AppDatabase.class, AppKey.NAME_DATABASE)
                        .allowMainThreadQueries()
                        .build();
                db.transactionDao().deleteTransactionByIdWallet(wallet.getIdWallet());
                db.walletDao().deleleByID(wallet.getIdWallet());

                transaction = manager.beginTransaction();
                transaction.replace(R.id.Frame_Content, new ListWalletFragment());
                transaction.commit();
                checkOpenToolBar();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void checkOpenToolBar() {
        if (((Home) getActivity()).homeToolbar.getVisibility() == View.GONE) {
            ((Home) getActivity()).homeToolbar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void callReport() {
        Intent intent = new Intent(getActivity(), ReportActivity.class);
        intent.putExtra(AppKey.KEY_CALL_REPORT, AppKey.KEY_WALLET_REPORT);
        intent.putExtra(AppKey.KEY_CALL_DATA_REPORT, idWallet);
        startActivity(intent);
    }

}
