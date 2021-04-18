package in.curioustools.water_reminder.ui.screen_dashboard.dashboard_fragment;


import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

import in.curioustools.water_reminder.R;
import in.curioustools.water_reminder.utils.JVMBasedUtils;

class QuantityButtonsVH extends RecyclerView.ViewHolder {
    private final ImageButton ibtQty;
    private final TextView tvQty;

    public QuantityButtonsVH(@NonNull View itemView) {
        super(itemView);
        ibtQty = itemView.findViewById(R.id.ibt_qty_btn);
        tvQty = itemView.findViewById(R.id.tv_qty_text);
        //ibtQty.setOnTouchListener(getMyItemTouchListener());

    }

    void bindData(int qtyRes, final int qty, final QuantityButtonClickListener listener, final boolean isLast, boolean imperialMetrics) {

        ibtQty.setImageResource(qtyRes);

        if (!isLast) {
            String text = imperialMetrics
                    ?  String.format(Locale.ROOT, "%d fl. oz.", JVMBasedUtils.convertToFluidOunces(qty))
                    : String.format(Locale.ROOT, "%d ml", qty);
            tvQty.setText(text);
        } else {
            tvQty.setText(tvQty.getContext().getString(R.string.add_new));
        }
        ibtQty.setOnClickListener(view -> {
            if (isLast) {
                listener.onAddNewQtyButtonClick();
            } else {
                listener.onQtyButtonClick(qty);
            }
        });
    }

}
  /*
    private static void showButtonPressAnimation(View view) {
        final float shrinkTo = 0.90f;
        final long duration = 100;
        ScaleAnimation grow = new ScaleAnimation(
                shrinkTo, 1,
                shrinkTo, 1,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        grow.setDuration(duration / 2);


        ScaleAnimation shrink = new ScaleAnimation(
                1, shrinkTo,
                1, shrinkTo,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        shrink.setDuration(duration / 2);

        grow.setStartOffset(duration / 2);
        AnimationSet set = new AnimationSet(true);
        set.setInterpolator(new LinearInterpolator());
        set.addAnimation(shrink);
        set.addAnimation(grow);
        view.startAnimation(set);
    }
    */
