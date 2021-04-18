package in.curioustools.water_reminder.db.db_water;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import in.curioustools.water_reminder.db.db_water.model.DailyLog;
import in.curioustools.water_reminder.db.db_water.model.TodayEntry;


public class WaterRepo {
    //always check if you are handling null correctly
    private final WaterDao dao;
    private final ExecutorService ioExecutorService;
    private static volatile WaterRepo REPO_INSTANCE;

    private static final String TAG = "WATER_REPO";

    @NonNull
    public static synchronized WaterRepo getRepoInstance(Context appCtx) {
        if (REPO_INSTANCE == null) {
            REPO_INSTANCE = new WaterRepo(appCtx);
        }
        return REPO_INSTANCE;
    }

    private WaterRepo(Context appCtx) {
        this.dao = WaterDB.getInstance(appCtx, false).getWaterDao();
        this.ioExecutorService = Executors.newSingleThreadExecutor();
    }


    public boolean checkIfLogExists(DailyLog log) {
        boolean logExists = getDailyLogByDetails(log.getDate()) != null;
        Log.e(TAG, "checkIfLogExists: logexists=" + logExists);
        return logExists;

    }


    public void addNewDailyLog(@Nullable final DailyLog log) {
        if (log != null) {
            ioExecutorService.execute(() -> dao.insertDailyLog(log));
        } else {
            Log.e(TAG, "addNewDailyLog: passed log is null");
        }
    }

    public void removeOldDailyLog(@Nullable final DailyLog log) {
        if (log != null) {
            ioExecutorService.execute(() -> dao.deleteDailyLog(log));
        } else {
            Log.e(TAG, "removeOldDailyLog: passed log is null");
        }
    }
//    public void removeAllLogs() {
//        ioExecutorService.execute(new Runnable() {
//            @Override
//            public void run() {
//                dao.deleteAllLogs();
//            }
//        });
//
//    }

//    public void modifyOldDailyLog(@Nullable final DailyLog log) {
//        if (log != null) {
//            ioExecutorService.execute(new Runnable() {
//                @Override
//                public void run() {
//                    dao.updateDailyLog(log);
//                }
//            });
//        } else {
//            Log.e(TAG, "modifyOldDailyLog: passed log is null");
//        }
//    }

    @Nullable
    private DailyLog getDailyLogByDetails(final String date) {
        try {
            return ioExecutorService.submit(() -> dao.getDailyLogByDate(date)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

//    @Nullable
//    public DailyLog getDailyLogByID(final long id) {
//        try {
//            return ioExecutorService.submit(new Callable<DailyLog>() {
//                @Override
//                public DailyLog call() {
//                    return dao.getDailyLogByID(id);
//                }
//            }).get();
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

//    @Nullable
//    public List<DailyLog> getAllDailyLogs() {
//        try {
//            return ioExecutorService.submit(new Callable<List<DailyLog>>() {
//                @Override
//                public List<DailyLog> call() {
//                    return dao.getDailyLogList();
//                }
//            }).get();
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    @Nullable
    public LiveData<List<DailyLog>> getAllDailyLogsObservable() {
        return dao.getDailyLogListLive();
    }

    //--------------------------------------------------------------------


    public void addNewTodayEntry(@Nullable final TodayEntry entry) {
        if (entry != null) {

            ioExecutorService.execute(() -> dao.insertToday(entry));
        } else {
            Log.e(TAG, "addNewTodayEntry: passed entry is null");
        }

    }

    public void removeOldTodayEntry(@Nullable final TodayEntry entry) {
        if (entry != null) {
            ioExecutorService.execute(() -> dao.deleteToday(entry));
        } else {
            Log.e(TAG, "removeOldTodayEntry: passed entry is null");
        }


    }

    public void removeAllTodayEntries() {
        ioExecutorService.execute(dao::deleteAllEntries);
    }

//    public void modifyOldTodayEntry(@Nullable final TodayEntry entry) {
//        if (entry != null) {
//            ioExecutorService.execute(new Runnable() {
//                @Override
//                public void run() {
//                    dao.updateToday(entry);
//                }
//            });
//        } else {
//            Log.e(TAG, "modifyOldTodayEntry: passed entry is null");
//        }
//
//    }


//    @Nullable
//    public TodayEntry getTodayEntryByID(final long id) {
//        try {
//            return ioExecutorService.submit(new Callable<TodayEntry>() {
//                @Override
//                public TodayEntry call() {
//                    return dao.getTodayByID(id);
//                }
//            }).get();
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    @Nullable
//    public List<TodayEntry> getAllTodayEntries() {
//        try {
//            return ioExecutorService.submit(new Callable<List<TodayEntry>>() {
//                @Override
//                public List<TodayEntry> call() {
//                    return dao.getTodayList();
//                }
//            }).get();
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    @Nullable
    public LiveData<List<TodayEntry>> getAllTodayEntriesObservable() {
        return dao.getTodayListLive();
    }

}
