package in.curioustools.water_reminder.broadcast_recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import in.curioustools.water_reminder.utils.NotifMaker;
import in.curioustools.water_reminder.utils.AndroidBasedUtils;
import in.curioustools.water_reminder.db.db_water.WaterRepo;
import in.curioustools.water_reminder.db.pref.PrefUserDetails.Defaults;
import in.curioustools.water_reminder.db.pref.PrefUserDetails.KEYS;
import in.curioustools.water_reminder.services.ServicesHandler;

import static in.curioustools.water_reminder.db.pref.PrefUserDetails.PREF_NAME;

public class NotificationActionReceiver extends BroadcastReceiver {
    public static final int RECEIVER_RQ_CODE1 = 111;
    public static final int RECEIVER_RQ_CODE2 = 112;

    public static final String KEY_ACTION_I_DRANK_WATER = "action_i_drank_water";
    public static final String KEY_ACTION_STOP_NOTIFICATIONS_FOR_TODAY = "action_stop_notifs";


    private static final String TAG = "RECIEVER>>>";

    /*for reciever to recieve some intent these lines are must:
     *  intent i = new intent(context of sender, Reciever,class
     *  i.putExtra(unique key,unique value)
     *  <for pending intent : pi.getBroadcast(ctx,UIQUE_REQ_CODE,intent,integr_flag)>
     *  <for intent: ctx.sendBroadcast(intent) >
     */

    @Override
    public void onReceive(Context ctx, Intent data) {
        Log.e(TAG, "onReceive: called");

        if (data.getBooleanExtra(KEY_ACTION_I_DRANK_WATER, false)) {
            Log.e(TAG, "KEY_ACTION_I_DRANK_WATER: called with true");
            doActionsForUserDrankWater(ctx);
        }
        if (data.getBooleanExtra(KEY_ACTION_STOP_NOTIFICATIONS_FOR_TODAY, false)) {
            Log.e(TAG, "KEY_ACTION_STOP_NOTIFICATIONS_FOR_TODAY: called with true");
            stopNotificationsForToday(ctx);
        }

    }


    private void stopNotificationsForToday(Context ctx) {
        Toast.makeText(ctx, "Check Settings To restart DailyNotifications", Toast.LENGTH_LONG).show();
        SharedPreferences prefMain = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefMain.edit().putBoolean(KEYS.KEY_SHOW_NOTIFS, false).apply();
        ServicesHandler.updateServices(ctx);


        NotifMaker.cancelAll(ctx.getApplicationContext());
    }

    private void doActionsForUserDrankWater(Context ctx) {
        SharedPreferences prefMain = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        WaterRepo repo = WaterRepo.getRepoInstance(ctx.getApplicationContext());

        AndroidBasedUtils.makeDateChanges(prefMain, repo);

        int achieved = prefMain.getInt(KEYS.KEY_TODAY_INTAKE_ACHIEVED, Defaults.TODAY_INTAKE_ACHIEVED);
        prefMain.edit().putInt(KEYS.KEY_TODAY_INTAKE_ACHIEVED, achieved + 150).apply();

        NotifMaker.cancelAll(ctx.getApplicationContext());

    }

}
