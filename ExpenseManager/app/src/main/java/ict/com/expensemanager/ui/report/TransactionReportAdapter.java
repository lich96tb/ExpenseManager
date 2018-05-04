package ict.com.expensemanager.ui.report;

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
import ict.com.expensemanager.data.database.entity.Transaction;
import ict.com.expensemanager.ui.custom.AutoFormatUtil;

/**
 * Created by PHAMHOAN on 1/30/2018.
 */

public class TransactionReportAdapter extends RecyclerView.Adapter<TransactionReportAdapter.RecyclerViewHolder> {
    Context context;
    List<Transaction> transactionDetailsList;
    IReport iReport;
    public TransactionReportAdapter(List<Transaction> transactionDetails,Context context, IReport iReport) {
        this.transactionDetailsList = transactionDetails;
        this.context = context;
        this.iReport = iReport;
        if(transactionDetailsList.size()==0){
            iReport.showText();
        }else{
            iReport.hideText();
        }
    }


    @Override
    public TransactionReportAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item, parent, false);
        return new TransactionReportAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TransactionReportAdapter.RecyclerViewHolder holder, int position) {
            Transaction transaction = transactionDetailsList.get(position);
            holder.txtTransactionName.setText(transaction.getTransactionName());
            String money = AutoFormatUtil.formatToStringWithoutDecimal(transaction.getPrice());
            holder.txtTransactionMoney.setText(money + " đ");
    }

    @Override
    public int getItemCount() {
        return transactionDetailsList.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView txtTransactionName;
        TextView txtTransactionMoney;
        LinearLayout linearLayoutItem;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            txtTransactionName = itemView.findViewById(R.id.txt_item_name);
            txtTransactionMoney = itemView.findViewById(R.id.txt_item_money);
            linearLayoutItem = itemView.findViewById(R.id.ln_item);
        }

    }

    public void clearList(){
        transactionDetailsList.clear();
        notifyDataSetChanged();
        iReport.showText();
    }
}
