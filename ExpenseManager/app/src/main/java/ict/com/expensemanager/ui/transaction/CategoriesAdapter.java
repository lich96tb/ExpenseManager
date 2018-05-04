package ict.com.expensemanager.ui.transaction;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ict.com.expensemanager.R;
import ict.com.expensemanager.data.database.entity.Category;
import ict.com.expensemanager.util.Commons;

import static ict.com.expensemanager.util.AppKey.VIEW_EMPTY;
import static ict.com.expensemanager.util.AppKey.VIEW_NORMAL;

/**
 * Created by nguyenanhtrung on 22/01/2018.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Category> categories;
    private OnMyCategorySelected onMyCategorySelected;
    private Context context;
    private OnCreateNewItemClick onCreateNewItemClick;



    public interface OnCreateNewItemClick {

        void onClickButtonCreate(View view);
    }

    public void setOnCreateNewItemClick(OnCreateNewItemClick onCreateNewItemClick) {
        this.onCreateNewItemClick = onCreateNewItemClick;
    }

    public interface OnMyCategorySelected {

        void onCategorySelected(View view, boolean isChecked, int position);
    }

    public CategoriesAdapter(List<Category> categories, OnMyCategorySelected onMyCategorySelected) {
        this.categories = categories;
        this.onMyCategorySelected = onMyCategorySelected;

    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch (viewType) {
            case VIEW_EMPTY:
                return new EmptyViewHolder(inflater.inflate(R.layout.items_empty, parent, false),onCreateNewItemClick);
            case VIEW_NORMAL:

                return new CategoryHolder(inflater.inflate(R.layout.item_transaction_category, parent, false),onMyCategorySelected);
        }


        return new CategoryHolder(inflater.inflate(R.layout.item_transaction_category, parent, false), onMyCategorySelected);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CategoryHolder) {
            Category category = categories.get(position);
            CategoryHolder categoryHolder = (CategoryHolder) holder;
            if (category != null) {

                categoryHolder.textCategoryName.setText(category.getCategoryName());
                if (context != null) {
                    categoryHolder.imageCategory.setImageResource(category.getCategoryIcon());
                }


                categoryHolder.checkboxSelection.setOnCheckedChangeListener(null);
                if (category.isSelected()) {

                    categoryHolder.checkboxSelection.setChecked(true);
                } else {
                    categoryHolder.checkboxSelection.setChecked(false);
                }
                categoryHolder.checkboxSelection.setOnCheckedChangeListener(categoryHolder);


            }
        }


    }

    @Override
    public int getItemCount() {
        if (categories.isEmpty()) {
            return 1;
        }
        return categories.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (categories.isEmpty()) {
            return VIEW_EMPTY;
        }
        return VIEW_NORMAL;
    }

    static class CategoryHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener{
        @BindView(R.id.image_category)
        CircleImageView imageCategory;
        @BindView(R.id.text_category_name)
        TextView textCategoryName;
        @BindView(R.id.checkbox_category_selection)
        CheckBox checkboxSelection;

        private OnMyCategorySelected onMyCategorySelected;

        CategoryHolder(View view,OnMyCategorySelected onMyCategorySelected ) {
            super(view);
            ButterKnife.bind(this, view);
            this.onMyCategorySelected = onMyCategorySelected;

            //
            checkboxSelection.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            onMyCategorySelected.onCategorySelected(compoundButton,b, getAdapterPosition());
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

