package ict.com.expensemanager.ui.transaction;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ict.com.expensemanager.R;
import ict.com.expensemanager.data.database.AppDatabase;
import ict.com.expensemanager.data.database.entity.Category;
import ict.com.expensemanager.data.preferences.SharePreferencesManager;
import ict.com.expensemanager.ui.base.BaseDialogFragment;
import ict.com.expensemanager.ui.category.CategoryActivity;
import ict.com.expensemanager.util.AppKey;
import ict.com.expensemanager.util.Commons;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesDialogFragment extends BaseDialogFragment
        implements View.OnClickListener,
        CategoriesAdapter.OnCreateNewItemClick,
        CategoriesAdapter.OnMyCategorySelected {


    @BindView(R.id.recyclerview_categories)
    RecyclerView recyclerviewCategories;
    @BindView(R.id.button_accept)
    Button buttonAccept;
    @BindView(R.id.button_cancel)
    Button buttonCancel;
    Unbinder unbinder;

    private List<Category> categories = new ArrayList<>();
    private CategoriesAdapter categoriesAdapter;
    private int categoryPosCurrentSelected = -1;
    private int categoryPostPreviousSelected = -1;
    private InteractionWithCategoriesDialog interaction;
    private int userId;


    public CategoriesDialogFragment() {
        // Required empty public constructor
    }

    public static CategoriesDialogFragment newInstance() {

        Bundle args = new Bundle();

        CategoriesDialogFragment fragment = new CategoriesDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TransactionAddingActivity) {
            interaction = (InteractionWithCategoriesDialog) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = SharePreferencesManager.getInstance(getActivity().getApplicationContext());
        userId=sharedPreferences.getInt(AppKey.KEY_USER_ID, 0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CategoriesTask categoriesTask = new CategoriesTask(this);
        categoriesTask.execute(userId);
        setupUiEvents();
        //

    }

    private void setupUiEvents() {
        buttonAccept.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
        //

    }


    public void setupRecyclerviewCategories(List<Category> datas) {

        if (datas != null) {
            categories = datas;
        } else {
            categories = new ArrayList<>();
        }


        recyclerviewCategories.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerviewCategories.setHasFixedSize(true);

        categoriesAdapter = new CategoriesAdapter(categories, this);

        if (categories.isEmpty()) {
            categoriesAdapter.setOnCreateNewItemClick(this);
        }
        recyclerviewCategories.setAdapter(categoriesAdapter);
    }

    @Override
    public void onResume() {
        Commons.setSizeDialog(getDialog());
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_accept:
                onClickButtonAccept();
                break;
            case R.id.button_cancel:
                onClickButtonCancel();
                break;
        }
    }

    private void onClickButtonCancel() {
        dismiss();
    }

    private void onClickButtonAccept() {
        if (categoryPosCurrentSelected != -1) {

            interaction.setCategorySelected(categories.get(categoryPosCurrentSelected));
            showMessage("Chọn thành công");
            dismiss();
        } else {
            showMessage("Chưa có danh mục nào được chọn, mời chọn lại!");
        }

    }

    @Override
    public void onCategorySelected(View view, boolean isChecked, int position) {

        if (isChecked) {

            if (categoryPostPreviousSelected == -1) {
                categoryPostPreviousSelected = position;
                categoryPosCurrentSelected = position;
                categories.get(position).setSelected(true);
                categoriesAdapter.notifyItemChanged(position);

            } else {

                categories.get(categoryPostPreviousSelected).setSelected(false);
                categoriesAdapter.notifyItemChanged(categoryPostPreviousSelected);

                categoryPosCurrentSelected = position;
                categories.get(categoryPosCurrentSelected).setSelected(true);
                categoriesAdapter.notifyItemChanged(categoryPosCurrentSelected);

                categoryPostPreviousSelected = categoryPosCurrentSelected;
            }

        } else {
            categories.get(position).setSelected(false);
            categoriesAdapter.notifyItemChanged(position);

            categoryPostPreviousSelected = -1;
            categoryPosCurrentSelected = -1;
        }
    }

    @Override
    public void onClickButtonCreate(View view) {
        dismiss();
        startActivity(new Intent(getContext(), CategoryActivity.class));
    }


    public interface InteractionWithCategoriesDialog {

        void setCategorySelected(Category category);
    }

    static class CategoriesTask extends AsyncTask<Integer, Void, List<Category>> {

        private WeakReference<CategoriesDialogFragment> dialogRef;

        public CategoriesTask(CategoriesDialogFragment fragment) {
            this.dialogRef = new WeakReference<CategoriesDialogFragment>(fragment);

        }

        @Override
        protected List<Category> doInBackground(Integer... integers) {
            int userID = integers[0];
            AppDatabase appDatabase = dialogRef.get().getAppDatabase();
            List<Category> categories = null;

            if (appDatabase != null) {
                categories = appDatabase.categoryDao().getCategories(userID);
            }


            return categories;
        }

        @Override
        protected void onPostExecute(List<Category> categories) {
            dialogRef.get().setupRecyclerviewCategories(categories);
        }
    }
}
