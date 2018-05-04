package ict.com.expensemanager.ui.event;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import ict.com.expensemanager.R;
import ict.com.expensemanager.data.database.entity.Event;
import ict.com.expensemanager.ui.custom.AutoFormatUtil;

/**
 * Created by PHAMHOAN on 1/22/2018.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>{
    private Context context;
    private List<Event> listEvents;
    private LayoutInflater layoutInflater;
    private IDataBack iDataBack;

    public EventAdapter(Context context, List<Event> listEvents) {
        this.context = context;
        this.listEvents = listEvents;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_event,parent,false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Event event = listEvents.get(position);
        holder.txtEventName.setText(event.getEventName());
        String balance = AutoFormatUtil.formatToStringWithoutDecimal(event.getBalance());
        String expected = AutoFormatUtil.formatToStringWithoutDecimal(event.getExpectedMoney());

        holder.txtBalance.setText(balance);
        holder.txtExpectedMoney.setText(expected);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iDataBack.dataBack(position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return listEvents.size();
    }
    public void setDataBack(IDataBack iDataBack) {
        this.iDataBack = iDataBack;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtEventName, txtExpectedMoney, txtBalance;

        public ViewHolder(View itemView) {
            super(itemView);
            txtEventName = itemView.findViewById(R.id.text_view_event_name);
            txtExpectedMoney  = itemView.findViewById(R.id.text_view_expected_money);
            txtBalance = itemView.findViewById(R.id.text_view_balance);
        }
    }



}
