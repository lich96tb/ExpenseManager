package ict.com.expensemanager.ui.slide_menu;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.List;

import ict.com.expensemanager.R;

/**
 * Created by lich96tb on 1/17/2018.
 */

public class SlineMenuAdapter extends ArrayAdapter<Slinemenu> {
    @NonNull
    Activity context;
    int resource;
    @NonNull
    List<Slinemenu> objects;

    public SlineMenuAdapter(@NonNull Activity context, int resource, @NonNull List<Slinemenu> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=this.context.getLayoutInflater();
        View view=inflater.inflate(R.layout.item_menu,null);
        TextView txtTenItem=view.findViewById(R.id.txtItemSlideMenu);
        ImageView imageView=view.findViewById(R.id.imgSlideMenu);

        Slinemenu slinemenu=this.objects.get(position);
        txtTenItem.setText(slinemenu.getName());
        imageView.setImageResource(slinemenu.getImg());
        return view;
    }
}
