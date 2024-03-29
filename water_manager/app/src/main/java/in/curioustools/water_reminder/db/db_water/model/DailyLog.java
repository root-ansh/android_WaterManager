package in.curioustools.water_reminder.db.db_water.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Locale;

import in.curioustools.water_reminder.utils.TimeUtilities;
import in.curioustools.water_reminder.db.db_water.WaterDbUtils;

@Entity(tableName = WaterDbUtils.TDL.Names.TABLE_NAME)
public class DailyLog {
    @ColumnInfo(name = WaterDbUtils.TDL.Names.COL_ID)
    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    @ColumnInfo(name = WaterDbUtils.TDL.Names.COL_DATE) //pattern must be EEEE, MMM dd, yyyy
    private String date;

    @ColumnInfo(name = WaterDbUtils.TDL.Names.COL_TARGET)
    private int targetInMilliLitres;

    @ColumnInfo(name = WaterDbUtils.TDL.Names.COL_ACHIEVED)
    private int achievedInMilliLitres;

    public DailyLog() {
        this.date = TimeUtilities.getCurrentDate();
    }

    @Ignore
    public DailyLog(long id, @NonNull String date, int target, int achievedInMilliLitres) {
        this.id = id;
        this.date = date;
        this.targetInMilliLitres = target;
        this.achievedInMilliLitres = achievedInMilliLitres;
    }

    public long getId() {
        return id;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public int getTargetInMilliLitres() {
        return targetInMilliLitres;
    }

    public int getAchievedInMilliLitres() {
        return achievedInMilliLitres;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    public void setTargetInMilliLitres(int targetInMilliLitres) {
        this.targetInMilliLitres = targetInMilliLitres;
    }

    public void setAchievedInMilliLitres(int achievedInMilliLitres) {
        this.achievedInMilliLitres = achievedInMilliLitres;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.ROOT,
                "DailyLog{id=%d, date='%s', target=%d, achieved=%d}", id, date, targetInMilliLitres, achievedInMilliLitres);
    }
}
