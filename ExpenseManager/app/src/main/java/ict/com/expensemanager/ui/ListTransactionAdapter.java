package ict.com.expensemanager.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ict.com.expensemanager.R;
import ict.com.expensemanager.data.database.dao.TransactionDao;
import ict.com.expensemanager.data.database.entity.Transaction;
import ict.com.expensemanager.ui.custom.AutoFormatEditText;
import ict.com.expensemanager.ui.custom.AutoFormatUtil;

/**
 * Created by PHAMHOAN on 1/29/2018.
 */

public class ListTransactionAdapter extends RecyclerView.Adapter<ListTransactionAdapter.ViewHolder> {
    private List<TransactionDao.TransactionWalletCategory> transactionList;
    private Context context;
    private LayoutInflater layoutInflater;

    public ListTransactionAdapter(Context context, List<TransactionDao.TransactionWalletCategory> transactionList) {
        this.transactionList = transactionList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_transaction, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final TransactionDao.TransactionWalletCategory transactionWalletCategory = transactionList.get(position);
        holder.txtTransactionName.setText(transactionWalletCategory.getTransaction_name());
        holder.txtTransactionWallet.setText(transactionWalletCategory.getWallet_name());
        holder.txtTransactionPrice.setText(AutoFormatUtil.formatToStringWithoutDecimal(transactionWalletCategory.getPrice()));
        if (transactionWalletCategory.getCategory_icon() != 0) {
            holder.imageIcon.setImageResource(transactionWalletCategory.getCategory_icon());
        }

    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTransactionName, txtTransactionWallet;
        private TextView txtTransactionPrice;
        private CircleImageView imageIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTransactionName = itemView.findViewById(R.id.txt_transaction_name);
            txtTransactionWallet = itemView.findViewById(R.id.txt_wallet_name);
            txtTransactionPrice = itemView.findViewById(R.id.txt_transaction_price);
            imageIcon = itemView.findViewById(R.id.image_category);

        }
    }
}
