package ict.com.expensemanager.ui.transaction;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ict.com.expensemanager.R;
import ict.com.expensemanager.ui.base.BaseDialogFragment;
import ict.com.expensemanager.util.Commons;


public class DayPickerDialogFragment extends BaseDialogFragment implements View.OnClickListener {


    @BindView(R.id.grid_view_days)
    GridView gridViewDays;
    @BindView(R.id.button_accept)
    Button buttonAccept;
    @BindView(R.id.button_cancel)
    Button buttonCancel;
    Unbinder unbinder;

    private View oldView = null;
    private View currentView = null;
    private int[] days = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31};
    private int currentPosition = -1;
    private InteractionWithDayPickerDialog interaction;


    public DayPickerDialogFragment() {
        // Required empty public constructor
    }

    public static DayPickerDialogFragment newInstance() {

        Bundle args = new Bundle();

        DayPickerDialogFragment fragment = new DayPickerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TransactionAddingActivity) {
            interaction = (InteractionWithDayPickerDialog) context;
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_day_picker_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUiEvents();
        setupGridViewDays();

    }

    private void setupUiEvents() {

        buttonAccept.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);


        gridViewDays.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (oldView == null) {
                    oldView  = view;
                    currentView = view;
                    currentView.setSelected(true);
                } else {
                    oldView.setSelected(false);
                    currentView = view;
                    currentView.setSelected(true);
                    oldView = currentView;
                }

                currentPosition = i;

            }
        });
    }

    private void setupGridViewDays() {
        gridViewDays.setAdapter(new DayPickerAdapter(getActivity(), days));
    }

    @Override
    public void onResume() {
        Commons.setSizeDialog(getDialog());
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();

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

    private void onClickButtonAccept() {

        if (currentPosition == -1) {

            showMessage("Chưa có ngày nào được chọn, mời nhập lại!");
        } else {

            interaction.setRepeatDay(days[currentPosition]);
            showMessage("Chọn thành công");
            dismiss();
        }
    }

    private void onClickButtonCancel() {

        dismiss();
    }


    public interface InteractionWithDayPickerDialog {

        void setRepeatDay(int repeatDay);
    }
}
