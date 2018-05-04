package ict.com.expensemanager.ui.transaction;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import ict.com.expensemanager.util.AppKey;

/**
 * Created by nguyenanhtrung on 26/01/2018.
 */

public class TransactionNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent activityIntent = new Intent(context,TransactionAddingActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,AppKey.ID_ALARM_NOTIFICATION_EVERYDAY,activityIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new  NotificationCompat.Builder(context)
                        .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                        .setContentTitle("Thêm giao dịch")
                        .setContentIntent(pendingIntent)
                        .setContentText("Hãy thêm giao dịch trong ngày!");

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(AppKey.ID_ALARM_NOTIFICATION_EVERYDAY, mBuilder.build());

    }
}
