package in.curioustools.water_reminder.ui.screen_dashboard.dashboard_fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import in.curioustools.water_reminder.R;

public class QuantityButtonsAdapter extends RecyclerView.Adapter<QuantityButtonsVH> {
    @NonNull
    private ArrayList<QuantityButtonModel> buttonModelList = new ArrayList<>();

    @Nullable
    private QuantityButtonClickListener clickListener = null;

    private boolean imperialMetrics = false;

    @NonNull
    @Override
    public QuantityButtonsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_recycler_buttons, parent, false);
        return new QuantityButtonsVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull QuantityButtonsVH holder, int pos) {
        QuantityButtonModel data = buttonModelList.get(pos);

        //is last is a check based on which our code to add new data will get triggered.
        boolean isLast = (data == QuantityButtonModel.QUANTITY_ADD_NEW);
        holder.bindData(data.getQtyImage(), data.getQty(), clickListener, isLast, imperialMetrics);
    }

    @Override
    public int getItemCount() {
        return buttonModelList.size();
    }

    public void addItemInCentre(QuantityButtonModel model) {
        int pos = buttonModelList.size() / 2;
        buttonModelList.add(pos, model);
        notifyItemInserted(pos);
    }

    public void updateButtonModelList(@NonNull ArrayList<QuantityButtonModel> buttonModelList) {
        this.buttonModelList = buttonModelList;
        notifyDataSetChanged();
    }


    public void updateClickListener(@NonNull QuantityButtonClickListener clickListener) {
        this.clickListener = clickListener;
        notifyDataSetChanged();
    }

    public void updateMetricsAsImperial(boolean showMetricsAsImperial) {
        this.imperialMetrics = showMetricsAsImperial;
        notifyDataSetChanged();
    }




}
