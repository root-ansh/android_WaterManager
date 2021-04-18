package in.curioustools.water_reminder.ui.screen_dashboard.dashboard_fragment;


import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

import in.curioustools.water_reminder.R;

class QuantityButtonsVH extends RecyclerView.ViewHolder {
    ImageButton ibtQty;
    TextView tvQty;

    QuantityButtonsVH(@NonNull View itemView) {
        super(itemView);
        ibtQty = itemView.findViewById(R.id.ibt_qty_btn);
        tvQty = itemView.findViewById(R.id.tv_qty_text);
        //ibtQty.setOnTouchListener(getMyItemTouchListener());

    }

    void bindData(int qtyRes, final int qty, final QuantityButtonsAdapter.QuantityButtonClickListener listener, final boolean isLast) {

        ibtQty.setImageResource(qtyRes);

        if (!isLast) {
            tvQty.setText(String.format(Locale.getDefault(), "%d ml", qty));
        } else {
            tvQty.setText(tvQty.getContext().getString(R.string.add_new));
        }
        ibtQty.setOnClickListener(view -> {
            if (isLast) {
                listener.onAddNewQtyButtonClick();
            } else {
                listener.onQtyButtonClick(qty);
            }

//                    showButtonPressAnimation(view);

        });
    }

}
