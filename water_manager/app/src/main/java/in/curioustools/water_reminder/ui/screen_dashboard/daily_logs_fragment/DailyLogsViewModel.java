package in.curioustools.water_reminder.ui.screen_dashboard.daily_logs_fragment;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import in.curioustools.water_reminder.db.db_water.WaterRepo;
import in.curioustools.water_reminder.db.db_water.model.DailyLog;


public class DailyLogsViewModel extends ViewModel {
    private WaterRepo repo;

    public DailyLogsViewModel(@Nullable WaterRepo repo) {
        this.repo = repo;
    }

    @Nullable
    LiveData<List<DailyLog>> getAllLogs(){
        return repo.getAllDailyLogsObservable();
    }
    void removeLog(@Nullable DailyLog log) {
        if (log != null) {
            repo.removeOldDailyLog(log);
        }
    }

}
