package in.curioustools.water_reminder.ui.screen_dashboard.daily_logs_fragment;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import in.curioustools.water_reminder.db.db_water.WaterRepo;
import in.curioustools.water_reminder.db.db_water.model.DailyLog;


public class DailyLogsViewModel extends ViewModel {
    private final WaterRepo repo;

    public DailyLogsViewModel(@Nullable WaterRepo repo) {
        this.repo = repo;
    }

    @Nullable
    public LiveData<List<DailyLog>> getAllLogs() throws Exception {
        if(repo!=null)
            return repo.getAllDailyLogsObservable();
        else
            throw new Exception("DailyLogsViewModel >> repo is null");
    }
    public void removeLog(@Nullable DailyLog log) {
        if (log != null && repo!= null) {
            repo.removeOldDailyLog(log);
        }
    }

}
