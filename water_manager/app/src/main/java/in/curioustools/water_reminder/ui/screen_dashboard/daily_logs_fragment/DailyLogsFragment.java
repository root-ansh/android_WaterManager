package in.curioustools.water_reminder.ui.screen_dashboard.daily_logs_fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.curioustools.water_reminder.R;
import in.curioustools.water_reminder.db.db_water.model.DailyLog;
import in.curioustools.water_reminder.db.pref.PrefUserDetails;

import static android.content.Context.MODE_PRIVATE;
import static in.curioustools.water_reminder.db.pref.PrefUserDetails.PREF_NAME;


public class DailyLogsFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Nullable
    private DailyLogsViewModel dailyLogsVM;
    private static final String TAG = "DAILY_LOGS_FRAGMENT";
    private final DailyLogsAdapter adp = new DailyLogsAdapter();

    @Nullable
    private SharedPreferences prefBasicInfo;

    public DailyLogsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_daily_logs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragView, savedInstanceState);

        FragmentActivity activity = this.getActivity();
        if (activity != null) {
            dailyLogsVM = new ViewModelProvider(this, DailyLogsViewModelFactory.getFactory(activity.getApplicationContext())).get(DailyLogsViewModel.class);
        } else {
            Log.e(TAG, "onActivityCreated: activity is null, viewmodel not initialised");
        }

        final LinearLayout llPlaceholder = fragView.findViewById(R.id.ll_placeholder);

        final RecyclerView rv = fragView.findViewById(R.id.rv_daily_logs);
        rv.setLayoutManager(new LinearLayoutManager(fragView.getContext()));
        rv.setAdapter(adp);

        adp.setItemsMenuListener(entry -> {
            if (dailyLogsVM != null) dailyLogsVM.removeLog(entry);
        });

        if (dailyLogsVM != null) {
            try {
                LiveData<List<DailyLog>> liveLogs = dailyLogsVM.getAllLogs();
                if (liveLogs == null) return;
                liveLogs.observe(
                        this,
                        dailyLogs -> {
//for testing dailyLogs:
//                            dailyLogs = new ArrayList<>();
//                            dailyLogs.add( new DailyLog(10L,"abcd",1500,800));
//                            dailyLogs.add( new DailyLog(11L,"abcd",2500,1800));
//                            dailyLogs.add( new DailyLog(12L,"abcd",3500,2800));
//                            dailyLogs.add( new DailyLog(13L,"abcd",4500,4500));

                            if (dailyLogs.size() == 0) {
                                llPlaceholder.setVisibility(View.VISIBLE);
                                rv.setVisibility(View.GONE);
                            } else {
                                llPlaceholder.setVisibility(View.GONE);
                                rv.setVisibility(View.VISIBLE);


                                adp.setLogList(dailyLogs);
                                updateAdapterForImperialMeasurements();
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        prefBasicInfo = fragView.getContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);


    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        updateAdapterForImperialMeasurements();
    }

    private void updateAdapterForImperialMeasurements() {
        if(prefBasicInfo == null) return;
        boolean showMetricsAsImperial = prefBasicInfo.getBoolean(
                PrefUserDetails.KEYS.KEY_SHOW_IMPERIAL_MM,
                PrefUserDetails.Defaults.SHOW_IMPERIAL_MM
        );
        adp.updateMetricsAsImperial(showMetricsAsImperial);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (prefBasicInfo != null) {
            prefBasicInfo.registerOnSharedPreferenceChangeListener(this);
            updateAdapterForImperialMeasurements();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (prefBasicInfo != null) {
            prefBasicInfo.unregisterOnSharedPreferenceChangeListener(this);
        }
    }
}
