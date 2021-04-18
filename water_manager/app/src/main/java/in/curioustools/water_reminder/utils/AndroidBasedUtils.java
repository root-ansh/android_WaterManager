package in.curioustools.water_reminder.utils;

import android.animation.ValueAnimator;
import android.content.SharedPreferences;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import in.curioustools.water_reminder.db.db_water.WaterRepo;
import in.curioustools.water_reminder.db.db_water.model.DailyLog;
import in.curioustools.water_reminder.db.pref.PrefUserDetails.Defaults;
import in.curioustools.water_reminder.db.pref.PrefUserDetails.KEYS;

public class AndroidBasedUtils {

    /**
     * A function to check weather the current date(i.e the date at which this method is called)
     * is equal to 'savedDate' attribute inside prefUserDetails. if True, it won't do anything.
     * If false, it will:  </br>
     *     1. add old date's log to logs table   </br>
     *     2. reset date and achieved qty in preferences   </br>
     *     3. clear allitems in today entries table table   </br>
     * */
    public static void makeDateChanges(@Nullable SharedPreferences prefBasicInfo,@Nullable WaterRepo repo) {
        if(prefBasicInfo == null || repo == null) return;
        boolean showLogs = prefBasicInfo.getBoolean(KEYS.KEY_SHOW_LOGS, Defaults.SHOW_LOGS);
        if (showLogs) {
            String dateFromPref = prefBasicInfo.getString(KEYS.KEY_DATE_TODAY, Defaults.DATE_TODAY);
            String today = TimeUtilities.getCurrentDate();
            if (!dateFromPref.equals(today)) {
                int achievedQty = prefBasicInfo.getInt(KEYS.KEY_TODAY_INTAKE_ACHIEVED, Defaults.TODAY_INTAKE_ACHIEVED);
                int targetQty = prefBasicInfo.getInt(KEYS.KEY_DAILY_TARGET, Defaults.DAILY_TARGET);

                DailyLog log = new DailyLog();
                log.setDate(dateFromPref);
                log.setTargetInMilliLitres(targetQty);
                log.setAchievedInMilliLitres(achievedQty);

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
    
    public static void setTextAnimated(int newValue, final TextView tv) {
        if(tv == null) return;

        int oldValue ;
        try {
            oldValue = Integer.parseInt(tv.getText().toString());
        }
        catch (Exception e){
            oldValue = 0;
        }
        int duration = oldValue > newValue ? 500 : 1500;
        ValueAnimator animator = ValueAnimator.ofInt(oldValue, newValue);
        animator.setDuration(duration);
        animator.addUpdateListener(
                valueAnimator -> {
                    String s = String.format("%s", valueAnimator.getAnimatedValue().toString());
                    tv.setText(s);
                }
        );
        animator.start();

    }


}
