package in.curioustools.water_reminder.ui.screen_dashboard.daily_logs_fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


import in.curioustools.water_reminder.R;
import in.curioustools.water_reminder.db.db_water.model.DailyLog;

public class DailyLogsAdapter extends RecyclerView.Adapter<DailyLogsVH> {
    @NonNull
    private List<DailyLog> logList;
    @Nullable
    private DlRvItemsMenuClickListener listener;

    private boolean imperialMetrics = false;

    DailyLogsAdapter() {
        this.logList = new ArrayList<>();
    }

    @NonNull
    @Override
    public DailyLogsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DailyLogsVH(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyLogsVH holder, int position) {
        holder.bindData(logList.get(position), listener,imperialMetrics);
    }

    @Override
    public int getItemCount() {
        return logList.size();
    }

//    @Nullable
//    public DlRvItemsMenuClickListener getListener() {
//        return listener;
//    }
//
//    @NonNull
//    public List<DailyLog> getLogList() {
//        return logList;
//    }

    public void setLogList(@Nullable List<DailyLog> logList) {
        logList = logList == null ? new ArrayList<>() : logList;
        this.logList = logList;
        notifyDataSetChanged();
    }

    public void setItemsMenuListener(@Nullable DlRvItemsMenuClickListener listener) {
        this.listener = listener;
        notifyDataSetChanged();
    }

    public void updateMetricsAsImperial(boolean showMetricsAsImperial) {
        this.imperialMetrics = showMetricsAsImperial;
        notifyDataSetChanged();
    }

    interface DlRvItemsMenuClickListener {
        void onDeleteButtonClick(@Nullable DailyLog entry);
    }

}
