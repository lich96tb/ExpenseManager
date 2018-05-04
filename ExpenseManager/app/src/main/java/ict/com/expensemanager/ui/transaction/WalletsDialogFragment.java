package ict.com.expensemanager.ui.transaction;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ict.com.expensemanager.R;
import ict.com.expensemanager.data.database.AppDatabase;
import ict.com.expensemanager.data.database.entity.Wallet;
import ict.com.expensemanager.data.preferences.SharePreferencesManager;
import ict.com.expensemanager.ui.base.BaseDialogFragment;
import ict.com.expensemanager.ui.category.CategoryActivity;
import ict.com.expensemanager.ui.wallet.WalletActivity;
import ict.com.expensemanager.util.AppKey;
import ict.com.expensemanager.util.Commons;

/**
 * A simple {@link Fragment} subclass.
 */
public class WalletsDialogFragment extends BaseDialogFragment implements
        WalletsAdapter.OnMyWalletSelected,
        WalletsAdapter.OnCreateNewItemClick,

        View.OnClickListener {


    @BindView(R.id.recyclerview_wallets)
    RecyclerView recyclerviewWallets;
    @BindView(R.id.button_accept)
    Button buttonAccept;
    @BindView(R.id.button_cancel)
    Button buttonCancel;
    Unbinder unbinder;

    private List<Wallet> wallets;
    private WalletsAdapter walletsAdapter;
    private InteractionWithWalletsDialog interaction;
    private int walletPosCurrentSelected = -1;
    private int walletPostPreviousSelected = -1;
    private int userId;


    public WalletsDialogFragment() {
        // Required empty public constructor
    }

    public static WalletsDialogFragment newInstance() {

        Bundle args = new Bundle();

        WalletsDialogFragment fragment = new WalletsDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        interaction = (InteractionWithWalletsDialog) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = SharePreferencesManager.getInstance(getActivity().getApplicationContext());
        userId = sharedPreferences.getInt(AppKey.KEY_USER_ID,0);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wallets_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //
        //
        setupUiEvents();
        setupWalletsRecyclerView();
    }

    private void setupWalletsRecyclerView() {
        WalletsTask walletsTask = new WalletsTask(this);
        walletsTask.execute(userId);
    }

    private void setupUiEvents() {
        buttonAccept.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
    }


    public void setDataWalletsRecyclerView(List<Wallet> datas) {
        if (datas !=  null) {
            wallets = datas;
        } else {
            wallets = new ArrayList<>();
        }


        recyclerviewWallets.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerviewWallets.setHasFixedSize(true);

        walletsAdapter = new WalletsAdapter(wallets,  this);
        if (wallets.isEmpty()) {

            walletsAdapter.setOnCreateNewItemClick(this);
        }

        recyclerviewWallets.setAdapter(walletsAdapter);
    }



    @Override
    public void onResume() {
        Commons.setSizeDialog(getDialog());
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onWalletSelected(View view, boolean isChecked, int position) {

        if (isChecked) {

            if (walletPostPreviousSelected == -1) {
                walletPostPreviousSelected = position;
                walletPosCurrentSelected = position;
                wallets.get(walletPosCurrentSelected).setSelected(true);
                walletsAdapter.notifyItemChanged(walletPosCurrentSelected);

            } else {

                wallets.get(walletPostPreviousSelected).setSelected(false);
                walletsAdapter.notifyItemChanged(walletPostPreviousSelected);

                walletPosCurrentSelected = position;
                wallets.get(walletPosCurrentSelected).setSelected(true);
                walletsAdapter.notifyItemChanged(walletPosCurrentSelected);

                walletPostPreviousSelected = walletPosCurrentSelected;
            }

        } else {
            showMessage("False");
            wallets.get(position).setSelected(false);
            walletsAdapter.notifyItemChanged(position);

            walletPostPreviousSelected = -1;
            walletPosCurrentSelected = -1;
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_accept:
                onClickButtonAccept();
                break;

            case R.id.button_cancel:
                onClickButtonCancel();
                break;
        }
    }

    private void onClickButtonAccept() {
        if (walletPosCurrentSelected != -1) {

            interaction.setWalletSelected(wallets.get(walletPosCurrentSelected));
            showMessage("Chọn thành công");
            dismiss();
        } else {
            showMessage("Chưa có ví nào được chọn, mời chọn lại!");
        }


    }

    private void onClickButtonCancel() {
        dismiss();
    }

    @Override
    public void onClickButtonCreate(View view) {
        dismiss();
        startActivity(new Intent(getContext(), WalletActivity.class));
    }


    //

    static class WalletsTask extends AsyncTask<Integer, Void, List<Wallet>> {

        private WeakReference<WalletsDialogFragment> dialogRef;

        public WalletsTask(WalletsDialogFragment fragment) {
            this.dialogRef = new WeakReference<WalletsDialogFragment>(fragment);

        }

        @Override
        protected List<Wallet> doInBackground(Integer... integers) {
            int userID = integers[0];
            AppDatabase appDatabase = dialogRef.get().getAppDatabase();
            List<Wallet> wallets = null;

            if (appDatabase != null) {
                wallets =  appDatabase.walletDao().getWallets(userID);
            }

            return wallets;
        }

        @Override
        protected void onPostExecute(List<Wallet> wallets) {
                dialogRef.get().setDataWalletsRecyclerView(wallets);
                //Toast.makeText(dialogRef.get().getActivity(), wallets.get(0).getWalletName(),Toast.LENGTH_LONG).show();

        }
    }


    public interface InteractionWithWalletsDialog {

            void setWalletSelected(Wallet wallet);


    }
}
