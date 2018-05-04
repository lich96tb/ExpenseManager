package ict.com.expensemanager.ui.report;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ict.com.expensemanager.R;
import ict.com.expensemanager.data.database.dao.TransactionDao;
import ict.com.expensemanager.ui.custom.AutoFormatUtil;

/**
 * Created by Lenovo-PC on 1/25/2018.
 */

public class TransactionReprotTimeAdapter extends RecyclerView.Adapter<TransactionReprotTimeAdapter.RecyclerViewHolder> {
    Context context;
    List<TransactionDao.timeTransaction> data;
    IReport iReport;

    public TransactionReprotTimeAdapter(List<TransactionDao.timeTransaction> data, Context context, IReport iReport) {
        this.data = data;
        this.context = context;
        this.iReport= iReport;
        if(data.size()==0){
            iReport.showText();
        }else{
            iReport.hideText();
        }

    }


    @Override
    public TransactionReprotTimeAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item, parent, false);

        return new TransactionReprotTimeAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TransactionReprotTimeAdapter.RecyclerViewHolder holder, int position) {
        TransactionDao.timeTransaction category = data.get(position);
        holder.txtTimeTransactionName.setText(category.getTransaction_name());
        String money = AutoFormatUtil.formatToStringWithoutDecimal(category.getPrice());
        holder.txtTimeTransactionMoney.setText(money + " đ");

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView txtTimeTransactionName;
        TextView txtTimeTransactionMoney;
        TextView txtReport;
        LinearLayout linearLayoutItem;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            txtTimeTransactionName = itemView.findViewById(R.id.txt_item_name);
            txtTimeTransactionMoney = itemView.findViewById(R.id.txt_item_money);
            linearLayoutItem = itemView.findViewById(R.id.ln_item);
            txtReport=itemView.findViewById(R.id.txtReport);

        }
    }

    public void updateList(List<TransactionDao.timeTransaction> data) {
        this.data = data;
    }
}
