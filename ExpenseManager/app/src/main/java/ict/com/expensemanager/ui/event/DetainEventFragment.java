package ict.com.expensemanager.ui.event;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ict.com.expensemanager.R;
import ict.com.expensemanager.data.database.AppDatabase;
import ict.com.expensemanager.data.database.entity.Event;
import ict.com.expensemanager.ui.custom.AutoFormatUtil;
import ict.com.expensemanager.ui.report.ReportActivity;
import ict.com.expensemanager.ui.slide_menu.Home;
import ict.com.expensemanager.util.AppKey;

/**
 * Created by PHAMHOAN on 1/24/2018.
 */

public class DetainEventFragment extends Fragment {
    @BindView(R.id.imv_back)
    ImageView imvBack;
    @BindView(R.id.imv_edit)
    ImageView imvEdit;
    @BindView(R.id.imv_delete)
    ImageView imvDelete;
    @BindView(R.id.txt_event_name)
    TextView txtEventName;
    @BindView(R.id.txt_expected_money)
    TextView txtExpectedMoney;
    @BindView(R.id.txt_transaction_money)
    TextView txtTransactionMoney;
    @BindView(R.id.txt_balance)
    TextView txtBalance;
    @BindView(R.id.txt_transaction_count)
    TextView txtTransactionCount;
    @BindView(R.id.btn_report)
    Button btnReport;
    Unbinder unbinder;
    int idevent;
    private View rootView;

    private Event event;
    private Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_event_detain, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (((Home) getActivity()).homeToolbar.getVisibility() == View.VISIBLE) {
            ((Home) getActivity()).homeToolbar.setVisibility(View.GONE);
        }
        initialize();
    }

    private void initialize() {

        bundle = getArguments();

        idevent = bundle.getInt(AppKey.ID_EVENT);
        if (idevent != 0) {
            AppDatabase db = Room.databaseBuilder(getContext(),
                    AppDatabase.class, AppKey.NAME_DATABASE)
                    .allowMainThreadQueries()
                    .build();
            event = db.eventDao().getEventById(idevent);
            txtEventName.setText(event.getEventName());

            String balance = AutoFormatUtil.formatToStringWithoutDecimal(event.getBalance());
            String expected = AutoFormatUtil.formatToStringWithoutDecimal(event.getExpectedMoney());
            String tracsactionMoney = AutoFormatUtil.formatToStringWithoutDecimal(db.transactionDao().transactionMoneyByEvent(event.getIdEvent()));
            int countTransaction = db.transactionDao().transactionByEvent(event.getIdEvent());
            Log.d("COUNTID", "initialize: " + countTransaction);
            txtTransactionCount.setText(countTransaction + "");
            txtExpectedMoney.setText(expected);
            txtBalance.setText(balance);
            txtTransactionMoney.setText(tracsactionMoney);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.imv_back, R.id.imv_edit, R.id.imv_delete, R.id.btn_report})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imv_back:
                if (((Home) getActivity()).homeToolbar.getVisibility() == View.GONE) {
                    ((Home) getActivity()).homeToolbar.setVisibility(View.VISIBLE);
                }
                getActivity().onBackPressed();
                break;
            case R.id.imv_edit:
                editEvent();
                break;
            case R.id.imv_delete:
                callDelete();
                break;
            case R.id.btn_report:
                callReport();
                break;
        }
    }

    private void editEvent() {
        bundle = new Bundle();
        bundle.putSerializable(AppKey.EVENT, event);
        EditEventFragment editEventFragment = new EditEventFragment();
        editEventFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, editEventFragment)
                .addToBackStack(null)
                .commit();
    }

    private void callDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn có chắc muốn xóa ?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppDatabase db = Room.databaseBuilder(getContext(),
                        AppDatabase.class, AppKey.NAME_DATABASE)
                        .allowMainThreadQueries()
                        .build();
                db.eventDao().deleteEvent(event);
                Intent intent = new Intent(AppKey.ACTION_UPDATE);
                getActivity().sendBroadcast(intent);
                getActivity().onBackPressed();
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
        if (((Home) getActivity()).homeToolbar.getVisibility() == View.GONE) {
            ((Home) getActivity()).homeToolbar.setVisibility(View.VISIBLE);
        }
    }

    private void callReport() {
        Intent intent = new Intent(getActivity(), ReportActivity.class);
        intent.putExtra(AppKey.KEY_CALL_REPORT, AppKey.KEY_EVENT_REPORT);
        intent.putExtra(AppKey.KEY_CALL_DATA_REPORT, idevent);

        startActivity(intent);
    }

}
