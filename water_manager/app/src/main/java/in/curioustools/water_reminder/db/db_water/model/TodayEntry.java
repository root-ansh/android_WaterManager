package in.curioustools.water_reminder.db.db_water.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Locale;

import in.curioustools.water_reminder.UiUtilities.TimeUtilities;
import in.curioustools.water_reminder.db.db_water.WaterDbUtils;


@Entity(tableName = WaterDbUtils.TT.Names.TABLE_NAME)
public class TodayEntry {
    @ColumnInfo(name = WaterDbUtils.TT.Names.COL_ID)
    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    @ColumnInfo(name = WaterDbUtils.TT.Names.COL_TIME)  //pattern must be hh:mm a
    private String time;

    @ColumnInfo(name = WaterDbUtils.TT.Names.COL_AMOUNT)
    private int amount;

    public TodayEntry() {
        this.time= TimeUtilities.getCurrentTime();
    }



    public long getId() {
        return id;
    }

    @NonNull
    public String getTime() {
        return time;
    }

    public int getAmount() {
        return amount;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setTime(@NonNull String time) {
        this.time = time;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


    @Override
    public String toString() {
        return String.format(Locale.ROOT, "TodayEntry{id=%d, time='%s', amount=%d}", id, time, amount);
    }
}
