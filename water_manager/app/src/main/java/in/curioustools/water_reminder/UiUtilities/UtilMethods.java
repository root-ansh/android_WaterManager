package in.curioustools.water_reminder.UiUtilities;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import in.curioustools.water_reminder.db.db_water.WaterRepo;
import in.curioustools.water_reminder.db.db_water.model.DailyLog;
import in.curioustools.water_reminder.db.pref.PrefUserDetails.Defaults;
import in.curioustools.water_reminder.db.pref.PrefUserDetails.KEYS;

public class UtilMethods {

    public static void makeDateChanges(
            @NonNull SharedPreferences prefBasicInfo, @NonNull WaterRepo repo) {
        /* A function to check weather the current date(i.e the date at which this method is called)
         * is equal to 'savedDate' attribute inside prefUserDetails. if True, it won't do anything.
         * If false, it will:
         *     1. add old date's log to logs table
         *     2. reset date and achieved qty in preferences
         *     3. clear allitems in today entries table table
         *
         * */
        boolean showLogs = prefBasicInfo.getBoolean(KEYS.KEY_SHOW_LOGS, Defaults.SHOW_LOGS);
        if (showLogs) {
            String dateFromPref = prefBasicInfo.getString(KEYS.KEY_DATE_TODAY, Defaults.DATE_TODAY);
            String today = TimeUtilities.getCurrentDate();
            if (!dateFromPref.equals(today)) {
                int achievedQty = prefBasicInfo.getInt(KEYS.KEY_TODAY_INTAKE_ACHIEVED, Defaults.TODAY_INTAKE_ACHIEVED);
                int targetQty = prefBasicInfo.getInt(KEYS.KEY_DAILY_TARGET, Defaults.DAILY_TARGET);

                DailyLog log = new DailyLog();
                log.setDate(dateFromPref);
                log.setTarget(targetQty);
                log.setAchieved(achievedQty);

                //1
                if (!repo.checkIfLogExists(log)) {
                    repo.addNewDailyLog(log);
                }

                //2.resetted date and intake to today,0
                prefBasicInfo.edit()
                        .putString(
                                KEYS.KEY_DATE_TODAY, Defaults.DATE_TODAY)
                        .putInt(
                                KEYS.KEY_TODAY_INTAKE_ACHIEVED,
                                Defaults.TODAY_INTAKE_ACHIEVED
                        )
                        .apply();

                //3
                repo.removeAllTodayEntries();
            }
        }


    }


}
