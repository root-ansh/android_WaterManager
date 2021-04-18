package in.curioustools.water_reminder.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static in.curioustools.water_reminder.db.pref.PrefUserDetails.Defaults;
import static in.curioustools.water_reminder.db.pref.PrefUserDetails.KEYS;
import static in.curioustools.water_reminder.db.pref.PrefUserDetails.PREF_NAME;

public class ServicesHandler {
    private static ServicesHandler INSTANCE = null;
    private static final String TAG = "ServicesHandler";
    private static final String PREF_KEY_DAILY_LOGS_S = "daily_log_service";
    private static final String PREF_KEY_PERIODIC_NOTIFIER_S = "periodic_notify_service";
    private static final String TAG_DAILY_LOG_REQ = "daily_log_service_req";
    private static final String TAG_PERIODIC_NOTIFY_REQ = "periodic_notify_service_req";

    private static final int NOTIF_MINUTES=15;
    private final WorkManager workManagerObj;
    private final SharedPreferences prefUUID;

    private ServicesHandler(Context ctx) {
        Context appCtx = ctx.getApplicationContext();
        this.workManagerObj = WorkManager.getInstance(appCtx);

        String prefName = "UUID_HANDLER";
        this.prefUUID = ctx.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }
    private static ServicesHandler getInstance(Context ctx) {
        if (INSTANCE == null) {
            INSTANCE = new ServicesHandler(ctx);
        }
        return INSTANCE;
    }


    public static void  updateServices(Context ctx){
        SharedPreferences prefMain=ctx.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean showNotifs=prefMain.getBoolean(KEYS.KEY_SHOW_NOTIFS,Defaults.SHOW_NOTIF);
        boolean showLogs=prefMain.getBoolean(KEYS.KEY_SHOW_LOGS,Defaults.SHOW_LOGS);

        if(showNotifs) {
            ServicesHandler.getInstance(ctx).startPeriodicNotifierServiceSinglton();
        }
        else {
            ServicesHandler.getInstance(ctx).stopPeriodicNotifierService();

        }

        if(showLogs) {
            ServicesHandler.getInstance(ctx).startDailyLogServiceSingleton();
        }
        else {
            ServicesHandler.getInstance(ctx).stopDailyLogService();

        }


    }

    private boolean isDailyLogServiceRunning() {
        Log.e(TAG, "isDailyLogServiceRunning: called.");

        boolean isRunning = false;

        UUID dailylogServiceID = getUUID(PREF_KEY_DAILY_LOGS_S);
        Log.e(TAG, "isDailyLogServiceRunning: getting service uuid from prefUUID=" + dailylogServiceID.toString());

        WorkInfo workInfo = getWorkInfoForService(dailylogServiceID);

        if (workInfo != null) {
            WorkInfo.State state = workInfo.getState();
            Log.e(TAG, "isDailyLogServiceRunning: workinfo state:" + state.name());

            if (state == WorkInfo.State.RUNNING || state == WorkInfo.State.ENQUEUED || state == WorkInfo.State.BLOCKED) {
                Log.e(TAG, "isDailyLogServiceRunning:state ==RUNNING || ENQUEUED || BLOCKED??,therefore isrunning=true");
                isRunning = true;
            }
        } else {
            Log.e(TAG, "isDailyLogServiceRunning: work info is null for this uuid");
        }


        return isRunning;
    }
    private boolean isPeriodicNotifyServiceRunning() {
        Log.e(TAG, "isPeriodicNotifyServiceRunning: called.");

        boolean isRunning = false;

        UUID periodicNotifyServiceID = getUUID(PREF_KEY_PERIODIC_NOTIFIER_S);
        Log.e(TAG, "isPeriodicNotifyServiceRunning: getting service uuid from prefUUID=" + periodicNotifyServiceID.toString());

        WorkInfo workInfo = getWorkInfoForService(periodicNotifyServiceID);

        if (workInfo != null) {
            WorkInfo.State state = workInfo.getState();
            Log.e(TAG, "isPeriodicNotifyServiceRunning: workinfo state:" + state.name());

            if (state == WorkInfo.State.RUNNING || state == WorkInfo.State.ENQUEUED || state == WorkInfo.State.BLOCKED) {
                Log.e(TAG, "isPeriodicNotifyServiceRunning:state ==RUNNING || ENQUEUED || BLOCKED??,therefore isrunning=true");
                isRunning = true;
            }
        } else {
            Log.e(TAG, "isPeriodicNotifyServiceRunning: work info is null");
        }

        return isRunning;
    }

