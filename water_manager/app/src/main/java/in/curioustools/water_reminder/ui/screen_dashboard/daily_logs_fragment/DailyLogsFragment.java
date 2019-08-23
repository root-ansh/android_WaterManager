package in.curioustools.water_reminder.ui.screen_dashboard.daily_logs_fragment;


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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.curioustools.water_reminder.R;
import in.curioustools.water_reminder.db.db_water.model.DailyLog;


public class DailyLogsFragment extends Fragment {
    @Nullable private DailyLogsViewModel model;
    private static final String TAG="DAILY_LOGS_FRAGMENT";

    public DailyLogsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_daily_logs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragView, savedInstanceState);

        FragmentActivity activity =this.getActivity();
        if(activity!=null) {
            model = ViewModelProviders
                    .of(this,
                            DailyLogsViewModelFactory.getFactory(activity.getApplicationContext()
                            ))
                    .get(DailyLogsViewModel.class);
        }
        else {
            Log.e(TAG, "onActivityCreated: activity is null, viewmodel not initialised" );
        }


        final RecyclerView rv =fragView.findViewById(R.id.rv_daily_logs);
        rv.setLayoutManager(new LinearLayoutManager(fragView.getContext()));

        final LinearLayout llPlaceholder=fragView.findViewById(R.id.ll_placeholder);

        final DailyLogsAdapter adp=new DailyLogsAdapter();
        rv.setAdapter(adp);

        adp.setItemsMenuListener(new DailyLogsAdapter.DlRvItemsMenuClickListener() {
            @Override
            public void onDeleteButtonClick(@Nullable DailyLog entry) {
                if(model!=null)model.removeLog(entry);
            }
        });

        if(model!=null){
            LiveData<List<DailyLog>> liveLogs=model.getAllLogs();
            if(liveLogs!=null){
                liveLogs.observe(this, new Observer<List<DailyLog>>() {
                    @Override
                    public void onChanged(List<DailyLog> dailyLogs) {
                        if(dailyLogs==null||dailyLogs.size()==0){
                            llPlaceholder.setVisibility(View.VISIBLE);
                            rv.setVisibility(View.GONE);
                        }
                        else{
                            llPlaceholder.setVisibility(View.GONE);
                            rv.setVisibility(View.VISIBLE);
                        }
                        adp.setLogList(dailyLogs);
                    }
                });
            }
        }




    }


}
