package in.curioustools.water_reminder.db.db_water;

public final class WaterDbUtils {

    static final String DB_NAME="wave_database";

    public static class TDL{ //TDL= Table DailyLog
        public interface Names{
            String TABLE_NAME = "table_daily_logs";
            String COL_ID = "ID_LOGS";
            String COL_DATE = "DATE";
            String COL_TARGET = "TARGET";
            String COL_ACHIEVED= "ACHIEVED";
        }
    }
    public static class TT{ //TT= Table TodayEntry
        public interface Names{
            String TABLE_NAME = "table_today_entries";
            String COL_ID = "ID_TODAY";
            String COL_TIME = "TIME";
            String COL_AMOUNT = "DRINK_AMOUNT";
        }
    }


}




