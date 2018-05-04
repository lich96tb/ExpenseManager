package ict.com.expensemanager.ui.event;

import android.arch.persistence.room.Room;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import butterknife.BindView;
import butterknife.ButterKnife;
import ict.com.expensemanager.R;
import ict.com.expensemanager.data.database.AppDatabase;
import ict.com.expensemanager.data.database.entity.Event;
import ict.com.expensemanager.data.preferences.SharePreferencesManager;
import ict.com.expensemanager.ui.custom.AutoFormatEditText;
import ict.com.expensemanager.util.AppKey;

/**
 * Created by PHAMHOAN on 1/22/2018.
 */

public class AddEventActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.imv_back)
    ImageView imvBack;
    @BindView(R.id.edt_event_name)
    EditText edtEventName;
    @BindView(R.id.check_box_running)
    CheckBox checkBoxRunning;
    @BindView(R.id.txt_save)
    TextView txtSave;
    @BindView(R.id.edt_expected_money)
    AutoFormatEditText edtExpectedMoney;
    @BindView(R.id.edt_balance)
    AutoFormatEditText edtBalance;
    private int userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        imvBack.setOnClickListener(this);
        txtSave.setOnClickListener(this);
        SharedPreferences sharedPreferences = SharePreferencesManager.getInstance(getApplicationContext());
        userId = sharedPreferences.getInt(AppKey.KEY_USER_ID, 0);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_back:
                finish();
                break;
            case R.id.txt_save:
                checkEvent();
                break;
            default:
                break;
        }
    }

    private void checkEvent() {
        if (edtEventName.getText().toString().isEmpty() || edtBalance.getText().toString().isEmpty()
                || edtExpectedMoney.getText().toString().isEmpty()) {
            Toast.makeText(this, "Hãy nhập đủ dữ liệu", Toast.LENGTH_SHORT).show();
        } else {
            double expectedMoney = Double.parseDouble(edtExpectedMoney.getText()
                    .toString().replace(",", "").replace(".", ""));
            final double balance = Double.parseDouble(edtBalance.getText()
                    .toString().replace(",", "").replace(".", ""));
            final boolean check = checkBoxRunning.isChecked();

            final AppDatabase db = Room.databaseBuilder(this,
                    AppDatabase.class, AppKey.NAME_DATABASE).allowMainThreadQueries().build();

            int a = db.eventDao().getCountName(edtEventName.getText().toString(), userId);
            if (a != 0) {
                Toast.makeText(AddEventActivity.this, "Tên đã dược sử dụng", Toast.LENGTH_SHORT).show();
                return;
            } else if (balance > expectedMoney) {
                Toast.makeText(AddEventActivity.this, "Tiền còn lại phải nhỏ hơn tiền dự kiến", Toast.LENGTH_SHORT).show();
                return;
            } else if (expectedMoney < 0) {
                Toast.makeText(AddEventActivity.this, "Tiền dự kiến không được âm", Toast.LENGTH_SHORT).show();
                return;
            } else {
                db.eventDao().addEvent(new Event(edtEventName.getText().toString()
                        , expectedMoney, balance, check, userId));
                Toast.makeText(AddEventActivity.this, "Thành công", Toast.LENGTH_SHORT).show();

                finish();
            }


        }
    }
}
