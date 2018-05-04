package ict.com.expensemanager.ui;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ict.com.expensemanager.R;
import ict.com.expensemanager.data.database.AppDatabase;
import ict.com.expensemanager.data.database.dao.TransactionDao;
import ict.com.expensemanager.data.preferences.SharePreferencesManager;
import ict.com.expensemanager.ui.slide_menu.Home;
import ict.com.expensemanager.ui.transaction.TransactionAddingActivity;
import ict.com.expensemanager.util.AppKey;

/**
 * Created by PHAMHOAN on 1/29/2018.
 */

public class ListTransactionFragment extends Fragment {
    View rootView;
    @BindView(R.id.rcv_listTransaction)
    RecyclerView rcvListTransaction;
    Unbinder unbinder;
    ListTransactionAdapter listTransactionAdapter;
    @BindView(R.id.fab_wallet)
    FloatingActionButton fabWallet;
    @BindView(R.id.txtTransaction)
    TextView txtTransaction;

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    private int idUser = 0;

    private List<TransactionDao.TransactionWalletCategory> transactionList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_list_transaction, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        transactionList = new ArrayList<>();
        AppDatabase db = Room.databaseBuilder(getContext(),
                AppDatabase.class, AppKey.NAME_DATABASE)
                .allowMainThreadQueries()
                .build();

        transactionList = db.transactionDao().getTransactionByWalletCategory(idUser);
        if (transactionList.size()!=0){
            txtTransaction.setVisibility(View.GONE);
        }else {
            txtTransaction.setVisibility(View.VISIBLE);
        }
        String test = "SELECT * FROM `Transaction` WHERE id_user = 1 ";
//        if(vi dc chon){
//        test = test + " AND id_wallet = 1";}
//        if(category dc chon){
//        test = test + " AND id_category = 1";}
//        if(su kien dc chon){
//        test = test + " AND id_event = 1";}
        Toast.makeText(getActivity(), test, Toast.LENGTH_LONG).show();
        Cursor c = db.query(test,null);
        int count = c.getCount();

        Toast.makeText(getActivity(), ""+count, Toast.LENGTH_SHORT).show();

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        listTransactionAdapter = new ListTransactionAdapter(getContext(), transactionList);
        rcvListTransaction.setLayoutManager(llm);
        rcvListTransaction.setAdapter(listTransactionAdapter);

        if (((Home) getActivity()).homeToolbar.getVisibility() == View.GONE) {
            ((Home) getActivity()).homeToolbar.setVisibility(View.VISIBLE);
            ((Home) getActivity()).getSupportActionBar().setTitle("Danh sách giao dịch");
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fab_wallet)
    public void onViewClicked() {
        SharedPreferences sharedPreferences = SharePreferencesManager.getInstance(getActivity().getApplicationContext());
        int id = sharedPreferences.getInt(AppKey.KEY_USER_ID, 0);
        Intent intent = new Intent(getActivity(), TransactionAddingActivity.class);
        intent.putExtra(AppKey.KEY_USER_ID, id);
        startActivity(intent);
    }
}
