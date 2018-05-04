package ict.com.expensemanager.ui.wallet;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import ict.com.expensemanager.R;
import ict.com.expensemanager.data.database.entity.Wallet;
import ict.com.expensemanager.ui.custom.AutoFormatUtil;

/**
 * Created by Tuan Huy on 1/22/2018.
 */

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.ViewHolder> {
    private List<Wallet> wallets;
    private Context context;
    private AdapterWalletCallBack callBack;


    public WalletAdapter(Context context, List<Wallet> wallets, AdapterWalletCallBack callBack) {
        this.context = context;
        this.wallets = wallets;
        this.callBack = callBack;
    }

    @Override
    public WalletAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wallet, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final WalletAdapter.ViewHolder holder, final int position) {
        final Wallet wallet = wallets.get(position);

        String balance = AutoFormatUtil.formatToStringWithoutDecimal(wallet.getBalance());

        holder.imageWallet.setImageResource(R.drawable.ic_wallet);
        holder.textWallet.setText(wallet.getWalletName());
        holder.textMoney.setText(balance);

        holder.cardviewWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LMMMMMMMMMM", "onBindViewHolder: " + wallet.getBalance());
                if (callBack != null) {
                    callBack.onItemClick(wallet);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return wallets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageWallet;
        private TextView textWallet, textMoney;
        private CardView cardviewWallet;

        public ViewHolder(View itemView) {
            super(itemView);
            imageWallet = itemView.findViewById(R.id.image_wallet);
            textWallet = itemView.findViewById(R.id.text_wallet);
            textMoney = itemView.findViewById(R.id.text_money);
            cardviewWallet = itemView.findViewById(R.id.cardview_wallet);
        }
    }
}
