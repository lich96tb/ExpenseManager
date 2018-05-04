package ict.com.expensemanager.ui.category;


import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ict.com.expensemanager.R;
import ict.com.expensemanager.data.database.AppDatabase;
import ict.com.expensemanager.data.database.entity.Category;
import ict.com.expensemanager.data.database.entity.User;
import ict.com.expensemanager.data.preferences.SharePreferencesManager;
import ict.com.expensemanager.ui.slide_menu.Home;
import ict.com.expensemanager.util.AppKey;

public class AddCategoryFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.image_back_add_category)
    ImageView imageBack;
    @BindView(R.id.text_save_category)
    TextView textSave;
    @BindView(R.id.image_add_category)
    ImageView imageCategory;
    @BindView(R.id.edittext_add_category)
    EditText edittextCategory;
    Unbinder unbinder;

    private FragmentManager manager;
    private FragmentTransaction transaction;
    private int icon = 0;
    private int idUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_category, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        ((Home) getActivity()).homeToolbar.setVisibility(View.GONE);
        manager = getFragmentManager();
        if (icon == 0) {
            imageCategory.setImageResource(R.drawable.ic_faq);
        } else {
            imageCategory.setImageResource(icon);
        }

        textSave.setOnClickListener(this);
        imageBack.setOnClickListener(this);
        imageCategory.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_save_category:
                addCategory();
                break;
            case R.id.image_back_add_category:
//                if (((Home) getActivity()).homeToolbar.getVisibility() == View.GONE) {
//                    ((Home) getActivity()).homeToolbar.setVisibility(View.VISIBLE);
//                }
//                manager.popBackStack();
                getActivity().finish();
                break;
            case R.id.image_add_category:
                chooseIcon();
                break;
            default:
                break;
        }

    }


    private void chooseIcon() {
        IconCategoryFragment iconCategoryFragment = new IconCategoryFragment();
        iconCategoryFragment.setTargetFragment(this, AppKey.FRAGMENT_CODE);
        transaction = manager.beginTransaction();
        transaction.replace(R.id.Frame_Category, iconCategoryFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void addCategory() {
        AppDatabase db = Room.databaseBuilder(getContext(),
                AppDatabase.class, AppKey.NAME_DATABASE)
                .allowMainThreadQueries()
                .build();
        SharedPreferences preferences = SharePreferencesManager.getInstance(getActivity().getApplicationContext());
        idUser = preferences.getInt(AppKey.KEY_USER_ID,0);

        String category = edittextCategory.getText().toString().trim();
        if (TextUtils.isEmpty(category)) {
            edittextCategory.setError("Bạn cần nhập tên nhóm");
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin của danh mục", Toast.LENGTH_SHORT).show();

        } else if (icon == 0) {
            Toast.makeText(getContext(), "Đừng quên chọn ảnh", Toast.LENGTH_SHORT).show();
        } else {
            int checkCategory = db.categoryDao().getCountByName(category);
            if (checkCategory != 0) {
                Toast.makeText(getActivity(), "Tên nhóm đã được sử dụng", Toast.LENGTH_SHORT).show();
            } else {
                db.categoryDao().insertAll(new Category(category, icon, idUser));
                getActivity().finish();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppKey.FRAGMENT_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                icon = data.getIntExtra(AppKey.ICON, AppKey.DEFAULT_VALUE);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
