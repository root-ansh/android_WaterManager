package in.curioustools.water_reminder.services;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Locale;

import in.curioustools.water_reminder.utils.NotifMaker;
import in.curioustools.water_reminder.R;
import in.curioustools.water_reminder.utils.TimeUtilities;
import in.curioustools.water_reminder.broadcast_recievers.NotificationActionReceiver;
import in.curioustools.water_reminder.db.pref.PrefUserDetails;
import in.curioustools.water_reminder.db.pref.PrefUserDetails.Defaults;
import in.curioustools.water_reminder.db.pref.PrefUserDetails.KEYS;

public class PeriodicNotificationService extends Worker {
    public PeriodicNotificationService(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {


        Context ctx = getApplicationContext();

        SharedPreferences pref = ctx.getSharedPreferences(PrefUserDetails.PREF_NAME, Context.MODE_PRIVATE);
        boolean showDailyNotifs = pref.getBoolean(KEYS.KEY_SHOW_NOTIFS, Defaults.SHOW_NOTIF);

        String sleepTime = pref.getString(KEYS.KEY_SLEEP_TIME, Defaults.SLEEP_TIME);
        String wakeupTime = pref.getString(KEYS.KEY_WAKEUP_TIME, Defaults.WAKEUP_TIME);
        String currentTime = TimeUtilities.getCurrentTime();
        boolean isBetweenSleep = TimeUtilities.isTimeInBetween2Times(sleepTime, wakeupTime, currentTime);

        if (showDailyNotifs && !isBetweenSleep) {
            createNotification(pref, ctx);

        }

        return Result.success();

    }

    private void createNotification(SharedPreferences pref, Context ctx) {
        String date = pref.getString(KEYS.KEY_DATE_TODAY, Defaults.DATE_TODAY);
        int achieved = pref.getInt(KEYS.KEY_TODAY_INTAKE_ACHIEVED, Defaults.TODAY_INTAKE_ACHIEVED);
        int target = pref.getInt(KEYS.KEY_DAILY_TARGET, Defaults.DAILY_TARGET);


        String title = "Hey! Drink some Water!";

        String details = String.format(Locale.ROOT, "%s Today's Progress:%d/%d ml. ", date, achieved, target);
        details += "\n Tap To Add 150 ml water to your daily Progress.";
        int resId = R.drawable.ic_notif_icon;

        //creating actions
        Intent i = new Intent(getApplicationContext(), NotificationActionReceiver.class);
        i.putExtra(NotificationActionReceiver.KEY_ACTION_I_DRANK_WATER, true);
        PendingIntent piDrankWater = PendingIntent.getBroadcast(
                ctx, NotificationActionReceiver.RECEIVER_RQ_CODE1, i, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action action1 = new NotificationCompat.Action(
                R.drawable.ic_logo, "Add 150 ml", piDrankWater);


        Intent i2 = new Intent(getApplicationContext(), NotificationActionReceiver.class);
        i2.putExtra(NotificationActionReceiver.KEY_ACTION_STOP_NOTIFICATIONS_FOR_TODAY, true);
        PendingIntent piStopTodayNotifs = PendingIntent.getBroadcast(
                ctx, NotificationActionReceiver.RECEIVER_RQ_CODE2, i2, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action action2 = new NotificationCompat.Action(
                R.drawable.ic_notifications_off, "Stop Notifications", piStopTodayNotifs);


        NotifMaker notifMaker = new NotifMaker(resId, ctx, title, details);
        notifMaker.setActionBtn1(action1);
        notifMaker.setActionBtn2(action2);
        notifMaker.show();
    }


}
