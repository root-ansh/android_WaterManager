package in.curioustools.water_reminder.ui.screen_dashboard.dashboard_fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.guilhe.views.CircularProgressView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.curioustools.water_reminder.R;
import in.curioustools.water_reminder.utils.JVMBasedUtils;
import in.curioustools.water_reminder.utils.TimeUtilities;
import in.curioustools.water_reminder.utils.AndroidBasedUtils;
import in.curioustools.water_reminder.db.pref.PrefUserDetails.Defaults;
import in.curioustools.water_reminder.db.pref.PrefUserDetails.KEYS;
import in.curioustools.water_reminder.ui.custom.carousal_layout_manager.CarouselLayoutManager;
import in.curioustools.water_reminder.ui.custom.carousal_layout_manager.CarouselZoomPostLayoutListener;
import in.curioustools.water_reminder.db.db_water.WaterRepo;
import in.curioustools.water_reminder.db.db_water.model.TodayEntry;

import static android.content.Context.MODE_PRIVATE;
import static in.curioustools.water_reminder.db.pref.PrefUserDetails.PREF_NAME;
import static in.curioustools.water_reminder.ui.custom.carousal_layout_manager.CarouselLayoutManager.HORIZONTAL;
import static in.curioustools.water_reminder.ui.screen_dashboard.dashboard_fragment.QuantityButtonsAdapter.*;


public class DashboardFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener, QuantityButtonClickListener, TodayEntriesAdapter.OnMyMenuItemClickListener {
    //----------------<global>---------------------------

    @Nullable
    View fragRootView;
    @Nullable
    RecyclerView rvButtons;

    private final QuantityButtonsAdapter adpButtons = new QuantityButtonsAdapter();
    private final TodayEntriesAdapter adpEntries = new TodayEntriesAdapter();

    @Nullable
    private WaterRepo repo;

    @Nullable
    private SharedPreferences prefBasicInfo;

    //----------------</global>---------------------------

