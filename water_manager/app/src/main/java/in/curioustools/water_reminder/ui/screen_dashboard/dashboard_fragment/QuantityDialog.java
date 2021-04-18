package in.curioustools.water_reminder.ui.screen_dashboard.dashboard_fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.Locale;

import in.curioustools.water_reminder.R;
import in.curioustools.water_reminder.utils.JVMBasedUtils;


class QuantityDialog {
    @Nullable
    private QuantityButtonModel currentData;
    private final AlertDialog.Builder builder;
    private final boolean showMetricsAsImperial;

    public interface OnPositiveClickListener {
        void onPositiveButtonClick(QuantityButtonModel data);
    }

    QuantityDialog(Context ctx, boolean showMetricsAsImperial) {
        this.currentData = QuantityButtonModel.QUANTITY_GLASS;
        this.showMetricsAsImperial = showMetricsAsImperial;
        View dialogView = createView(ctx);
        this.builder = new AlertDialog.Builder(ctx)
                .setView(dialogView)
                .setCancelable(false)
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
    }

    private View createView(Context ctx) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.dialog_new_quantitiy_btn, null);

        //init ui
        SeekBar seekQty = v.findViewById(R.id.seekbar_qty);
        final TextView tvQty = v.findViewById(R.id.tv_qty_text_dialog);
        final ImageView ivQty = v.findViewById(R.id.iv_qty_icon_dialog);
        //init data and defaults
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekQty.setMin(QuantityButtonModel.QUANTITY_MIN);
        }
        seekQty.setMax(QuantityButtonModel.QUANTITY_MAX);
        if (currentData != null) {
            seekQty.setProgress(currentData.getQty());
            tvQty.setText(getAmountString(currentData.getQty()));
            ivQty.setImageResource(currentData.getQtyImage());
        }
        //init listener
        seekQty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                progress = Math.max(progress, QuantityButtonModel.QUANTITY_MIN);
                tvQty.setText(getAmountString(progress));
                int resID = QuantityButtonModel.getResForQty(progress);
                ivQty.setImageResource(resID);
                currentData = new QuantityButtonModel(resID, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        return v;
    }

    private String getAmountString(int qty) {
        return showMetricsAsImperial
                ? String.format(Locale.ROOT, "%d fl. oz.", JVMBasedUtils.convertToFluidOunces(qty))
                : String.format(Locale.ROOT, "%d ml", qty);
    }

    public void setOnPositiveClickListener(@Nullable final OnPositiveClickListener listener) {
        builder.setPositiveButton("Done", (dialogInterface, i) -> {
            if (listener != null) {
                listener.onPositiveButtonClick(currentData);
            }
            dialogInterface.dismiss();
        });
    }

    public void show() {
        builder.show();
    }
}
