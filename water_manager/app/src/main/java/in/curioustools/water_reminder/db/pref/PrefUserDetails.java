package in.curioustools.water_reminder.db.pref;


import in.curioustools.water_reminder.utils.TimeUtilities;

public class PrefUserDetails {
    public static final String PREF_NAME = "basicinfo";
    public interface KEYS {
        String KEY_DATE_TODAY = "basicinfo_date_today";
        String KEY_WAKEUP_TIME = "basicinfo_time_wakeup";
        String KEY_SLEEP_TIME = "basicinfo_time_sleep";
        String KEY_GENDER = "basicinfo_gender";
        String KEY_WEIGHT = "basicinfo_weight";
        String KEY_ACTIVITY = "basicinfo_activity";
        String KEY_SHOW_NOTIFS = "show_notif";
        String KEY_SHOW_IMPERIAL_MM = "show_imperial_metrics";
        String KEY_SHOWN_INFO_ACTIVITY = "launched_info_activity_once";

        String KEY_DAILY_TARGET = "basicinfo_daily_intake";
        String KEY_TODAY_INTAKE_ACHIEVED = "basicinfo_today_intake";

        String KEY_SHOW_LOGS ="basicinfo_start_daily_logger_service" ;
    }
    public interface Defaults {
        int WEIGHT = 40;
        int DAILY_TARGET = 2000;
        String GENDER = UserGender.MALE.name();
        String WAKEUP_TIME = "6:00 AM";
        String SLEEP_TIME = "11:30 PM";
        String USER_ACTIVITY = UserActivity.LOTS.toString();

        int TODAY_INTAKE_ACHIEVED = 0;
        String DATE_TODAY = TimeUtilities.getCurrentDate();

        boolean SHOW_NOTIF = false;
        boolean HAS_SHOWN_INTRO_INFO_ACTIVITY = false;
        boolean SHOW_LOGS =false;
        Boolean SHOW_IMPERIAL_MM = false;
    }

    public enum UserActivity {LOTS, SOME, AFEW, NONE}
    public enum UserGender {MALE, FEMALE}
//    private SharedPreferences prefObj;

//    public class UserDetailsLive{
//        String sleepTime,wakeUpTime,DateToday;
//        int weight,dailyTarget,achieved;
//        boolean hasShowIntroActivity, showFullDayNotifications;
//        UserGender gender;UserActivity activity;
//    }
//
//
//    public PrefUserDetails(Context ctx) {
//        this.prefObj = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
//    }
//
//    public UserGender getGender() {
//        String gen = this.prefObj.getString(KEYS.KEY_GENDER, Defaults.GENDER);
//        return UserGender.valueOf(gen);
//    }
//    public int getWeight() {
//        return this.prefObj.getInt(KEYS.KEY_WEIGHT, Defaults.WEIGHT);
//    }
//    public String getWakeUpTime() {
//        return this.prefObj.getString(KEYS.KEY_WAKEUP_TIME, Defaults.WAKEUP_TIME);
//    }
//    public String getSleepTime() {
//        return this.prefObj.getString(KEYS.KEY_SLEEP_TIME, Defaults.SLEEP_TIME);
//    }
//    public UserActivity getUserActivity() {
//        String userActivityStr = this.prefObj.getString(KEYS.KEY_ACTIVITY, Defaults.USER_ACTIVITY);
//        return UserActivity.valueOf(userActivityStr);
//    }
//    public int getDailyTargetIntake() {
//        return this.prefObj.getInt(KEYS.KEY_DAILY_TARGET, Defaults.DAILY_TARGET);
//    }
//    public int getCurrentlyAchievedIntake() {
//        return this.prefObj.getInt(KEYS.KEY_TODAY_INTAKE_ACHIEVED, Defaults.TODAY_INTAKE_ACHIEVED);
//    }
//    public String getSavedDate() {
//        return this.prefObj.getString(KEYS.KEY_DATE_TODAY, Defaults.DATE_TODAY);
//    }
//    public boolean getReminderNotifications() {
//        return this.prefObj.getBoolean(KEYS.KEY_SHOW_NOTIFS, Defaults.SHOW_NOTIF);
//    }
//    public boolean getHasShownIntroInfoActivity() {
//        return this.prefObj.getBoolean(KEYS.KEY_SHOWN_INFO_ACTIVITY, Defaults.HAS_SHOWN_INTRO_INFO_ACTIVITY);
//    }
//    public SharedPreferences getInternalPrefObj(){
//        return this.prefObj;
//    }
//
//
//    public void setGender(@NonNull UserGender gender) {
//        this.prefObj.edit().putString(KEYS.KEY_GENDER, gender.name()).apply();
//    }
//    public void setWeight(int weight) {
//        this.prefObj.edit().putInt(KEYS.KEY_WEIGHT, weight).apply();
//    }
//    public void setWakeUpTime(String wakeUpTime) {
//        this.prefObj.edit().putString(KEYS.KEY_WAKEUP_TIME, wakeUpTime).apply();
//    }
//    public void setSleepTime(String sleepTime) {
//        this.prefObj.edit().putString(KEYS.KEY_SLEEP_TIME, sleepTime).apply();
//    }
//    public void setUserActivity(UserActivity userActivity) {
//        this.prefObj.edit().putString(KEYS.KEY_ACTIVITY, userActivity.name()).apply();
//    }
//    public void setDailyTargetIntake(int target) {
//        this.prefObj.edit().putInt(KEYS.KEY_DAILY_TARGET, target).apply();
//    }
//    public void setCurrentlyAchievedIntake(int achieved) {
//        this.prefObj.edit().putInt(KEYS.KEY_TODAY_INTAKE_ACHIEVED, achieved).apply();
//    }
//    public void setSavedDate(String date) {
//        this.prefObj.edit().putString(KEYS.KEY_DATE_TODAY, date).apply();
//    }
//    public void setReminderNotifications(boolean reminderNotifications) {
//        this.prefObj.edit().putBoolean(KEYS.KEY_SHOW_NOTIFS, reminderNotifications).apply();
//    }
//    public void setHasShownIntroInfoActivity(boolean hasShowUserActivity) {
//        this.prefObj.edit().putBoolean(KEYS.KEY_SHOWN_INFO_ACTIVITY, hasShowUserActivity).apply();
//    }
//
//
//    public void registerPrefListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
//        this.prefObj.registerOnSharedPreferenceChangeListener(listener);
//    }
//    public void unRegisterPrefListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
//        this.prefObj.unregisterOnSharedPreferenceChangeListener(listener);
//    }

}
