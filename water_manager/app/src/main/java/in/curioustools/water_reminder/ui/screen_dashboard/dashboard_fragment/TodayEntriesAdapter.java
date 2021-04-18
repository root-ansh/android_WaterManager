package in.curioustools.water_reminder.ui.screen_dashboard.dashboard_fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.curioustools.water_reminder.R;
import in.curioustools.water_reminder.db.db_water.model.TodayEntry;

public class TodayEntriesAdapter extends RecyclerView.Adapter<TodayEntriesVH> {
    @NonNull
    private List<TodayEntry> entryList = new ArrayList<>();

    @Nullable
    private OnTodayItemMenuClickListener listener = null;

    private boolean imperialMetrics = false;


    @NonNull
    @Override
    public TodayEntriesVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycler_today_entries, parent, false);
        return new TodayEntriesVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TodayEntriesVH holder, int position) {
        boolean isLast = (position == (entryList.size() - 1));
        holder.bindData(entryList.get(position), isLast, listener, imperialMetrics);
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    public void setEntryList(@Nullable List<TodayEntry> entryList) {
        entryList = entryList == null ? new ArrayList<>() : entryList;
        this.entryList = entryList;
        notifyDataSetChanged();
    }

    public void setListener(@NonNull OnTodayItemMenuClickListener listener) {
        this.listener = listener;
        notifyDataSetChanged();
    }

    public void updateMetricsAsImperial(boolean showMetricsAsImperial) {
        this.imperialMetrics = showMetricsAsImperial;
        notifyDataSetChanged();
    }

    //----------------------------------------------------------------------------------------------

}
