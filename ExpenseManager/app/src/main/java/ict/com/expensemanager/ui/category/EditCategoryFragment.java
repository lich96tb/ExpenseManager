package ict.com.expensemanager.ui.category;


import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
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
import ict.com.expensemanager.ui.slide_menu.Home;
import ict.com.expensemanager.util.AppKey;

public class EditCategoryFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.image_back_edit_category)
    ImageView imageBack;
    @BindView(R.id.text_save_category_edit)
    TextView textSave;
    @BindView(R.id.image_category_edit)
    ImageView imageEdit;
    @BindView(R.id.edittext_category_edit)
    EditText edittextEdit;
    Unbinder unbinder;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private Category category;
    private int icon = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_category, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        manager = getFragmentManager();
        Bundle bundle = getArguments();
        category = (Category) bundle.getSerializable("Category");
        edittextEdit.setText(category.getCategoryName());

        if (icon == 0) {
            icon = category.getCategoryIcon();
        }
        imageEdit.setImageResource(icon);

        //events
        imageBack.setOnClickListener(this);
        textSave.setOnClickListener(this);
        imageEdit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_category_edit:
                chooseIcon();
                break;
            case R.id.text_save_category_edit:
                editCategory();
                break;
            case R.id.image_back_edit_category:
                manager.popBackStack();
                break;
            default:
                break;
        }
    }


    private void chooseIcon() {
        IconCategoryFragment iconCategoryFragment = new IconCategoryFragment();
        iconCategoryFragment.setTargetFragment(this, AppKey.FRAGMENT_CODE);
        transaction = manager.beginTransaction();
        transaction.replace(R.id.Frame_Content, iconCategoryFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void editCategory() {
        AppDatabase db = Room.databaseBuilder(getContext(),
                AppDatabase.class, AppKey.NAME_DATABASE)
                .allowMainThreadQueries()
                .build();

        String nameCategory = edittextEdit.getText().toString().trim();

        if (TextUtils.isEmpty(nameCategory)) {
            edittextEdit.setError("Bạn cần nhập tên nhóm");
        } else {
            int checkCategory = db.categoryDao().getCountByName(nameCategory);
            if (checkCategory != 0) {
                Toast.makeText(getActivity(), "Tên nhóm đã được sử dụng", Toast.LENGTH_SHORT).show();
            } else {
                db.categoryDao().updateByID(category.getIdCategory(), nameCategory, icon);
//                transaction = manager.beginTransaction();
//                transaction.replace(R.id.Frame_Content, new ListCategoryFragment());
//                transaction.commit();
                manager = getFragmentManager();
                manager.popBackStack();
//                if (((Home) getActivity()).homeToolbar.getVisibility() == View.GONE) {
//                    ((Home) getActivity()).homeToolbar.setVisibility(View.VISIBLE);
//                }
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
