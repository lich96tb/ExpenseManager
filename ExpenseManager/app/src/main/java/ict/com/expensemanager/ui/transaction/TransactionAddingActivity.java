package ict.com.expensemanager.ui.transaction;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ict.com.expensemanager.R;
import ict.com.expensemanager.data.database.AppDatabase;
import ict.com.expensemanager.data.database.entity.Category;
import ict.com.expensemanager.data.database.entity.Event;
import ict.com.expensemanager.data.database.entity.Partner;
import ict.com.expensemanager.data.database.entity.Repeat;
import ict.com.expensemanager.data.database.entity.RepeatType;
import ict.com.expensemanager.data.database.entity.Transaction;
import ict.com.expensemanager.data.database.entity.TransactionPartner;
import ict.com.expensemanager.data.database.entity.Wallet;
import ict.com.expensemanager.data.preferences.SharePreferencesManager;
import ict.com.expensemanager.ui.base.BaseActivity;
import ict.com.expensemanager.ui.custom.AutoFormatEditText;
import ict.com.expensemanager.util.AppKey;
import ict.com.expensemanager.util.Commons;
import ict.com.expensemanager.util.Validations;

import static ict.com.expensemanager.util.AppKey.DAY_OF_WEEK.FRIDAY;
import static ict.com.expensemanager.util.AppKey.DAY_OF_WEEK.MONDAY;
import static ict.com.expensemanager.util.AppKey.DAY_OF_WEEK.SATURDAY;
import static ict.com.expensemanager.util.AppKey.DAY_OF_WEEK.SUNDAY;
import static ict.com.expensemanager.util.AppKey.DAY_OF_WEEK.THURSDAY;
import static ict.com.expensemanager.util.AppKey.DAY_OF_WEEK.TUESDAY;
import static ict.com.expensemanager.util.AppKey.DAY_OF_WEEK.WEDNESDAY;

public class TransactionAddingActivity extends BaseActivity
        implements View.OnClickListener,
        WalletsDialogFragment.InteractionWithWalletsDialog,
        CategoriesDialogFragment.InteractionWithCategoriesDialog,
        EventsDialogFragment.InteractionWithEventsDialog,
        PartnersDialogFragment.InteractionWithPartnersDialog,
        DayPickerDialogFragment.InteractionWithDayPickerDialog

