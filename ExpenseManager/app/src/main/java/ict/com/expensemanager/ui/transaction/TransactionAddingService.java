package ict.com.expensemanager.ui.transaction;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.SparseIntArray;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import ict.com.expensemanager.data.database.AppDatabase;
import ict.com.expensemanager.data.database.entity.Repeat;
import ict.com.expensemanager.data.database.entity.Transaction;
import ict.com.expensemanager.data.preferences.SharePreferencesManager;
import ict.com.expensemanager.util.AppKey;
import ict.com.expensemanager.util.Commons;

/**
 * Created by nguyenanhtrung on 25/01/2018.
 */

public class TransactionAddingService extends Service {

    private AppDatabase appDatabase;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        appDatabase = AppDatabase.getInstance(getApplicationContext());
        sharedPreferences = SharePreferencesManager.getInstance(getApplicationContext());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        handleIntent(intent);


        return super.onStartCommand(intent, flags, startId);
    }

    private void handleIntent(Intent intent) {

        if (intent != null) {
            int transactionId = intent.getIntExtra(AppKey.KEY_TRANSACTION_ID, 0);


            if (transactionId != 0) {

                AutoAddTransactionTask autoAddTransactionTask = new AutoAddTransactionTask(this);
                autoAddTransactionTask.execute(transactionId);


            }
        }


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void pushNotificationTransactionAdded(String title, String contentText) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                        .setContentTitle(title)
                        .setContentText(contentText);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(AppKey.ID_NOTIFICATION_TRANSACTION_ADDED, mBuilder.build());
    }

    private void stopAlarmManagerRepeating(int alarmId) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, TransactionAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmId, intent,alarmId);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    private long getCurrentDate() {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        return Commons.convertStringDateToLong(simpleDateFormat.format(calendar.getTime()));
    }

    static class AutoAddTransactionTask extends AsyncTask<Integer, Void, SparseIntArray> {

        private WeakReference<TransactionAddingService> serviceRef;

        public AutoAddTransactionTask(TransactionAddingService transactionAddingService) {

            serviceRef = new WeakReference<TransactionAddingService>(transactionAddingService);

        }

        @Override
        protected SparseIntArray doInBackground(Integer... integers) {
            int transactionId = integers[0];
            TransactionAddingService service = serviceRef.get();
            AppDatabase appDatabase = service.appDatabase;
            Transaction transaction = appDatabase.transactionDao().getTransactionById(transactionId);

            if (transaction != null) {

                //calculate balance event and wallet
                double walletBalance = appDatabase.walletDao().getWalletBalanceById(transaction.getIdWallet());
                double eventBalance = appDatabase.eventDao().getEventBalanceById(transaction.getIdEvent());
                walletBalance -= transaction.getPrice();
                eventBalance -= transaction.getPrice();

                //update current time add transaction
                long currentDate = service.getCurrentDate();
                transaction.setTime(currentDate);
                transaction.setIdTransaction(0);

                //insert new transaction
                long idNewTransaction = appDatabase.transactionDao().insertTransaction(transaction);

                if (idNewTransaction != 0) {
                    appDatabase.walletDao().updateWalletBalance(walletBalance);
                    appDatabase.eventDao().updateEventBalance(eventBalance);

                    Repeat repeat = appDatabase.repeatDao().getRepeatByTransactionId(transactionId);
                    SparseIntArray myKeys = new SparseIntArray();

                    if (repeat.getNumber() != AppKey.KEY_INFINITE_REPEAT_COUNT) {
                        myKeys.append(AppKey.KEY_REPEAT_COUNT, repeat.getNumber());
                    } else {
                        myKeys.append(AppKey.KEY_REPEAT_COUNT, AppKey.KEY_INFINITE_REPEAT_COUNT);
                    }

                    myKeys.append(AppKey.KEY_OLD_TRANSACTION, Integer.parseInt(String.valueOf(transactionId)));


                    return myKeys;
                }


            }



            return null;
        }

        @Override
        protected void onPostExecute(SparseIntArray map) {
            int repeatCount = map.get(AppKey.KEY_REPEAT_COUNT);
            int transactionOldId = map.get(AppKey.KEY_OLD_TRANSACTION);
            int alarmCount = 0;
            TransactionAddingService service = serviceRef.get();
            SharedPreferences sharedPreferences = service.sharedPreferences;

            //
            if (repeatCount != AppKey.KEY_INFINITE_REPEAT_COUNT) {

                alarmCount = sharedPreferences.getInt(AppKey.KEY_PREF_ALARM_COUNT + transactionOldId, 0);
                alarmCount++;

                if (alarmCount == repeatCount) {

                    service.stopAlarmManagerRepeating(transactionOldId);
                    //stop alarm
                }

                alarmCount++;
                sharedPreferences.edit().putInt(AppKey.KEY_PREF_ALARM_COUNT + transactionOldId, alarmCount).apply();

            }


            //push notification transaction added!
            service.pushNotificationTransactionAdded("Thêm giao dịch", "Đã thêm giao dịch mới");

        }
    }
}
