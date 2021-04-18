package in.curioustools.water_reminder.ui.screen_dashboard.dashboard_fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.Locale;

import in.curioustools.water_reminder.R;

import static in.curioustools.water_reminder.ui.screen_dashboard.dashboard_fragment.QuantityButtonModel.*;


class QuantityDialog {
    @Nullable
    private QuantityButtonModel currentData;
    private AlertDialog.Builder builder;

    public interface OnPositiveClickListener {
        void onPositiveButtonClick(QuantityButtonModel data);
    }

    QuantityDialog(Context ctx) {
        this.currentData = QUANTITY_GLASS;
        View dialogView = createView(ctx);
        this.builder = new AlertDialog.Builder(ctx)
                .setView(dialogView)
                .setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
    }

    private View createView(Context ctx) {
        View v = LayoutInflater.from(ctx)
                .inflate(R.layout.dialog_new_quantitiy_btn, null);

        //init ui
        SeekBar seekQty = v.findViewById(R.id.seekbar_qty);
        final TextView tvQty = v.findViewById(R.id.tv_qty_text_dialog);
        final ImageView ivQty = v.findViewById(R.id.iv_qty_icon_dialog);
        //init data and defaults
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekQty.setMin(QUANTITY_MIN);
        }
        seekQty.setMax(QUANTITY_MAX);
        if (currentData != null) {
            seekQty.setProgress(currentData.getQty());
            tvQty.setText(String.format(Locale.ROOT, "%d ml", currentData.getQty()));
            ivQty.setImageResource(currentData.getQtyImage());
        }
        //init listener
        seekQty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                progress = (progress < QUANTITY_MIN) ? QUANTITY_MIN : progress;
                tvQty.setText(String.format(Locale.ROOT, "%d ml", progress));
                int resID = getResForQty(progress);
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

    void setOnPositiveClickListener(@Nullable final OnPositiveClickListener listener) {
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (listener != null) {
                    listener.onPositiveButtonClick(currentData);
                }
                dialogInterface.dismiss();
            }
        });

    }

    void show() {
        builder.show();
    }

}
