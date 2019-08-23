package in.curioustools.water_reminder.ui.screen_dashboard.daily_logs_fragment;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


import in.curioustools.water_reminder.R;
import in.curioustools.water_reminder.db.db_water.model.DailyLog;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.O;

public class DailyLogsAdapter extends RecyclerView.Adapter<DailyLogsAdapter.RvHolder> {
    @NonNull
    private List<DailyLog> logList;
    @Nullable
    private DlRvItemsMenuClickListener listener;

    DailyLogsAdapter() {
        this.logList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_recycler_daily_logs, parent, false);
        return new RvHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RvHolder holder, int position) {

        holder.bindData(logList.get(position), listener);
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

    void setLogList(@Nullable List<DailyLog> logList) {
        logList = logList == null ? new ArrayList<DailyLog>() : logList;
        this.logList = logList;
        notifyDataSetChanged();
    }

    void setItemsMenuListener(@Nullable DlRvItemsMenuClickListener listener) {
        this.listener = listener;
        notifyDataSetChanged();
    }

    class RvHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvAchievedAndTarget;
        ImageView ivQty;
        ProgressBar pbAchievedAndTarget;
        ImageButton ibtMenu;
        PopupMenu popupMenuDelete;

        RvHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
            bindData(null, null);
        }

        private void initViews(View v) {
            tvDate = v.findViewById(R.id.dl_tv_date);
            tvAchievedAndTarget = v.findViewById(R.id.dl_tv_values);
            ivQty = v.findViewById(R.id.dl_iv_qty_icon);

            pbAchievedAndTarget = v.findViewById(R.id.dl_progressbar);
            pbAchievedAndTarget.setIndeterminate(false);

            ibtMenu = v.findViewById(R.id.dl_ibt_popup_menu);
            popupMenuDelete = new PopupMenu(v.getContext(), ibtMenu);
            popupMenuDelete.inflate(R.menu.menu_popup_eachrow_delete);


        }

        void bindData(@Nullable final DailyLog data, @Nullable final DlRvItemsMenuClickListener listener) {

            if (data == null) {
                tvDate.setText("");
                tvAchievedAndTarget.setText("");
                ivQty.setImageResource(R.drawable.ic_container_glass_colored);
                if (SDK_INT >= O) {
                    pbAchievedAndTarget.setMin(0);
                }
                pbAchievedAndTarget.setMax(100);

            } else {
                //extracting data
                String date = data.getDate();
                int achieved = data.getAchieved(), target = data.getTarget();

                tvDate.setText(date);

                String stat = "" + achieved + "/" + target + " ml";
                tvAchievedAndTarget.setText(stat);

                if (SDK_INT >= O) {
                    pbAchievedAndTarget.setMin(0);
                }
                pbAchievedAndTarget.setMax(target);
                if (achieved > target) {
                    pbAchievedAndTarget.setProgress(target);
                    ivQty.setImageResource(R.drawable.ic_star_golden);
                    ivQty.setBackgroundTintList(ColorStateList.valueOf(ivQty.getResources().getColor(R.color.golden_light)));
                } else {
                    pbAchievedAndTarget.setProgress(achieved);
                    ivQty.setBackgroundTintList(ColorStateList.valueOf(ivQty.getResources().getColor(R.color.blue_very_light)));
                    ivQty.setImageResource(R.drawable.ic_container_glass_colored);
                }


                popupMenuDelete.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.menuitem_delete) {
                            if (listener != null) {
                                listener.onDeleteButtonClick(data);
                                return true;
                            }
                        }
                        return false;
                    }
                });

                ibtMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupMenuDelete.show();
                    }
                });
//              not working correctly when used wih livedata. need to ask commonsware
//            if (isLast) {
//                itemView.findViewById(R.id.fl_ladder_design).setVisibility(View.GONE);
//            }

            }
        }


    }

    interface DlRvItemsMenuClickListener {
        void onDeleteButtonClick(@Nullable DailyLog entry);
    }

}
