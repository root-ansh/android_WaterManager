package in.curioustools.water_reminder.ui.screen_dashboard.dashboard_fragment;

import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.curioustools.water_reminder.R;
import in.curioustools.water_reminder.db.db_water.model.TodayEntry;

public class TodayEntriesAdapter extends RecyclerView.Adapter<TodayEntriesAdapter.RvHolder> {
    @NonNull
    private List<TodayEntry> entryList;
    @Nullable
    private OnMyMenuItemClickListener listener;


    TodayEntriesAdapter() {
        this.entryList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_recycler_today_entries, parent, false);
        return new RvHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RvHolder holder, int position) {

        boolean isLast = (position == (entryList.size() - 1));
        holder.bindData(entryList.get(position), isLast, listener);
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

//    @Nullable
//    public OnMyMenuItemClickListener getListener() {
//        return listener;
//    }
//
//    @NonNull
//    public List<TodayEntry> getEntryList() {
//        return entryList;
//    }

    void setEntryList(@Nullable List<TodayEntry> entryList) {
        entryList = entryList == null ? new ArrayList<TodayEntry>() : entryList;
        this.entryList = entryList;
        notifyDataSetChanged();
    }

    void setListener(@Nullable OnMyMenuItemClickListener listener) {
        this.listener = listener;
        notifyDataSetChanged();
    }

    //----------------------------------------------------------------------------------------------

    class RvHolder extends RecyclerView.ViewHolder {
        TextView tvQtyTitle, tvTime;
        ImageView ivQty;
        ImageButton ibtMenu;
        PopupMenu popupMenuDelete;

        RvHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void initViews(View v) {
            tvQtyTitle = v.findViewById(R.id.tv_amount);
            tvTime = v.findViewById(R.id.tv_time);
            ivQty = v.findViewById(R.id.iv_qty_icon);
            ibtMenu = v.findViewById(R.id.ibt_popup_menu);
            popupMenuDelete = new PopupMenu(v.getContext(), ibtMenu);
            popupMenuDelete.inflate(R.menu.menu_popup_eachrow_delete);

//            popupMenuDelete.show();
        }

        @SuppressWarnings("unused")
        void bindData(final TodayEntry data, final boolean isLast, @Nullable final OnMyMenuItemClickListener listener) {
            String a = "Drank ";
            String b = "" + data.getAmount();
            String c = " ml of water";
            String qtyText = a + b + c;
            Spanned qtyTextFormatted = Html.fromHtml(a + "<font color=#304EFF><b>" + b + "</b></font>" + c);

            tvQtyTitle.setText(qtyTextFormatted);
            tvTime.setText(String.format("at: %s", data.getTime()));

            popupMenuDelete.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.menuitem_delete && listener!=null) {

                        listener.onDeleteButtonClick(data);
                        return true;
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

            ivQty.setImageResource(QuantityButtonModel.getResForQty(data.getAmount()));

        }


    }

    interface OnMyMenuItemClickListener {
        void onDeleteButtonClick(TodayEntry entry);
    }

}
