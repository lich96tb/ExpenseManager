package ict.com.expensemanager.util;

/**
 * Created by PHAMHOAN on 1/18/2018.
 */

public final class AppKey {
    public static final String KEY_BUNDLE = "key_bundle";
    public static final String NAME_DATABASE = "ExpenseManager";
    public static final String ACTION_UPDATE = "action_update";
    public static final String EVENT = "event";
    public static final String ID_EVENT = "id_event";
    public static final String ICON = "Icon";
    public static final int DEFAULT_VALUE = 0;
    public static final int FRAGMENT_CODE = 1;

    private AppKey() {
    }

    public static final String DATABASE_NAME = "ExpenseManager";

    public static final String USER_NAME = "name";
    public static final String USER_PASS = "pass";

    public static final String LIST = "listTransaction";


    //test
    public static final String KEY_CALL_REPORT = "key_report";
    public static final int KEY_EVENT_REPORT = 3;
    public static final int KEY_WALLET_REPORT = 2;
    public static final int KEY_CATEGORY_REPORT = 1;
    public static final String KEY_CALL_DATA_REPORT = "key_data_report";
    public static final String KEY_USER_ID = "userId";
    public static final int KEY_REPEAT_COUNT = 248;
    public static final int KEY_OLD_TRANSACTION = 250;
    public static final String PREFERENCES_NAME = "mypreferences";
    public static final int ID_ALARM_NOTIFICATION_EVERYDAY = 900;
    public static final String KEY_PREF_ALARM_NOTIFICAITON_EVERYDAY = "alarmeveryday";

    public static final String WALLETS_DIALOG_FRAGMENT_TAG = "walletsdialog";
    public static final String CATEGORIES_DIALOG_FRAGMENT_TAG = "categoriesdialog";
    public static final String EVENTS_DIALOG_FRAGMENT_TAG = "eventsdialog";
    public static final String PARTNERS_DIALOG_FRAGMENT_TAG = "partnersdialog";
    public static final String DAYS_PICKER_DIALOG_FRAGMENT_TAG = "dayspickerdialog";
    public static final String PREF_ALARM_COUNT_NAME = "prefalarmcountname";
    public static final String KEY_PREF_ALARM_COUNT = "alarmcount";

    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 199;

    public static final String KEY_LIST_PARTNER_SELECTED = "partners_selected";
    public static final String KEY_TRANSACTION_ID = "transaction_id";
    public static final int MONTH_REPEAT_TYPE = 1;
    public static final int WEEK_REPEAT_TYPE = 2;
    public static final int DAY_REPEAT_TYPE = 3;

    public static final String INFINITE_REPEAT_COUNT = "Mãi mãi";
    public static final String CUSTOM_REPEAT_COUNT = "Tự chọn";
    public static final int KEY_INFINITE_REPEAT_COUNT = -99;

    public static final int ID_NOTIFICATION_TRANSACTION_ADDED = 999;


    public static final int VIEW_EMPTY = 0;
    public static final int VIEW_NORMAL = 1;

    public enum DAY_OF_WEEK {

        MONDAY("Thứ hai"),
        TUESDAY("Thứ ba"),
        WEDNESDAY("Thứ tư"),
        THURSDAY("Thứ năm"),
        FRIDAY("Thứ sáu"),
        SATURDAY("Thứ bảy"),
        SUNDAY("Chủ nhật");

        private String name;

        DAY_OF_WEEK(String dayName) {
            this.name = dayName;
        }

        public String getName() {
            return name;
        }
    }
}
