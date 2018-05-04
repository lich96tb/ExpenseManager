package ict.com.expensemanager.ui.category;


import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ict.com.expensemanager.R;
import ict.com.expensemanager.data.database.AppDatabase;
import ict.com.expensemanager.data.database.entity.Category;
import ict.com.expensemanager.data.preferences.SharePreferencesManager;
import ict.com.expensemanager.ui.slide_menu.Home;
import ict.com.expensemanager.util.AppKey;


public class ListCategoryFragment extends Fragment implements View.OnClickListener, AdapterCategoryCallBack {
    @BindView(R.id.rcvCategory)
    RecyclerView rcvCategory;
    @BindView(R.id.fab_category)
    FloatingActionButton fab;
    Unbinder unbinder;
    private CategoryAdapter adapter;
    private List<Category> categories;
    private FragmentTransaction transaction;
    private FragmentManager manager;
    private int idUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_category, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        manager = getFragmentManager();
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_category:
                startActivity(new Intent(getContext(), CategoryActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Home)getActivity()).getSupportActionBar().setTitle("Danh sách danh mục");

        if (((Home) getActivity()).homeToolbar.getVisibility() == View.GONE) {
            ((Home) getActivity()).homeToolbar.setVisibility(View.VISIBLE);
        }
        SharedPreferences preferences = SharePreferencesManager.getInstance(getActivity().getApplicationContext());
        idUser = preferences.getInt(AppKey.KEY_USER_ID, 0);
        AppDatabase db = Room.databaseBuilder(getContext(),
                AppDatabase.class, AppKey.NAME_DATABASE)
                .allowMainThreadQueries()
                .build();
        categories = db.categoryDao().getAllCategories(idUser);
        adapter = new CategoryAdapter(getContext(), categories, this);
        rcvCategory.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvCategory.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemClick(Category category) {
        DetailsCategoryFragment detailsCategoryFragment = new DetailsCategoryFragment();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.Frame_Content, detailsCategoryFragment);
        transaction.addToBackStack(null);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Category", category);
        detailsCategoryFragment.setArguments(bundle);
        transaction.commit();
    }

}
