package in.curioustools.water_reminder.ui.screen_dashboard.dashboard_fragment;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

import in.curioustools.water_reminder.R;


public class QuantityButtonsAdapter extends RecyclerView.Adapter<QuantityButtonsAdapter.RvHolder> {


    @NonNull
    private ArrayList<QuantityButtonModel> buttonModelList;
    private QuantityButtonClickListener clickListener;

    QuantityButtonsAdapter() {
        this(new ArrayList<QuantityButtonModel>(), null);
    }

    private QuantityButtonsAdapter(@NonNull ArrayList<QuantityButtonModel> buttonModelList,
                                   QuantityButtonClickListener listener) {
        this.buttonModelList = buttonModelList;
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public RvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_recycler_buttons, parent, false);
        return new RvHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RvHolder holder, int pos) {
        QuantityButtonModel data = buttonModelList.get(pos);

        //is last is a check based on which our code to add new data will get triggered.
        boolean isLast = (data == QuantityButtonModel.QUANTITY_ADD_NEW);
        holder.bind(data.getQtyImage(), data.getQty(), clickListener, isLast);
    }

    @Override
    public int getItemCount() {
        return buttonModelList.size();
    }

//    @NonNull
//    public ArrayList<QuantityButtonModel> getButtonModelList() {
//        return buttonModelList;
//    }

    void setButtonModelList(@NonNull ArrayList<QuantityButtonModel> buttonModelList) {
        this.buttonModelList = buttonModelList;
        notifyDataSetChanged();
    }

//    public QuantityButtonClickListener getClickListener() {
//        return clickListener;
//    }

     void setClickListener(QuantityButtonClickListener clickListener) {
        this.clickListener = clickListener;
        notifyDataSetChanged();
    }


     void addItemInCentre(QuantityButtonModel model) {
        int pos = buttonModelList.size() / 2;
        buttonModelList.add(pos, model);
        notifyItemInserted(pos);
    }

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
        void bind(int qtyRes, final int qty, final QuantityButtonClickListener listener, final boolean isLast) {

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


    interface QuantityButtonClickListener {
        void onItemClick(int qty);

        void onAddNewItemClick();
    }

     @SuppressWarnings("unused")
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

}
