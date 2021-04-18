package in.curioustools.water_reminder.ui.screen_dashboard.dashboard_fragment;

import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import in.curioustools.water_reminder.R;
import in.curioustools.water_reminder.db.db_water.model.TodayEntry;
import in.curioustools.water_reminder.utils.JVMBasedUtils;

class TodayEntriesVH extends RecyclerView.ViewHolder {
    TextView tvQtyTitle, tvTime;
    ImageView ivQty;
    ImageButton ibtMenu;
    PopupMenu popupMenuDelete;

    TodayEntriesVH(@NonNull View itemView) {
        super(itemView);
        initViews(itemView);
    }

    private void initViews(@NonNull View v) {
        tvQtyTitle = v.findViewById(R.id.tv_amount);
        tvTime = v.findViewById(R.id.tv_time);
        ivQty = v.findViewById(R.id.iv_qty_icon);
        ibtMenu = v.findViewById(R.id.ibt_popup_menu);
        popupMenuDelete = new PopupMenu(v.getContext(), ibtMenu);
        popupMenuDelete.inflate(R.menu.menu_popup_eachrow_delete);

//            popupMenuDelete.show();
    }

    //todo : use [isLast]
    void bindData(final TodayEntry data, final boolean isLast, @Nullable final OnTodayItemMenuClickListener listener, boolean imperialMetrics) {
        int amount  = imperialMetrics? JVMBasedUtils.convertToFluidOunces(data.getAmountInMilliLitres()):data.getAmountInMilliLitres();
        String units = imperialMetrics ? " fl. oz. " : " ml ";
        String a = "Drank ";
        String b = "" + amount;
        String c = "of water";
        Spanned qtyTextFormatted = Html.fromHtml(a + "<font color=#304EFF><b>" + b + units+ "</b></font>" + c);

        tvQtyTitle.setText(qtyTextFormatted);
        tvTime.setText(String.format("at: %s", data.getTime()));

        popupMenuDelete.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menuitem_delete && listener!=null) {

                listener.onDeleteButtonClick(data);
                return true;
            }
            return false;
        });

        ibtMenu.setOnClickListener(view -> popupMenuDelete.show());
        ivQty.setImageResource(QuantityButtonModel.getResForQty(data.getAmountInMilliLitres()));

    }


}
