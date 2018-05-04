package ict.com.expensemanager.ui.transaction;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ict.com.expensemanager.R;
import ict.com.expensemanager.data.database.entity.RepeatType;

/**
 * Created by nguyenanhtrung on 24/01/2018.
 */

public class RepeatTypeAdapter extends ArrayAdapter<RepeatType> {

    private Context context = null;
    private List<RepeatType> repeatTypes = null;
    private int resource = 0;


    public RepeatTypeAdapter(@NonNull Context context, int resource, @NonNull List<RepeatType> objects) {
        super(context, resource, objects);
        this.context = context;
        this.repeatTypes = objects;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(resource, parent,false);
        TextView textRepeatType = view.findViewById(R.id.text_repeat_type_name);

        RepeatType repeatType = repeatTypes.get(position);
        if (repeatType != null) {
            textRepeatType.setText(repeatType.getRepeat_type_name());
        }

        return view;

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_repeat_type, parent,false);
        TextView textRepeatType = view.findViewById(R.id.text_repeat_type_name);

        RepeatType repeatType = repeatTypes.get(position);
        if (repeatType != null) {
            textRepeatType.setText(repeatType.getRepeat_type_name());
        }

        return view;
    }
}
