package ict.com.expensemanager.ui.category;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ict.com.expensemanager.R;
import ict.com.expensemanager.data.database.entity.Category;

/**
 * Created by Tuan Huy on 1/19/2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<Category> categories;
    private Context context;
    private AdapterCategoryCallBack callBack;

    public CategoryAdapter(Context context,List<Category> categories,AdapterCategoryCallBack callBack) {
        this.context = context;
        this.categories = categories;
        this.callBack = callBack;

    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.ViewHolder holder, int position) {
        final Category category = categories.get(position);
        holder.imgCategory.setImageResource(category.getCategoryIcon());
        holder.tvCategory.setText(category.getCategoryName());
        holder.cardviewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callBack != null) {
                    callBack.onItemClick(category);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgCategory;
        private TextView tvCategory;
        private CardView cardviewCategory;

        public ViewHolder(View itemView) {
            super(itemView);
            imgCategory = itemView.findViewById(R.id.image_category);
            tvCategory = itemView.findViewById(R.id.text_category);
            cardviewCategory = itemView.findViewById(R.id.cardview_category);
        }
    }
}