    //----------------<lifecycle funcs>---------------------------

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View fragView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragView, savedInstanceState);
        fragRootView = fragView;
        //initUI

        rvButtons = fragView.findViewById(R.id.rv_buttons);
        if (rvButtons != null) {
            adpButtons.setButtonModelList(QuantityButtonModel.getDefaultButtonList());
            adpButtons.setClickListener(this);

            CarouselLayoutManager lm = new CarouselLayoutManager(HORIZONTAL, true);
            lm.setPostLayoutListener(new CarouselZoomPostLayoutListener());
            rvButtons.setLayoutManager(lm);
            rvButtons.setHasFixedSize(true);
            rvButtons.setAdapter(adpButtons);
            rvButtons.scrollToPosition(adpButtons.getItemCount() / 2);
        }


        RecyclerView rvTodayEntries = fragView.findViewById(R.id.rv_today_entries);
        if (rvTodayEntries != null) {
            adpEntries.setEntryList(new ArrayList<>());
            adpEntries.setListener(this);

            rvTodayEntries.setLayoutManager(new LinearLayoutManager(fragView.getContext()));
            rvTodayEntries.setHasFixedSize(true);
            rvTodayEntries.setAdapter(adpEntries);
        }

        //init preferences
        prefBasicInfo = fragView.getContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        setUiFromPreferences(prefBasicInfo);
        AndroidBasedUtils.makeDateChanges(prefBasicInfo, repo);

        //setRepoAndLiveData
        repo = WaterRepo.getRepoInstance(fragView.getContext().getApplicationContext());
        LiveData<List<TodayEntry>> liveEntries = repo.getAllTodayEntriesObservable();
        if (liveEntries != null) {
            liveEntries.observe(this, adpEntries::setEntryList);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (prefBasicInfo == null || repo == null) return;

        prefBasicInfo.registerOnSharedPreferenceChangeListener(this);
        setUiFromPreferences(prefBasicInfo);

        AndroidBasedUtils.makeDateChanges(prefBasicInfo, repo);

    }

    @Override
    public void onPause() {
        if (prefBasicInfo != null) {
            prefBasicInfo.unregisterOnSharedPreferenceChangeListener(this);
        }
        super.onPause();

    }

    //----------------</lifecycle funcs>---------------------------

    //----------------<funcs from implemented interfaces>---------------------------

    @Override
    public void onSharedPreferenceChanged(SharedPreferences pref, String s) {
        if (repo != null && prefBasicInfo != null) {
            AndroidBasedUtils.makeDateChanges(prefBasicInfo, repo);
        }

        setUiFromPreferences(pref);
    }


    @Override
    public void onQtyButtonClick(int qty) {
        if (repo == null || prefBasicInfo == null) return;

        String time = TimeUtilities.getCurrentTime();

        int origQty = prefBasicInfo.getInt(KEYS.KEY_TODAY_INTAKE_ACHIEVED, Defaults.TODAY_INTAKE_ACHIEVED);

        TodayEntry entry = new TodayEntry();
        entry.setTime(time);
        entry.setAmountInMilliLitres(qty);

        //---
        repo.addNewTodayEntry(entry);
        prefBasicInfo.edit().putInt(KEYS.KEY_TODAY_INTAKE_ACHIEVED, origQty + qty).apply();

        AndroidBasedUtils.makeDateChanges(prefBasicInfo, repo);

    }

    @Override
    public void onAddNewQtyButtonClick() {
        if (fragRootView == null || rvButtons == null) return;

        QuantityDialog dialog;
        dialog = new QuantityDialog(fragRootView.getContext());
        dialog.setOnPositiveClickListener(
                data -> {
                    adpButtons.addItemInCentre(data);
                    rvButtons.scrollToPosition(adpButtons.getItemCount() / 2);
                });
        dialog.show();
    }

    @Override
    public void onDeleteButtonClick(TodayEntry entry) {
        if (repo != null) {
            repo.removeOldTodayEntry(entry);
        }
    }

    //----------------</funcs from implemented interfaces>---------------------------

    //----------------<other funcs>---------------------------


    private void setUiFromPreferences(@Nullable SharedPreferences pref) {
        setProgressBar(pref);
        setProgressImage(pref);
        setTargetAndAchievedAmount(pref);
    }

    private void setProgressImage(@Nullable SharedPreferences pref) {
        // we can return on null root view but not null  preference data, we gota use fallback
        if (fragRootView == null) return;

        final int progressImageRes;

        if (pref == null) {
            progressImageRes = R.drawable.ic_progress_centre_happy;
        } else {
            String sleepTime = pref.getString(KEYS.KEY_SLEEP_TIME, Defaults.SLEEP_TIME);
            String wakeTime = pref.getString(KEYS.KEY_WAKEUP_TIME, Defaults.WAKEUP_TIME);
            String currentTime = TimeUtilities.getCurrentTime();

            boolean isBetweenSleep = TimeUtilities.isTimeInBetween2Times(sleepTime, wakeTime, currentTime);
            if (isBetweenSleep) {
                progressImageRes = R.drawable.ic_progress_centre_sleeping;
            } else {
                int achieved = pref.getInt(KEYS.KEY_TODAY_INTAKE_ACHIEVED, Defaults.TODAY_INTAKE_ACHIEVED);
                int target = pref.getInt(KEYS.KEY_DAILY_TARGET, Defaults.DAILY_TARGET);
                if (achieved > target) {
                    progressImageRes = R.drawable.ic_progress_centre_target_crossed;
                } else if (achieved >= target / 4) {
                    progressImageRes = R.drawable.ic_progress_centre_happy;
                } else {
                    progressImageRes = R.drawable.ic_progress_centre_thirsty;
                }
            }
        }

        ImageView ivProgressViewImage = fragRootView.findViewById(R.id.iv_progress_centre);
        ivProgressViewImage.setImageResource(progressImageRes);

    }

    private void setTargetAndAchievedAmount(@Nullable SharedPreferences pref) {
        if (fragRootView == null) return;

        final int target;
        final int achieved;
        final boolean showAsImperial;

        if (pref == null) {
            target = Defaults.DAILY_TARGET;
            achieved = Defaults.TODAY_INTAKE_ACHIEVED;
            showAsImperial = Defaults.SHOW_IMPERIAL_MM;
        } else {
            target = pref.getInt(KEYS.KEY_DAILY_TARGET, Defaults.DAILY_TARGET);
            achieved = pref.getInt(KEYS.KEY_TODAY_INTAKE_ACHIEVED, Defaults.TODAY_INTAKE_ACHIEVED);
            showAsImperial = pref.getBoolean(KEYS.KEY_SHOW_IMPERIAL_MM, Defaults.SHOW_IMPERIAL_MM);
        }

        String finalTargetString = showAsImperial
                ? String.format(Locale.ROOT, "%s Fl. Oz.", JVMBasedUtils.convertToFluidOunces(target))
                : String.format(Locale.ROOT, "%s ml", target);

        int finalAchievedValue = showAsImperial ? JVMBasedUtils.convertToFluidOunces(achieved) : achieved;

        TextView tvAchieved = fragRootView.findViewById(R.id.fdv_tv_achieved);
        TextView tvTarget = fragRootView.findViewById(R.id.fdv_tv_target);

        tvTarget.setText(finalTargetString);
        AndroidBasedUtils.setTextAnimated(finalAchievedValue, tvAchieved);//tvAchieved.setText(String.valueOf(achieved));

    }

    private void setProgressBar(@Nullable SharedPreferences pref) {
        if (fragRootView == null) return;

        int target;
        int achieved;
        final boolean showAsImperial;

        if (pref == null) {
            target = Defaults.DAILY_TARGET;
            achieved = Defaults.TODAY_INTAKE_ACHIEVED;
            showAsImperial = Defaults.SHOW_IMPERIAL_MM;
        } else {
            target = pref.getInt(KEYS.KEY_DAILY_TARGET, Defaults.DAILY_TARGET);
            achieved = pref.getInt(KEYS.KEY_TODAY_INTAKE_ACHIEVED, Defaults.TODAY_INTAKE_ACHIEVED);
            showAsImperial = pref.getBoolean(KEYS.KEY_SHOW_IMPERIAL_MM, Defaults.SHOW_IMPERIAL_MM);
        }

        if (showAsImperial) {
            achieved = JVMBasedUtils.convertToFluidOunces(achieved);
            target = JVMBasedUtils.convertToFluidOunces(target);
        }

        CircularProgressView progressView = fragRootView.findViewById(R.id.progress_daily_intake);

        //setInitialData();
        progressView.setProgress(achieved);
        progressView.setMax(target);

    }

    //----------------</other funcs>---------------------------


}
