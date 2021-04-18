package in.curioustools.water_reminder.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import in.curioustools.water_reminder.utils.UtilMethods;
import in.curioustools.water_reminder.db.db_water.WaterRepo;
import in.curioustools.water_reminder.db.pref.PrefUserDetails;
import in.curioustools.water_reminder.db.pref.PrefUserDetails.Defaults;
import in.curioustools.water_reminder.db.pref.PrefUserDetails.KEYS;

public class DailyLogsService extends Worker {

    private static final String TAG = "DailyLogsService>>";

    public DailyLogsService(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.e(TAG, "doWork: called");

        Context ctx = getApplicationContext();
        SharedPreferences prefs = ctx.getSharedPreferences(PrefUserDetails.PREF_NAME, Context.MODE_PRIVATE);
        WaterRepo repo = WaterRepo.getRepoInstance(ctx);

        Log.e(TAG, "doWork: created repo and pref instance from application context");

        boolean showLogs = prefs.getBoolean(KEYS.KEY_SHOW_LOGS, Defaults.SHOW_LOGS);

        Log.e(TAG, "doWork: prefs.getBoolean(KEYS.KEY_SHOW_LOGS) = " + showLogs);
        if (showLogs) {
            Log.e(TAG, "doWork: calling UtilMethods.makeDateChanges(prefs,repo); coz showLogs is true");
            UtilMethods.makeDateChanges(prefs, repo);
        } else {
            Log.e(TAG, "doWork: doing nothing( coz showLogs is false)");
        }

        Log.e(TAG, "doWork:  returning Results.SUCCESS");
        return Result.success();
    }
}
