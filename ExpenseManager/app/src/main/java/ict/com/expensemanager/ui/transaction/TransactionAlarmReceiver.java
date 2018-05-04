package ict.com.expensemanager.ui.transaction;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import ict.com.expensemanager.R;
import ict.com.expensemanager.util.AppKey;

/**
 * Created by nguyenanhtrung on 25/01/2018.
 */

public class TransactionAlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        int transactionId = intent.getIntExtra(AppKey.KEY_TRANSACTION_ID, 0);
        Toast.makeText(context,"transactionId = " + transactionId,Toast.LENGTH_LONG).show();
        if (transactionId != 0) {
            Intent serviceIntent = new Intent(context.getApplicationContext(), TransactionAddingService.class);
            serviceIntent.putExtra(AppKey.KEY_TRANSACTION_ID,transactionId);
            context.getApplicationContext().startService(serviceIntent);
        }



    }
}
