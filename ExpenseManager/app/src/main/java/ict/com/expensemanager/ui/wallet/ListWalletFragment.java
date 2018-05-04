package ict.com.expensemanager.ui.wallet;


import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ict.com.expensemanager.R;
import ict.com.expensemanager.data.database.AppDatabase;
import ict.com.expensemanager.data.database.entity.Wallet;
import ict.com.expensemanager.data.preferences.SharePreferencesManager;
import ict.com.expensemanager.ui.slide_menu.Home;
import ict.com.expensemanager.util.AppKey;


public class ListWalletFragment extends Fragment implements View.OnClickListener, AdapterWalletCallBack {

    @BindView(R.id.rcvWallet)
    RecyclerView rcvWallet;
    @BindView(R.id.fab_wallet)
    FloatingActionButton fab;
    Unbinder unbinder;
    private WalletAdapter adapter;
    private List<Wallet> wallets;
    private FragmentTransaction transaction;
    private FragmentManager manager;
    private int idUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_wallet, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fab.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Home)getActivity()).getSupportActionBar().setTitle("Danh sách ví");
        if (((Home) getActivity()).homeToolbar.getVisibility() == View.GONE) {
            ((Home) getActivity()).homeToolbar.setVisibility(View.VISIBLE);
        }
        AppDatabase db = Room.databaseBuilder(getContext(),
                AppDatabase.class, AppKey.NAME_DATABASE)
                .allowMainThreadQueries()
                .build();
        SharedPreferences preferences = SharePreferencesManager.getInstance(getActivity().getApplicationContext());
        idUser = preferences.getInt(AppKey.KEY_USER_ID, 0);
        wallets = db.walletDao().getAllWallets(idUser);
        rcvWallet.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new WalletAdapter(getContext(), wallets, this);
        rcvWallet.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_wallet:
                startActivity(new Intent(getContext(), WalletActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemClick(Wallet wallet) {
        DetailsWalletFragment detailsWalletFragment = new DetailsWalletFragment();
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.Frame_Content, detailsWalletFragment);
        transaction.addToBackStack(null);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Wallet", wallet);
        detailsWalletFragment.setArguments(bundle);
        transaction.commit();
    }
}
