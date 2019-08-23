package in.curioustools.water_reminder.UiUtilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;


import in.curioustools.water_reminder.R;

import static android.content.Context.NOTIFICATION_SERVICE;


@SuppressWarnings("FieldCanBeLocal")
public class NotifMaker {
    private static final String TAG = "NJS>>";
    private static final int NOTIFICATION_ID = 220;
    private static final String CHANNEL_ID = "notify-tea";
    private int CHANNEL_IMPORTANCE;

    private int NOTIF_PRIORITY =NotificationCompat.PRIORITY_HIGH;
    private static final String CHANNEL_NAME = "CHANNEL_NAME";
    private static final String CHANNEL_DESC = "CHANNEL_DESC";

    private int smallIconID;
    private Context appCtx;
    private String notificationTitle, notificationDescription;

    @Nullable
    private PendingIntent pendingIntent;
    @Nullable private NotificationCompat.Action actionbtn1;
    @Nullable private NotificationCompat.Action actionbtn2;
    @Nullable private NotificationCompat.Action actionbtn3;

    public NotifMaker(int smallIconID, @NonNull Context ctx, @NonNull String notificationTitle, @NonNull String notificationDescription) {
        this.smallIconID = smallIconID;
        this.appCtx = ctx;
        this.notificationTitle = notificationTitle;
        this.notificationDescription = notificationDescription;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_MAX;
        }

    }



    @SuppressWarnings("unused")
    public void setPendingIntent(@Nullable PendingIntent pendingIntent) {
        this.pendingIntent = pendingIntent;
    }
    public void setActionBtn1(@Nullable NotificationCompat.Action actionBtn1) {
        this.actionbtn1 = actionBtn1;
    }
    public void setActionBtn2(@Nullable NotificationCompat.Action actionBtn2) {
        this.actionbtn2 = actionBtn2;
    }


    @SuppressWarnings("unused")
    public void setActionBtn3(@Nullable NotificationCompat.Action actionBtn3) {
        this.actionbtn3 = actionBtn3;
    }

    public void show() {
        NotificationManager notifManager = (NotificationManager) appCtx.getSystemService((NOTIFICATION_SERVICE));
        if (notifManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notifManager.createNotificationChannel(getChannel(notifManager));
            }
            Notification notif = getNotificationObj();
            notifManager.notify(NOTIFICATION_ID, notif);
        }
    }
    public static void cancelAll(Context appCtx){
        appCtx=appCtx.getApplicationContext();
        NotificationManager notifManager =
                (NotificationManager) appCtx.getSystemService((NOTIFICATION_SERVICE));
        if (notifManager != null) {
            notifManager.cancelAll();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private NotificationChannel getChannel(NotificationManager notifManager) {
        NotificationChannel mChannel;
        mChannel = notifManager.getNotificationChannel(CHANNEL_ID);
        if (mChannel != null) {
            return mChannel;
        } else {
            mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, CHANNEL_IMPORTANCE);
        }
        mChannel.setDescription(CHANNEL_DESC);
        return mChannel;
    }
    private Notification getNotificationObj() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(appCtx, CHANNEL_ID);
        Bitmap icBottle = BitmapFactory.decodeResource(appCtx.getResources(),R.drawable.ic_container_bottle_colored);

        builder.setContentTitle(notificationTitle)
                .setContentText(notificationDescription)
                .setColor(appCtx.getResources().getColor(R.color.colorAccent))
                .setColorized(true)
                .setLargeIcon(icBottle)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationDescription))
                .setAutoCancel(true)
                .setTicker(notificationTitle)
                .setPriority(NOTIF_PRIORITY)
                .setSmallIcon(smallIconID);

        if (pendingIntent != null) {
            builder.setContentIntent(pendingIntent);
        }
        if(actionbtn1!=null){
            Log.e(TAG, "getNotificationObj: action button 1 is not null" );
            builder.addAction(actionbtn1);
        }
        if(actionbtn2!=null){
            Log.e(TAG, "getNotificationObj: action button 2 is not null" );

            builder.addAction(actionbtn2);
        }
        if(actionbtn3!=null){
            builder.addAction(actionbtn3);
        }


        return builder.build();


    }



}
