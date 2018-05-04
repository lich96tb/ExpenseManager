package ict.com.expensemanager.ui.transaction;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import ict.com.expensemanager.R;

/**
 * Created by nguyenanhtrung on 24/01/2018.
 */

public class DayPickerAdapter extends BaseAdapter {
    private int[] days;
    private Context context;

    public DayPickerAdapter(Context context, int[] days) {
        this.context = context;
        this.days = days;
    }

    @Override
    public int getCount() {
        return days.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView textView = null;
        textView = (TextView) LayoutInflater.from(context).inflate(R.layout.item_cell_day, viewGroup, false);
        textView.setText(String.valueOf(days[i]));

        return textView;


    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
