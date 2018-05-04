package ict.com.expensemanager.ui.transaction;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokenautocomplete.TokenCompleteTextView;

import ict.com.expensemanager.R;
import ict.com.expensemanager.data.database.entity.Partner;

/**
 * Created by nguyenanhtrung on 23/01/2018.
 */

public class PartnerCompletionView extends TokenCompleteTextView<Partner> {


    public PartnerCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View getViewForObject(Partner object) {
        LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TextView view = (TextView) l.inflate(R.layout.item_partner, (ViewGroup) getParent(), false);
        view.setText(object.getPartnerName());

        return view;
    }

    @Override
    protected Partner defaultObject(String completionText) {
        return new Partner();
    }
}
