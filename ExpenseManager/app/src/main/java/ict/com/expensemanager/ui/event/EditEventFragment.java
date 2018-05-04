package ict.com.expensemanager.ui.event;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ict.com.expensemanager.R;
import ict.com.expensemanager.data.database.AppDatabase;
import ict.com.expensemanager.data.database.entity.Event;
import ict.com.expensemanager.ui.custom.AutoFormatEditText;
import ict.com.expensemanager.ui.custom.AutoFormatUtil;
import ict.com.expensemanager.util.AppKey;

/**
 * Created by PHAMHOAN on 1/24/2018.
 */

public class EditEventFragment extends Fragment {
    View rootView;
    @BindView(R.id.imv_back)
    ImageView imvBack;
    @BindView(R.id.txt_save)
    TextView txtSave;
    @BindView(R.id.edt_event_name)
    EditText edtEventName;
    @BindView(R.id.edt_expected_money)
    AutoFormatEditText edtExpectedMoney;
    @BindView(R.id.edt_balance)
    AutoFormatEditText edtBalance;
    @BindView(R.id.check_box_running)
    CheckBox checkBoxRunning;
    Unbinder unbinder;
    Event event;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_edit_event, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
    }

    private void initialize() {
        Bundle bundle = getArguments();

        event = (Event) bundle.getSerializable(AppKey.EVENT);
        if (event != null) {
            String balance = AutoFormatUtil.formatToStringWithoutDecimal(event.getBalance());
            String expected = AutoFormatUtil.formatToStringWithoutDecimal(event.getExpectedMoney());
            boolean check = event.isStatus();

            edtEventName.setText(event.getEventName().toString());
            edtEventName.setFocusable(true);
            edtExpectedMoney.setText(expected);
            edtBalance.setText(balance);

            if (check) {
                checkBoxRunning.setChecked(true);
            } else {
                checkBoxRunning.setChecked(false);
            }
        }
        edtExpectedMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edtBalance.setText(edtExpectedMoney.getText().toString());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.imv_back, R.id.txt_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imv_back:
                getActivity().onBackPressed();
                break;
            case R.id.txt_save:
                checkEvent();
                break;
        }
    }

    private void checkEvent() {
        if (edtEventName.getText().toString().isEmpty() || edtBalance.getText().toString().isEmpty()
                || edtExpectedMoney.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Hãy nhập đủ dữ liệu", Toast.LENGTH_SHORT).show();
        } else {
            final double expectedMoney = Double.parseDouble(edtExpectedMoney.getText()
                    .toString().replace(",", "").replace(".", ""));
            final double balance = Double.parseDouble(edtBalance.getText()
                    .toString().replace(",", "").replace(".", ""));
            final boolean check = checkBoxRunning.isChecked();

            event.setBalance(balance);
            event.setEventName(edtEventName.getText().toString());
            event.setExpectedMoney(expectedMoney);
            event.setStatus(check);

            final AppDatabase db = Room.databaseBuilder(getActivity(),
                    AppDatabase.class, AppKey.NAME_DATABASE).allowMainThreadQueries().build();
            int a = db.eventDao().getCountName(edtEventName.getText().toString(),event.getIdUser());
            if (a != 0) {
                Toast.makeText(getActivity(), "Tên đã dược sử dụng", Toast.LENGTH_SHORT).show();
                return;
            } else if (balance > expectedMoney) {
                Toast.makeText(getActivity(), "Tiền còn lại phải nhỏ hơn tiền dự kiến", Toast.LENGTH_SHORT).show();
                return;
            } else {
                db.eventDao().updateEvent(event);
                Toast.makeText(getActivity(), "Thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AppKey.ACTION_UPDATE);
                getActivity().sendBroadcast(intent);
                getActivity().onBackPressed();
            }
        }

    }


}
