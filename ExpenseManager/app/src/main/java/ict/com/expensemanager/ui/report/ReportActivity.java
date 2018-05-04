package ict.com.expensemanager.ui.report;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ict.com.expensemanager.R;
import ict.com.expensemanager.data.database.AppDatabase;
import ict.com.expensemanager.data.database.dao.TransactionDao;
import ict.com.expensemanager.data.database.entity.Transaction;
import ict.com.expensemanager.data.preferences.SharePreferencesManager;
import ict.com.expensemanager.util.AppKey;
import ict.com.expensemanager.util.Commons;

public class ReportActivity extends Activity implements IReport {
    long dateEnd, dateStart;
    @BindView(R.id.imv_back)
    ImageView imvBack;
    List<TransactionDao.timeTransaction> dataTimeTransactionList = new ArrayList<>();
    List<Transaction> dataTransactionDetailsList = new ArrayList<>();
    TransactionReprotTimeAdapter mRcvAdapterTimeTransaction;
    TransactionReportAdapter mRcvAdapterTransaction;
    @BindView(R.id.txt_select)
    TextView txt_select;
    @BindView(R.id.txt_DayStart)
    TextView txt_daystart;
    @BindView(R.id.txt_DayEnd)
    TextView txt_dayend;
    @BindView(R.id.txtReport)
    TextView txtReport;
    @BindView(R.id.txt_filter)
    TextView txt_filter;
    private String result = "";
    private String[] list = {"Theo danh mục", "Theo ví", "Theo sự kiện", "Theo thời gian"};
    RecyclerView mRecyclerView;
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    AppDatabase db;
    String[] eventName;
    String[] walletName;
    String[] categoryName;
    int eventId;
    int walletId;
    int categoryId;
    Calendar calendarStart, calendar1End;
    AlertDialog alertDialog;
    int positionDialog;
    int idUser;
    Spinner spinnerTP;
    ArrayAdapter<String> adapter;
    int report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);
        SharedPreferences sharedPreferences = SharePreferencesManager.getInstance(getApplicationContext());
        idUser = sharedPreferences.getInt(AppKey.KEY_USER_ID, 0);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, AppKey.DATABASE_NAME)
                .allowMainThreadQueries()
                .build();
        db.getOpenHelper().getWritableDatabase();

        spinnerTP = (Spinner) findViewById(R.id.spn_List);
        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerTP.setAdapter(adapter);
        callData();
        spinner();
    }

    public void callData() {
        Intent intent = getIntent();
        if (intent != null) {
            report = intent.getIntExtra(AppKey.KEY_CALL_REPORT, 0);
            switch (report) {
                case AppKey.KEY_CATEGORY_REPORT:
                    spinnerTP.setSelection(0);
                    categoryId = intent.getIntExtra(AppKey.KEY_CALL_DATA_REPORT, 0);
                    dataTransactionDetailsList = db.transactionDao().getTransactionByCategory(categoryId);
                    mRcvAdapterTransaction = new TransactionReportAdapter(dataTransactionDetailsList, getApplicationContext(), ReportActivity.this);
                    mRecyclerView.setAdapter(mRcvAdapterTransaction);
                    break;
                case AppKey.KEY_WALLET_REPORT:
                    spinnerTP.setSelection(1);
                    walletId = intent.getIntExtra(AppKey.KEY_CALL_DATA_REPORT, 0);
                    dataTransactionDetailsList = db.transactionDao().getTransactionByWallet(walletId);
                    mRcvAdapterTransaction = new TransactionReportAdapter(dataTransactionDetailsList, getApplicationContext(), ReportActivity.this);
                    mRecyclerView.setAdapter(mRcvAdapterTransaction);
                    break;
                case AppKey.KEY_EVENT_REPORT:
                    spinnerTP.setSelection(2);
                    eventId = intent.getIntExtra(AppKey.KEY_CALL_DATA_REPORT, 0);
                    dataTransactionDetailsList = db.transactionDao().getTransactionByEvent(eventId);
                    mRcvAdapterTransaction = new TransactionReportAdapter(dataTransactionDetailsList, getApplicationContext(), ReportActivity.this);
                    mRecyclerView.setAdapter(mRcvAdapterTransaction);
                    break;
            }
        }
    }

    public void spinner() {
        spinnerTP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        txt_dayend.setVisibility(View.INVISIBLE);
                        txt_daystart.setVisibility(View.INVISIBLE);
                        txt_select.setVisibility(View.VISIBLE);
                        txt_select.setText("Chọn tên danh mục");
                        AlertDialog.Builder builder = new AlertDialog.Builder(ReportActivity.this);
                        DialogInterface.OnClickListener actionListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        };
                        categoryName = db.categoryDao().getCategorName(idUser);
                        builder.setTitle("Tên danh mục");
                        builder.setItems(categoryName, actionListener);
                        builder.setNegativeButton("Thoát", null);
                        alertDialog = builder.create();
                        builder.setSingleChoiceItems(list, 0, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                positionDialog = item;
                                txt_select.setText(categoryName[item]);
                                alertDialog.cancel();
                            }
                        });
                        txt_select.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.show();
                            }
                        });
                        if (report == 1) {
                            String s = db.categoryDao().getCategoryNameReport(categoryId);
                            txt_select.setText(s);
                        }
                        txt_filter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String a = txt_select.getText().toString();
                                if (categoryName.length != 0) {
                                    if (a.equals(categoryName[positionDialog])) {
                                        mRecyclerView.setVisibility(mRecyclerView.VISIBLE);
                                        categoryId = db.categoryDao().getCategoryId(categoryName[positionDialog]);
                                        dataTransactionDetailsList = db.transactionDao().getTransactionByCategory(categoryId);
                                        mRcvAdapterTransaction = new TransactionReportAdapter(dataTransactionDetailsList, getApplicationContext(), ReportActivity.this);
                                        mRecyclerView.setAdapter(mRcvAdapterTransaction);
                                    } else {
                                        mRcvAdapterTransaction.clearList();
                                        Toast.makeText(ReportActivity.this, "Hãy chọn danh mục cần xem", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    mRcvAdapterTransaction.clearList();
                                    Toast.makeText(ReportActivity.this, "không có giao dịch nào", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        break;
                    case 1:
                        txt_dayend.setVisibility(View.INVISIBLE);
                        txt_daystart.setVisibility(View.INVISIBLE);
                        txt_select.setVisibility(View.VISIBLE);
                        txt_select.setText("Chọn tên ví");
                        builder = new AlertDialog.Builder(ReportActivity.this);
                        actionListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        };
                        walletName = db.walletDao().getWalletName(idUser);
                        builder.setTitle("Tên ví");
                        builder.setItems(walletName, actionListener);
                        builder.setNegativeButton("Thoát", null);
                        alertDialog = builder.create();
                        builder.setSingleChoiceItems(list, 0, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                positionDialog = item;
                                txt_select.setText(walletName[item]);
                                alertDialog.cancel();
                            }
                        });
                        txt_select.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.show();
                            }
                        });
                        if (report == 2) {
                            String s = db.walletDao().getWalletNameReport(walletId);
                            txt_select.setText(s);
                        }
                        txt_filter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String a = txt_select.getText().toString();
                                if (walletName.length != 0) {
                                    if (a.equals(walletName[positionDialog])) {
                                        mRecyclerView.setVisibility(mRecyclerView.VISIBLE);
                                        walletId = db.walletDao().getWalletId(walletName[positionDialog]);
                                        dataTransactionDetailsList = db.transactionDao().getTransactionByWallet(walletId);
                                        mRcvAdapterTransaction = new TransactionReportAdapter(dataTransactionDetailsList, getApplicationContext(), ReportActivity.this);
                                        mRecyclerView.setAdapter(mRcvAdapterTransaction);

                                    } else {
                                        mRcvAdapterTransaction.clearList();
                                        Toast.makeText(ReportActivity.this, "Hãy chọn ví cần xem", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    mRcvAdapterTransaction.clearList();
                                    Toast.makeText(ReportActivity.this, "không có giao dịch nào", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        break;
                    case 2:
                        txt_dayend.setVisibility(View.INVISIBLE);
                        txt_daystart.setVisibility(View.INVISIBLE);
                        txt_select.setVisibility(View.VISIBLE);
                        txt_select.setText("Chọn tên sự kiện");
                        builder = new AlertDialog.Builder(ReportActivity.this);
                        actionListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        };
                        eventName = db.eventDao().getEventName(idUser);
                        builder.setTitle("Tên sự kiện");
                        builder.setItems(eventName, actionListener);
                        builder.setNegativeButton("Thoát", null);
                        alertDialog = builder.create();
                        builder.setSingleChoiceItems(list, 0, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                positionDialog = item;
                                txt_select.setText(eventName[item]);
                                alertDialog.cancel();
                            }
                        });
                        txt_select.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.show();
                            }
                        });
                        if (report == 3) {
                            String s = db.eventDao().getEventNameReport(eventId);
                            txt_select.setText(s);
                        }
                        txt_filter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String a = txt_select.getText().toString();
                                if (eventName.length != 0) {
                                    if (a.equals(eventName[positionDialog])) {
                                        mRecyclerView.setVisibility(mRecyclerView.VISIBLE);
                                        eventId = db.eventDao().getEventId(eventName[positionDialog]);
                                        dataTransactionDetailsList = db.transactionDao().getTransactionByEvent(eventId);
                                        mRcvAdapterTransaction = new TransactionReportAdapter(dataTransactionDetailsList, getApplicationContext(), ReportActivity.this);
                                        mRecyclerView.setAdapter(mRcvAdapterTransaction);
                                    } else {
                                        mRcvAdapterTransaction.clearList();
                                        Toast.makeText(ReportActivity.this, "Hãy chọn sự kiện cần xem", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    mRcvAdapterTransaction.clearList();
                                    Toast.makeText(ReportActivity.this, "không có giao dịch nào", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        break;
                    case 3:
                        txt_daystart.setVisibility(View.VISIBLE);
                        txt_dayend.setVisibility(View.VISIBLE);
                        txt_select.setVisibility(View.GONE);
                        dateTimeStart();
                        dateTimeEnd();
                        txt_filter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (dateStart != 0 && dateEnd != 0) {
                                    mRecyclerView.setVisibility(mRecyclerView.VISIBLE);
                                    dataTimeTransactionList = db.transactionDao().getByDate(idUser, dateStart, dateEnd);
                                    mRcvAdapterTimeTransaction = new TransactionReprotTimeAdapter(dataTimeTransactionList, getApplicationContext(), ReportActivity.this);
                                    mRecyclerView.setAdapter(mRcvAdapterTimeTransaction);
                                } else {
                                    mRcvAdapterTransaction.clearList();
                                    Toast.makeText(ReportActivity.this, "không có giao dịch nào", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                result = "";
            }
        });
    }

    public void dateTimeStart() {
        calendarStart = Calendar.getInstance();
        txt_daystart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        txt_daystart.setText(day + "/" + (month + 1) + "/" + year);
                        String s = txt_daystart.getText().toString();
                        dateStart = Commons.convertStringDateToLong(s);
                    }
                };
                int date = calendarStart.get(Calendar.DAY_OF_MONTH);
                int month = calendarStart.get(Calendar.MONTH);
                int year = calendarStart.get(Calendar.YEAR);
                DatePickerDialog pic = new DatePickerDialog(
                        ReportActivity.this,
                        callback, year, month, date);
                pic.setTitle("Chọn ngày kết thúc");
                pic.show();
            }
        });
    }

    public void dateTimeEnd() {
        calendar1End = Calendar.getInstance();
        txt_dayend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        txt_dayend.setText(day + "/" + (month + 1) + "/" + year);
                        String s = txt_dayend.getText().toString();
                        dateEnd = Commons.convertStringDateToLong(s);
                    }
                };
                int date = calendar1End.get(Calendar.DAY_OF_MONTH);
                int month = calendar1End.get(Calendar.MONTH);
                int year = calendar1End.get(Calendar.YEAR);
                DatePickerDialog pic = new DatePickerDialog(
                        ReportActivity.this,
                        callback, year, month, date);
                pic.setTitle("Chọn ngày kết thúc");
                pic.show();
            }
        });
    }

    @OnClick(R.id.imv_back)
    public void onViewClicked() {
        finish();
    }


    @Override
    public void showText() {
        txtReport.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideText() {
        txtReport.setVisibility(View.GONE);

    }
}
