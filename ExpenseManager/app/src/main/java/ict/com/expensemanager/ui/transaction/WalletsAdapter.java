package ict.com.expensemanager.ui.transaction;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ict.com.expensemanager.R;
import ict.com.expensemanager.data.database.entity.Wallet;
import ict.com.expensemanager.ui.custom.AutoFormatUtil;

import static ict.com.expensemanager.util.AppKey.VIEW_EMPTY;
import static ict.com.expensemanager.util.AppKey.VIEW_NORMAL;

/**
 * Created by nguyenanhtrung on 22/01/2018.
 */

public class WalletsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Wallet> wallets;
    private OnMyWalletSelected onMyWalletSelected;
    private OnCreateNewItemClick onCreateNewItemClick;



    public interface OnMyWalletSelected {

        void onWalletSelected(View view,boolean isChecked, int position);
    }

    public interface OnCreateNewItemClick {

        void onClickButtonCreate(View view);
    }

    public WalletsAdapter(List<Wallet> wallets, OnMyWalletSelected onMyWalletSelected) {
        this.wallets = wallets;
        this.onMyWalletSelected = onMyWalletSelected;

    }


    public void setOnCreateNewItemClick(OnCreateNewItemClick onCreateNewItemClick) {
        this.onCreateNewItemClick = onCreateNewItemClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch (viewType) {
            case VIEW_EMPTY:
                return new EmptyViewHolder(inflater.inflate(R.layout.items_empty, parent, false), onCreateNewItemClick);
            case VIEW_NORMAL:

                return new WalletHolder(inflater.inflate(R.layout.item_transaction_wallet, parent, false),onMyWalletSelected);
        }


        return new WalletHolder(inflater.inflate(R.layout.item_transaction_wallet, parent, false), onMyWalletSelected);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof WalletHolder) {
            Wallet wallet = wallets.get(position);
            WalletHolder walletHolder = (WalletHolder) holder;
            if (wallet != null) {

                walletHolder.textWalletName.setText(wallet.getWalletName());
                Log.d("ABCD", "onBindViewHolder: "+ wallet.getBalance());
                String balance = String.valueOf(String.valueOf(wallet.getBalance()).replace(".0",""));


                walletHolder.textWalletBalance.setText(AutoFormatUtil.formatToStringWithoutDecimal(balance));


                walletHolder.checkboxSelection.setOnCheckedChangeListener(null);
                if (wallet.isSelected()) {

                    walletHolder.checkboxSelection.setChecked(true);
                } else {
                    walletHolder.checkboxSelection.setChecked(false);
                }
                walletHolder.checkboxSelection.setOnCheckedChangeListener(walletHolder);


            }
        }


    }

    @Override
    public int getItemCount() {
        if (wallets.isEmpty()) {
            return 1;
        }
        return wallets.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (wallets.isEmpty()) {
            return VIEW_EMPTY;
        }
        return VIEW_NORMAL;
    }

    static class WalletHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener{
        @BindView(R.id.text_wallet_name)
        TextView textWalletName;
        @BindView(R.id.text_wallet_balance)
        TextView textWalletBalance;
        @BindView(R.id.checkbox_selection)
        CheckBox checkboxSelection;

        private OnMyWalletSelected onMyWalletSelected;

        WalletHolder(View view,OnMyWalletSelected onMyWalletSelected ) {
            super(view);
            ButterKnife.bind(this, view);
            this.onMyWalletSelected = onMyWalletSelected;

            //
            checkboxSelection.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            onMyWalletSelected.onWalletSelected(compoundButton,b, getAdapterPosition());
        }
    }

    static class EmptyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button buttonCreateNew;

        private OnCreateNewItemClick onCreateNewItemClick;



        public EmptyViewHolder(View itemView, OnCreateNewItemClick onCreateNewItemClick) {
            super(itemView);
            buttonCreateNew = itemView.findViewById(R.id.button_create_new);
            this.onCreateNewItemClick = onCreateNewItemClick;

            buttonCreateNew.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onCreateNewItemClick.onClickButtonCreate(view);

        }
    }
}
