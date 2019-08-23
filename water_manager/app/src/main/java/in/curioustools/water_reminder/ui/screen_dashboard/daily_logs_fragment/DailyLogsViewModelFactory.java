package in.curioustools.water_reminder.ui.screen_dashboard.daily_logs_fragment;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.InvocationTargetException;

import in.curioustools.water_reminder.db.db_water.WaterRepo;


public class DailyLogsViewModelFactory implements ViewModelProvider.Factory {

    private static volatile DailyLogsViewModelFactory FACT_INSTANCE;
    private WaterRepo repo;

    @NonNull
    static synchronized DailyLogsViewModelFactory getFactory(Context appCtx) {
        if (FACT_INSTANCE == null) {
            FACT_INSTANCE = new DailyLogsViewModelFactory(appCtx);
        }
        return FACT_INSTANCE;
    }

    private DailyLogsViewModelFactory(Context appCtx) {
        this.repo = WaterRepo.getRepoInstance(appCtx);
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            return modelClass.getConstructor(WaterRepo.class).newInstance(repo);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        }
    }
}
