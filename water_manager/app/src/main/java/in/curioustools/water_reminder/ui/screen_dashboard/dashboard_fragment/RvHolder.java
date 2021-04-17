package in.curioustools.water_reminder.ui.screen_dashboard.dashboard_fragment;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

import in.curioustools.water_reminder.R;

class RvHolder extends RecyclerView.ViewHolder {
    ImageButton ibtQty;
    TextView tvQty;

    RvHolder(@NonNull View itemView) {
        super(itemView);
        ibtQty = itemView.findViewById(R.id.ibt_qty_btn);
        tvQty = itemView.findViewById(R.id.tv_qty_text);
        //ibtQty.setOnTouchListener(getMyItemTouchListener());

    }

    @SuppressLint("SetTextI18n")
    void bind(int qtyRes, final int qty, final QuantityButtonsAdapter.QuantityButtonClickListener listener, final boolean isLast) {

        ibtQty.setImageResource(qtyRes);

        if (!isLast) {
            tvQty.setText(String.format(Locale.getDefault(), "%d ml", qty));
        } else {
            tvQty.setText("Add new");
        }
        ibtQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLast) {
                    listener.onAddNewItemClick();
                } else {
                    listener.onItemClick(qty);
                }

//                    showButtonPressAnimation(view);

            }
        });
    }

}
