package ict.com.expensemanager.ui.category;


import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ict.com.expensemanager.R;
import ict.com.expensemanager.data.database.AppDatabase;
import ict.com.expensemanager.data.database.entity.Category;
import ict.com.expensemanager.ui.custom.AutoFormatUtil;
import ict.com.expensemanager.ui.report.ReportActivity;
import ict.com.expensemanager.ui.slide_menu.Home;
import ict.com.expensemanager.util.AppKey;

public class DetailsCategoryFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.image_back_category_details)
    ImageView imageBack;
    @BindView(R.id.image_delete_category)
    ImageView imageDelete;
    @BindView(R.id.text_category_name)
    TextView textCategoryName;
    @BindView(R.id.text_category_money)
    TextView textCategoryMoney;
    @BindView(R.id.text_category_transaction)
    TextView textCategoryTransaction;
    @BindView(R.id.button_view_report_category)
    Button buttonViewReport;
    Unbinder unbinder;
    @BindView(R.id.image_edit_category)
    ImageView imageEdit;
    int idCategory;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private Bundle bundle;
    private Category category;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details_category, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //events
        imageDelete.setOnClickListener(this);
        imageEdit.setOnClickListener(this);
        imageBack.setOnClickListener(this);
        buttonViewReport.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_delete_category:
                deleteCategory();
                break;
            case R.id.image_edit_category:
                gotoFragmentEditCategory();
                break;
            case R.id.image_back_category_details:
                checkOpenToolbar();
                manager.popBackStack();
                break;
            case R.id.button_view_report_category:
                callReport();
                break;
            default:
                break;
        }
    }

    private void gotoFragmentEditCategory() {
        EditCategoryFragment editCategoryFragment = new EditCategoryFragment();
        transaction = manager.beginTransaction();
        bundle.putSerializable("Category",category);
        editCategoryFragment.setArguments(bundle);
        transaction.replace(R.id.Frame_Content, editCategoryFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (((Home) getActivity()).homeToolbar.getVisibility() == View.VISIBLE) {
            ((Home) getActivity()).homeToolbar.setVisibility(View.GONE);
        }
        manager = getFragmentManager();
        AppDatabase db = Room.databaseBuilder(getContext(),
                AppDatabase.class, AppKey.NAME_DATABASE)
                .allowMainThreadQueries()
                .build();

        bundle = getArguments();
        category = (Category) bundle.getSerializable("Category");
        textCategoryName.setText(db.categoryDao().getCategoryNameReport(category.getIdCategory()));
        idCategory = category.getIdCategory();
        int countTransaction = db.transactionDao().transactionByCategory(category.getIdCategory());
        textCategoryTransaction.setText(countTransaction + "");
        String money = AutoFormatUtil.formatToStringWithoutDecimal(db.transactionDao().transactionMoneyByCategory(category.getIdCategory()));
        textCategoryMoney.setText(money);
        category = db.categoryDao().getCategoryByIdCategory(category.getIdCategory());
    }

    private void deleteCategory() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Xóa danh mục");
        builder.setMessage("Bạn chắc chắn muốn xóa danh mục này không?");
        builder.setCancelable(false);
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AppDatabase db = Room.databaseBuilder(getContext(),
                        AppDatabase.class, AppKey.NAME_DATABASE)
                        .allowMainThreadQueries()
                        .build();
                db.categoryDao().deleleByID(category.getIdCategory());

                transaction = manager.beginTransaction();
                transaction.replace(R.id.Frame_Content, new ListCategoryFragment());
                transaction.commit();
                checkOpenToolbar();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void checkOpenToolbar() {
        if (((Home) getActivity()).homeToolbar.getVisibility() == View.GONE) {
            ((Home) getActivity()).homeToolbar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void callReport() {
        Intent intent = new Intent(getActivity(), ReportActivity.class);
        intent.putExtra(AppKey.KEY_CALL_REPORT, AppKey.KEY_CATEGORY_REPORT);
        intent.putExtra(AppKey.KEY_CALL_DATA_REPORT, idCategory);
        startActivity(intent);
    }
}