{

    @BindView(R.id.button_back)
    ImageButton buttonBack;
    @BindView(R.id.text_title_toolbar)
    TextView textTitleToolbar;
    @BindView(R.id.text_save_data)
    TextView textSaveData;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edit_text_transaction_name)
    EditText editTextTransactionName;
    @BindView(R.id.edit_text_transaction_price)
    AutoFormatEditText editTextTransactionPrice;
    @BindView(R.id.text_layout_transaction_price)
    TextInputLayout textLayoutTransactionPrice;
    @BindView(R.id.button_select_wallet)
    Button buttonSelectWallet;
    @BindView(R.id.button_select_category)
    Button buttonSelectCategory;
    @BindView(R.id.button_select_event)
    Button buttonSelectEvent;
    @BindView(R.id.button_select_date)
    Button buttonSelectDate;
    @BindView(R.id.text_select_partner)
    TextView textSelectPartner;
    @BindView(R.id.spinner_repeat_mode)
    Spinner spinnerRepeatMode;
    @BindView(R.id.spinner_repeat_count)
    Spinner spinnerRepeatCount;
    @BindView(R.id.edit_text_repeat_count)
    EditText editTextRepeatCount;
    @BindView(R.id.button_select_repeat_date)
    Button buttonSelectRepeatDate;
    @BindView(R.id.text_input_layout_transaction_name)
    TextInputLayout textLayoutTransactionName;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.text_title_repeat_count_date)
    TextView textTitleRepeatCountDate;

    private Wallet walletSelected = null;
    private Category categorySelected = null;
    private Event eventSelected = null;
    private List<Partner> partnersSelected = null;
    private RepeatTypeAdapter repeatTypeAdapter = null;
    private ArrayAdapter<String> repeatCountAdapter = null;
    private int userId;

    private String[] dayOfWeeks = new String[]{
            MONDAY.getName(),
            AppKey.DAY_OF_WEEK.TUESDAY.getName(),
            AppKey.DAY_OF_WEEK.WEDNESDAY.getName(),
            AppKey.DAY_OF_WEEK.THURSDAY.getName(),
            AppKey.DAY_OF_WEEK.FRIDAY.getName(),
            AppKey.DAY_OF_WEEK.SATURDAY.getName(),
            AppKey.DAY_OF_WEEK.SUNDAY.getName()
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_adding);
        ButterKnife.bind(this);
        //
        handleIntent();
        setupUis();
        setupUiEvents();
        requestReadContactPermission();
        setupAlarmTransactionEveryDay(4);
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getIntExtra(AppKey.KEY_USER_ID, 0);
        }
    }


    private void setupUiEvents() {
        buttonBack.setOnClickListener(this);
        buttonSelectCategory.setOnClickListener(this);
        buttonSelectDate.setOnClickListener(this);
        buttonSelectEvent.setOnClickListener(this);
        buttonSelectWallet.setOnClickListener(this);
        textSaveData.setOnClickListener(this);
        textSelectPartner.setOnClickListener(this);
        buttonSelectRepeatDate.setOnClickListener(this);


        //
        spinnerRepeatMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                RepeatType repeatTypeSelected = (RepeatType) adapterView.getSelectedItem();
                if (repeatTypeSelected != null) {
                    buttonSelectRepeatDate.setText(getString(R.string.error_empty));
                    setTextTitleRepeatCountDate(repeatTypeSelected.getIdRepeatType());
                    showMessage(textTitleRepeatCountDate.getText().toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerRepeatCount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String repeatCount = (String) adapterView.getSelectedItem();

                switch (repeatCount) {

                    case AppKey.INFINITE_REPEAT_COUNT:
                        if (editTextRepeatCount.isEnabled()) {
                            editTextRepeatCount.setEnabled(false);
                        }
                        break;
                    case AppKey.CUSTOM_REPEAT_COUNT:
                        if (!editTextRepeatCount.isEnabled()) {
                            editTextRepeatCount.setEnabled(true);
                        }

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }


    private void setupUis() {
        setupToolbar();
        initButtonSelectDate();
        setupSpinnerRepeatType();
        setupSpinnerRepeatCount();
        setupScrollView();
        //


    }

    private void setupScrollView() {
        scrollView.fullScroll(View.FOCUS_DOWN);
    }

    private void setupSpinnerRepeatCount() {
        List<String> repeatCounts = new ArrayList<>();
        repeatCounts.add(AppKey.INFINITE_REPEAT_COUNT);
        repeatCounts.add(AppKey.CUSTOM_REPEAT_COUNT);

        repeatCountAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, repeatCounts);
        spinnerRepeatCount.setAdapter(repeatCountAdapter);
    }


    private void setupSpinnerRepeatType() {

        RepeatTypeAsyncTask repeatTypeAsyncTask = new RepeatTypeAsyncTask(this);
        repeatTypeAsyncTask.execute();
    }

    public void setDataSpinnerRepeatType(List<RepeatType> repeatTypes) {

        repeatTypeAdapter = new RepeatTypeAdapter(this, R.layout.item_repeat_type, repeatTypes);
        spinnerRepeatMode.setAdapter(repeatTypeAdapter);
    }


    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void setTextTitleRepeatCountDate(int repeatType) {
        switch (repeatType) {
            case AppKey.MONTH_REPEAT_TYPE:
                textTitleRepeatCountDate.setText("Thời gian(Ngày)");
                break;
            case AppKey.WEEK_REPEAT_TYPE:
                textTitleRepeatCountDate.setText("Thời gian(Thứ)");
                break;
            case AppKey.DAY_REPEAT_TYPE:
                textTitleRepeatCountDate.setText("Thời gian(Giờ)");
                break;
        }
    }

    private void requestReadContactPermission() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {


            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        AppKey.MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            }
        }
    }

    private void onClickTextSaveData() {

        if (editTextRepeatCount.getText().toString().isEmpty()) {
            editTextRepeatCount.setText("0");
        }


        if (isUserInputValid() && walletSelected != null && isRepeatModeValid()) {

            Transaction transaction = new Transaction();
            transaction.setIdUser(userId);
            transaction.setTransactionName(editTextTransactionName.getText().toString());
            transaction.setPrice(Double.parseDouble(editTextTransactionPrice.getText().toString().replace(".", "")));
            transaction.setTime(Commons.convertStringDateToLong(buttonSelectDate.getText().toString()));

            //
            if (categorySelected != null) {
                transaction.setIdCategory(categorySelected.getIdCategory());
            }

            if (eventSelected != null) {
                transaction.setIdEvent(eventSelected.getIdEvent());
            }

            transaction.setIdWallet(walletSelected.getIdWallet());


            TransactionAddingTask transactionAddingTask = new TransactionAddingTask(this);
            transactionAddingTask.execute(transaction);


        }


        //show error message about repeat mode


    }

    private boolean isRepeatModeValid() {

        int repeatCount = Integer.parseInt(editTextRepeatCount.getText().toString());
        String repeatDate = buttonSelectRepeatDate.getText().toString();
        String repeatCountType = (String) spinnerRepeatCount.getSelectedItem();


        switch (repeatCountType) {
            case AppKey.INFINITE_REPEAT_COUNT:
                if (repeatCount != 0 && repeatDate.equalsIgnoreCase(getString(R.string.error_empty))) {

                    showMessage("Thời gian lặp chưa được chọn!");
                    return false;
                }


            case AppKey.CUSTOM_REPEAT_COUNT:
                if (repeatCount != 0 && repeatDate.equalsIgnoreCase(getString(R.string.error_empty))) {

                    showMessage("Thời gian lặp chưa được chọn!");
                    return false;
                }
                break;
        }


//        if (repeatCount != 0  & repeatDate.equalsIgnoreCase(getString(R.string.error_empty))) {
//            showMessage("Thời gian lặp chưa được chọn");
//            return false;
//        }


        return true;

    }


    private boolean isUserInputValid() {
        boolean isValidName = false;
        boolean isValidPrice = false;


        if (Validations.isTransactionNameValid(editTextTransactionName, textLayoutTransactionName)) {
            isValidName = true;
            textLayoutTransactionName.setError(null);
        } else {
            isValidName = false;
        }

        if (Validations.isTransactionPriceValid(editTextTransactionPrice, textLayoutTransactionPrice)) {
            isValidPrice = true;
            textLayoutTransactionPrice.setError(null);
        } else {
            isValidPrice = false;
        }

        return isValidName && isValidPrice;
    }

    private void initButtonSelectDate() {
        buttonSelectDate.setText(Commons.getCurrentDate());
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.text_save_data:
                onClickTextSaveData();
                break;

            case R.id.button_select_wallet:
                onClickButtonSelectWallet();
                break;

            case R.id.button_select_category:
                onClickButtonSelectCategory();
                break;

            case R.id.button_select_event:
                onClickButtonSelectEvent();
                break;

            case R.id.button_select_date:
                onClickButtonSelectDate();
                break;
            case R.id.text_select_partner:
                onClickTextSelectPartners();
                break;

            case R.id.button_select_repeat_date:
                onClickButtonSelectRepeatDate();
                break;
            case R.id.button_back:
                onClickButtonBack();
                break;

        }
    }

    private void onClickButtonBack() {
        finish();
    }

    private void onClickButtonSelectRepeatDate() {
        RepeatType repeatModeSelected = (RepeatType) spinnerRepeatMode.getSelectedItem();
        switch (repeatModeSelected.getIdRepeatType()) {

            case AppKey.MONTH_REPEAT_TYPE:
                DayPickerDialogFragment dialogFragment = DayPickerDialogFragment.newInstance();
                dialogFragment.show(getSupportFragmentManager(), AppKey.DAYS_PICKER_DIALOG_FRAGMENT_TAG);
                break;

            case AppKey.WEEK_REPEAT_TYPE:
                showDayOfWeekSelectionDialog();
                break;


            case AppKey.DAY_REPEAT_TYPE:
                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {

                        setRepeatTime(i, i1);

                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();

                break;
        }

    }


    private void setRepeatTime(int hour, int minute) {

        String time = "" + hour + ":" + minute;
        buttonSelectRepeatDate.setText(time);
    }


    private void onClickTextSelectPartners() {
        PartnersDialogFragment dialogFragment = PartnersDialogFragment.newInstance(partnersSelected);
        dialogFragment.show(getSupportFragmentManager(), AppKey.PARTNERS_DIALOG_FRAGMENT_TAG);
    }

    private void onClickButtonSelectDate() {
        Calendar calendar = Calendar.getInstance();
        //
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        //
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                buttonSelectDate.setText(new StringBuilder().append(i2).append("/").append(i1 + 1).append("/").append(i).toString());
            }
        }, currentYear, currentMonth, currentDay);

        datePickerDialog.show();

    }

    private void onClickButtonSelectEvent() {
        EventsDialogFragment eventDialog = EventsDialogFragment.newInstance();
        eventDialog.show(getSupportFragmentManager(), AppKey.EVENTS_DIALOG_FRAGMENT_TAG);
    }

    private void onClickButtonSelectCategory() {
        CategoriesDialogFragment categoryDialog = CategoriesDialogFragment.newInstance();
        categoryDialog.show(getSupportFragmentManager(), AppKey.CATEGORIES_DIALOG_FRAGMENT_TAG);
    }

    private void onClickButtonSelectWallet() {

        WalletsDialogFragment walletDialog = WalletsDialogFragment.newInstance();
        walletDialog.show(getSupportFragmentManager(), AppKey.WALLETS_DIALOG_FRAGMENT_TAG);
    }


    @Override
    public void setWalletSelected(Wallet wallet) {
        walletSelected = wallet;
        buttonSelectWallet.setText(walletSelected.getWalletName());
    }

    @Override
    public void setCategorySelected(Category category) {
        categorySelected = category;
        buttonSelectCategory.setText(categorySelected.getCategoryName());

    }

    @Override
    public void setEventSelected(Event event) {
        eventSelected = event;
        buttonSelectEvent.setText(eventSelected.getEventName());
    }

    @Override
    public void setPartners(List<Partner> partners) {
        partnersSelected = partners;
        String result = partners.toString().replace("[", "").replace("]", "");
        textSelectPartner.setText(result);
    }

    @Override
    public void setRepeatDay(int repeatDay) {
        //test, delete then
        buttonSelectRepeatDate.setText(String.valueOf(repeatDay));

    }


    private void showDayOfWeekSelectionDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn thứ");
        builder.setItems(dayOfWeeks, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //
                buttonSelectRepeatDate.setText(dayOfWeeks[i]);

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void setupTransactionAddingAlarm(int transactionId, int repeatTypeId, String repeatTime) {
        //init alarm and components
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, TransactionAlarmReceiver.class);
        intent.putExtra(AppKey.KEY_TRANSACTION_ID, transactionId);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), transactionId, intent, transactionId);

        //set time alarm trigger
        Calendar timeSet = Calendar.getInstance();
        switch (repeatTypeId) {

            case AppKey.MONTH_REPEAT_TYPE:
                timeSet.set(Calendar.DAY_OF_MONTH, Integer.parseInt(repeatTime));
                break;
            case AppKey.WEEK_REPEAT_TYPE:
                timeSet.set(Calendar.DAY_OF_WEEK, convertDayOfWeekToNumber(repeatTime));
                break;
            case AppKey.DAY_REPEAT_TYPE:
                String[] timeResult = repeatTime.split(":");
                timeSet.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeResult[0]));
                timeSet.set(Calendar.MINUTE, Integer.parseInt(timeResult[1]));
                break;
        }

        //start alarm
        alarmManager.setRepeating(AlarmManager.RTC, timeSet.getTimeInMillis(), 120000, alarmIntent);


    }

    private int convertDayOfWeekToNumber(String dayOfWeek) {

        if (dayOfWeek.equals(MONDAY.getName())) {
            return 2;
        }

        if (dayOfWeek.equals(TUESDAY.getName())) {
            return 3;
        }

        if (dayOfWeek.equals(WEDNESDAY.getName())) {
            return 4;
        }

        if (dayOfWeek.equals(THURSDAY.getName())) {
            return 5;
        }

        if (dayOfWeek.equals(FRIDAY.getName())) {
            return 6;
        }

        if (dayOfWeek.equals(SATURDAY.getName())) {
            return 7;
        }

        if (dayOfWeek.equals(SUNDAY.getName())) {
            return 8;
        }

        return -1;


    }


    private void setupAlarmTransactionEveryDay(int hourOfDay) {
        SharedPreferences sharedPreferences = SharePreferencesManager.getInstance(this);
        String alarmExits = sharedPreferences.getString(AppKey.KEY_PREF_ALARM_NOTIFICAITON_EVERYDAY, "");

       // showMessage(alarmExits);

        if (alarmExits.equals("")) {

            //
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(this, TransactionNotificationReceiver.class);

            PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(),
                    AppKey.ID_ALARM_NOTIFICATION_EVERYDAY, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar timeSet = Calendar.getInstance();
            timeSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            timeSet.set(Calendar.MINUTE, 0);
            timeSet.set(Calendar.MILLISECOND, 0);


            alarmManager.setRepeating(AlarmManager.RTC, timeSet.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
            sharedPreferences.edit().putString(AppKey.KEY_PREF_ALARM_NOTIFICAITON_EVERYDAY, "exists").apply();
        }


    }


    static class TransactionAddingTask extends AsyncTask<Transaction, Void, Long> {

        private WeakReference<TransactionAddingActivity> activityRef;

        public TransactionAddingTask(TransactionAddingActivity activity) {
            activityRef = new WeakReference<TransactionAddingActivity>(activity);

        }


        @Override
        protected Long doInBackground(Transaction... transactions) {

            TransactionAddingActivity activity = activityRef.get();
            AppDatabase appDatabase = activity.getAppDatabase();
            Long transactionId = 0L;
            List<Partner> partnersSelected = activity.partnersSelected;
            //

            if (appDatabase != null && transactions[0] != null) {

                transactionId = appDatabase.transactionDao().insertTransaction(transactions[0]);

                if (transactionId != 0) {

                    //Calculate Wallet Balance
                    if (transactions[0].getIdWallet() != 0) {
                        activity.walletSelected.setBalance(activity.walletSelected.getBalance() - transactions[0].getPrice());
                        appDatabase.walletDao().updateWallet(activity.walletSelected);

                    }

                    //Calculate Event Balance
                    if (transactions[0].getIdEvent() != 0) {
                        activity.eventSelected.setBalance(activity.eventSelected.getBalance() - transactions[0].getPrice());
                        appDatabase.eventDao().updateEvent(activity.eventSelected);
                    }


                    //Add Partner

                    if (partnersSelected != null) {

                        for (Partner partner : partnersSelected) {

                            long partnerId = appDatabase.partnerDao().isPartnerAlreadyExists(partner.getPartnerName());

                            if (partnerId == 0) {
                                partnerId = appDatabase.partnerDao().insertPartners(partner);
                            }


                            TransactionPartner transactionPartner = new TransactionPartner();
                            transactionPartner.setIdTransaction(Integer.parseInt(String.valueOf(transactionId)));
                            transactionPartner.setIdPartner(Integer.parseInt(String.valueOf(partnerId)));

                            //
                            appDatabase.transactionPartnerDao().insertTransactionPartner(transactionPartner);
                        }

                    }


                    //Add Repeat

                    RepeatType repeatType = (RepeatType) activity.spinnerRepeatMode.getSelectedItem();
                    String repeatCountMode = (String) activity.spinnerRepeatCount.getSelectedItem();
                    int repeatCount = Integer.parseInt(activity.editTextRepeatCount.getText().toString());
                    String repeatTime = activity.buttonSelectRepeatDate.getText().toString();

                    Repeat repeat = new Repeat();
                    repeat.setIdTransaction(Integer.parseInt(String.valueOf(transactionId)));
                    repeat.setIdRepeatType(repeatType.getIdRepeatType());
                    repeat.setRepeatName(repeatType.getRepeat_type_name());

                    if (repeatCountMode.equals(AppKey.INFINITE_REPEAT_COUNT)) {
                        repeatCount = AppKey.KEY_INFINITE_REPEAT_COUNT;
                    }

                    repeat.setNumber(repeatCount);
                    repeat.setTimeRepeat(repeatTime);

                    //
                    appDatabase.repeatDao().insertRepeatMode(repeat);


                }

            }

            return transactionId;
        }

        @Override
        protected void onPostExecute(Long aLong) {

            if (aLong != 0) {
                TransactionAddingActivity activity = activityRef.get();

                if (activity != null) {

                    activity.showMessage("Lưu dữ liệu thành công");
                    //

                    RepeatType repeatType = (RepeatType) activity.spinnerRepeatMode.getSelectedItem();

                    int repeatCount = Integer.parseInt(activity.editTextRepeatCount.getText().toString());
                    String repeatCountType = (String) activity.spinnerRepeatCount.getSelectedItem();


                    //start alarm
                    if (repeatCount != 0 || repeatCountType.equals(AppKey.INFINITE_REPEAT_COUNT)) {
                        activity.setupTransactionAddingAlarm(Integer.parseInt(String.valueOf(aLong)), repeatType.getIdRepeatType(),
                                activity.buttonSelectRepeatDate.getText().toString());
                    }

                    //finish activity then

                }

                activity.finish();


            }
        }


    }


    static class RepeatTypeAsyncTask extends AsyncTask<Void, Void, List<RepeatType>> {

        private WeakReference<TransactionAddingActivity> activityRef;

        public RepeatTypeAsyncTask(TransactionAddingActivity activity) {
            activityRef = new WeakReference<TransactionAddingActivity>(activity);
        }

        @Override
        protected List<RepeatType> doInBackground(Void... voids) {

            List<RepeatType> repeatTypes = null;
            TransactionAddingActivity activity = activityRef.get();

            repeatTypes = activity.getAppDatabase().repeatTypeDao().getRepeatTypes();

            return repeatTypes;
        }

        @Override
        protected void onPostExecute(List<RepeatType> repeatTypes) {

            if (repeatTypes == null) {
                repeatTypes = new ArrayList<>();

            } else {
                activityRef.get().setDataSpinnerRepeatType(repeatTypes);
            }
        }
    }
}
