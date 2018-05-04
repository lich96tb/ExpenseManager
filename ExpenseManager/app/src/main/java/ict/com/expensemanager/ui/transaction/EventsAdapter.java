package ict.com.expensemanager.ui.transaction;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import ict.com.expensemanager.data.database.entity.Event;
import ict.com.expensemanager.ui.custom.AutoFormatUtil;
import ict.com.expensemanager.util.Commons;

import static ict.com.expensemanager.util.AppKey.VIEW_EMPTY;
import static ict.com.expensemanager.util.AppKey.VIEW_NORMAL;

/**
 * Created by nguyenanhtrung on 22/01/2018.
 */

public class EventsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Event> events;
    private OnMyEventSelected onMyEventSelected;
    private OnCreateNewItemClick onCreateNewItemClick;


    public interface OnCreateNewItemClick {

        void onClickButtonCreate(View view);
    }


    public EventsAdapter(List<Event> events, OnMyEventSelected onMyEventSelected) {
        this.events = events;
        this.onMyEventSelected = onMyEventSelected;

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

                return new EventHolder(inflater.inflate(R.layout.item_transaction_event, parent, false), onMyEventSelected);
        }


        return new EventHolder(inflater.inflate(R.layout.item_transaction_event, parent, false), onMyEventSelected);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EventHolder) {
            Event event = events.get(position);
            EventHolder eventHolder = (EventHolder) holder;
            if (event != null) {

                eventHolder.textEventName.setText(event.getEventName());
                String balance = String.valueOf(Commons.convertMoneyDoubleToLong(event.getBalance()));
                String estimateMoney = String.valueOf(Commons.convertMoneyDoubleToLong(event.getExpectedMoney()));

                eventHolder.textBalance.setText(AutoFormatUtil.formatToStringWithoutDecimal(balance));
                eventHolder.textEstimateMoney.setText(AutoFormatUtil.formatToStringWithoutDecimal(estimateMoney));


                eventHolder.checkboxSelection.setOnCheckedChangeListener(null);
                if (event.isSelected()) {

                    eventHolder.checkboxSelection.setChecked(true);
                } else {
                    eventHolder.checkboxSelection.setChecked(false);
                }
                eventHolder.checkboxSelection.setOnCheckedChangeListener(eventHolder);


            }
        }


    }

    @Override
    public int getItemCount() {
        if (events.isEmpty()) {
            return 1;
        }
        return events.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (events.isEmpty()) {
            return VIEW_EMPTY;
        }
        return VIEW_NORMAL;
    }

    public interface OnMyEventSelected {

        void onCategorySelected(View view, boolean isChecked, int position);
    }

    static class EventHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
        @BindView(R.id.text_event_name)
        TextView textEventName;
        @BindView(R.id.text_estimate_money)
        TextView textEstimateMoney;
        @BindView(R.id.text_balance)
        TextView textBalance;
        @BindView(R.id.checkbox_selection)
        CheckBox checkboxSelection;
        private OnMyEventSelected onMyEventSelected;

        EventHolder(View view, OnMyEventSelected onMyEventSelected) {
            super(view);
            ButterKnife.bind(this, view);
            this.onMyEventSelected = onMyEventSelected;

            //
            checkboxSelection.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            onMyEventSelected.onCategorySelected(compoundButton, b, getAdapterPosition());
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