    @Nullable
    private WorkInfo getWorkInfoForService(UUID uuid) {
        Log.e(TAG, "getWorkInfoForService: called for uuid:" + uuid.toString());
        WorkInfo workInfo = null;
        try {
            workInfo = workManagerObj.getWorkInfoById(uuid).get();
            Log.e(TAG, "getWorkInfoForService: workinfo for this id=" + workInfo);

        } catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "getWorkInfoForService: Exception occurred!");
            e.printStackTrace();
        }
        return workInfo;
    }
    private UUID getUUID(String key) {
        //will return uuid of either a previous service running, or a garbage uuid(if preferences doesn't have a value
        String garbageID = UUID.randomUUID().toString();
        Log.e(TAG, "getUUID: garbageUUID string=" + garbageID);

        UUID generatedID = UUID.fromString(prefUUID.getString(key, garbageID));
        Log.e(TAG, "getUUID: genertedUUID string=" + generatedID.toString());

        return generatedID;
    }

    private void startDailyLogServiceSingleton() {
        Log.e(TAG, "startDailyLogServiceSingleton: called" );
        if (isDailyLogServiceRunning()) {
            Log.e(TAG, "startSingleTonDailyLogService: service already running, therefore return");
            //return;
        } else {
            Log.e(TAG, "startSingleTonDailyLogService: building a new service and starting it");
            PeriodicWorkRequest dailyLogServiceRequest = buildDailyLogsServiceRequest();
            putUUID(PREF_KEY_DAILY_LOGS_S, dailyLogServiceRequest.getId());
            workManagerObj.enqueue(dailyLogServiceRequest);
        }

    }
    private PeriodicWorkRequest buildDailyLogsServiceRequest() {
        Constraints.Builder constraintsBuilder = new Constraints.Builder()
                .setRequiresCharging(false)
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresBatteryNotLow(false)
                .setRequiresStorageNotLow(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            constraintsBuilder.setRequiresDeviceIdle(false);
        }

        return new PeriodicWorkRequest
                .Builder(DailyLogsService.class, 15, TimeUnit.MINUTES)
                .setConstraints(constraintsBuilder.build())
                .addTag(TAG_DAILY_LOG_REQ)
                .build();
    }

    private void startPeriodicNotifierServiceSinglton() {
        Log.e(TAG, "startPeriodicNotifierServiceSinglton: called" );
        if (isPeriodicNotifyServiceRunning()) {
            Log.e(TAG, "startSingltonPeriodicNotifierService: service already running, therefore returning");
            //return;
        } else {
            Log.e(TAG, "startSingltonPeriodicNotifierService: building a new service and starting it");

            PeriodicWorkRequest periodicNotifyServiceRequest = buildPeriodicNotifierServiceRequest();
            putUUID(PREF_KEY_PERIODIC_NOTIFIER_S, periodicNotifyServiceRequest.getId());
            workManagerObj.enqueue(periodicNotifyServiceRequest);
        }

    }
    private PeriodicWorkRequest buildPeriodicNotifierServiceRequest() {
        Constraints.Builder constraintsBuilder = new Constraints.Builder()
                .setRequiresCharging(false)
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresBatteryNotLow(false)
                .setRequiresStorageNotLow(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            constraintsBuilder.setRequiresDeviceIdle(false);
        }

        return new PeriodicWorkRequest
                .Builder(PeriodicNotificationService.class, NOTIF_MINUTES, TimeUnit.MINUTES)
                .setConstraints(constraintsBuilder.build())
                .addTag(TAG_PERIODIC_NOTIFY_REQ)
                .build();
    }

    private void putUUID(String key, UUID uuidString) {
        //will put uuid in prefs
        Log.e(TAG, "putUUID: called for key" + key + "with uuid string" + uuidString);
        prefUUID.edit().putString(key, uuidString.toString()).apply();
    }

    private void  stopDailyLogService(){
        Log.e(TAG, "stopDailyLogService: called" );
        workManagerObj.cancelAllWorkByTag(TAG_DAILY_LOG_REQ);
        putUUID(PREF_KEY_DAILY_LOGS_S, UUID.randomUUID());
    }
    private void  stopPeriodicNotifierService(){
        Log.e(TAG, "stopPeriodicNotifierService: called" );
        workManagerObj.cancelAllWorkByTag(TAG_PERIODIC_NOTIFY_REQ);
        putUUID(PREF_KEY_PERIODIC_NOTIFIER_S, UUID.randomUUID());
    }

}
