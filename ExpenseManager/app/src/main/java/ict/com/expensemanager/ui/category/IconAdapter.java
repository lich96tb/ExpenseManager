package ict.com.expensemanager.ui.category;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Tuan Huy on 1/23/2018.
 */

public class IconAdapter extends BaseAdapter {
    private Context context;
    private Integer[] iconId;

    public IconAdapter(Context context) {
        this.context = context;
    }

    public IconAdapter(Context context, Integer[] iconId) {
        this.context = context;
        this.iconId = iconId;
    }

    @Override
    public int getCount() {
        return iconId.length;
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
        ImageView imageIcon;
        if (view == null) {
            imageIcon = new ImageView(context);

            imageIcon.setLayoutParams(new GridView.LayoutParams(100, 100));
            imageIcon.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageIcon.setPadding(8, 8, 8, 8);
        } else {
            imageIcon = (ImageView) view;
        }

        imageIcon.setImageResource(iconId[i]);
        return imageIcon;
    }
}
