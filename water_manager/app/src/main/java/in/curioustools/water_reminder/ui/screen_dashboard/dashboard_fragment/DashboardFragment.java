package in.curioustools.water_reminder.ui.screen_dashboard.dashboard_fragment;

import android.animation.ValueAnimator;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import in.curioustools.water_reminder.utils.TimeUtilities;
import in.curioustools.water_reminder.utils.UtilMethods;
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


public class DashboardFragment extends Fragment {
    //----------------<global>---------------------------

    private static final String TAG = "DashboardFragment";


    private TextView tvAchieved, tvTarget;
    private RecyclerView rvButtons;
    private QuantityButtonsAdapter adpButtons;
    private TodayEntriesAdapter adpEntries;

    @Nullable
    private WaterRepo repo;

    private CircularProgressView progressView;
    private ImageView ivProgressViewImage;

    @Nullable
    private SharedPreferences prefBasicInfo;
    @Nullable
    private SharedPreferences.OnSharedPreferenceChangeListener prefListener;
    //----------------</global>---------------------------
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View fragView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragView, savedInstanceState);
        initUi(fragView);
        setInitialData();
        initPrefAndListener(fragView);
        if (repo != null && prefBasicInfo != null) {
            UtilMethods.makeDateChanges(prefBasicInfo, repo);
        }
        setUiFromPreferences(prefBasicInfo);
        setRepoAndLiveData(fragView);
        setListeners(fragView);
    }

    private void initUi(View fragView) {
        rvButtons = fragView.findViewById(R.id.rv_buttons);
        CarouselLayoutManager lm = new CarouselLayoutManager(HORIZONTAL, true);
        lm.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        rvButtons.setLayoutManager(lm);
        rvButtons.setHasFixedSize(true);
        adpButtons = new QuantityButtonsAdapter();
        rvButtons.setAdapter(adpButtons);
        RecyclerView rvTodayEntries = fragView.findViewById(R.id.rv_today_entries);
        rvTodayEntries.setLayoutManager(new LinearLayoutManager(fragView.getContext()));
        rvTodayEntries.setHasFixedSize(true);
        adpEntries = new TodayEntriesAdapter();
        rvTodayEntries.setAdapter(adpEntries);
        ivProgressViewImage = fragView.findViewById(R.id.iv_progress_centre);
        progressView = fragView.findViewById(R.id.progress_daily_intake);
        tvAchieved = fragView.findViewById(R.id.fdv_tv_achieved);
        tvTarget = fragView.findViewById(R.id.fdv_tv_target);
    }

    private void setInitialData() {
        progressView.setProgress(0);
        progressView.setMax(0);

        ivProgressViewImage.setImageResource(getProgressImage(null));

        adpButtons.setButtonModelList(QuantityButtonModel.getDefaultButtonList());
        rvButtons.scrollToPosition(adpButtons.getItemCount() / 2);
        adpEntries.setEntryList(new ArrayList<>());
        tvTarget.setText(tvTarget.getContext().getString(R.string.zero_ml));
        tvAchieved.setText("0");
    }

    private void updateTextAnimated(int initialVal, int finalVal, final TextView tv) {
        ValueAnimator animator = ValueAnimator.ofInt(initialVal, finalVal);
        animator.setDuration(1500);
        animator.addUpdateListener(
                valueAnimator -> {
                    String s = String.format("%s", valueAnimator.getAnimatedValue().toString());
                    tv.setText(s);
                }
        );
        animator.start();
    }

    private int getProgressImage(@Nullable SharedPreferences pref) {
        if (pref == null) {
            return R.drawable.ic_progress_centre_happy;
        } else {
            String sleepTime = pref.getString(KEYS.KEY_SLEEP_TIME, Defaults.SLEEP_TIME);
            String wakeTime = pref.getString(KEYS.KEY_WAKEUP_TIME, Defaults.WAKEUP_TIME);
            String currentTime = TimeUtilities.getCurrentTime();

            boolean isBetweenSleep = TimeUtilities.isTimeInBetween2Times(sleepTime, wakeTime, currentTime);
            if (isBetweenSleep) {
                return R.drawable.ic_progress_centre_sleeping;
            } else {
                int achieved = pref.getInt(KEYS.KEY_TODAY_INTAKE_ACHIEVED, Defaults.TODAY_INTAKE_ACHIEVED);
                int target = pref.getInt(KEYS.KEY_DAILY_TARGET, Defaults.DAILY_TARGET);
                if (achieved > target) {
                    return R.drawable.ic_progress_centre_target_crossed;
                } else if (achieved >= target / 4) {
                    return R.drawable.ic_progress_centre_happy;
                } else {
                    return R.drawable.ic_progress_centre_thirsty;
                }
            }
        }
    }

    private void initPrefAndListener(View root) {
        Log.e(TAG, "initPrefAndListener: called");
        prefBasicInfo = root.getContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        Log.e(TAG, "initPrefAndListener: setted preference:" + prefBasicInfo);
        prefListener = (pref, s) -> {
            Log.e(TAG, "onSharedPreferenceChanged: preference updated calling setUiFromPreferences(pref) for pref= " + pref);
            if (repo != null && prefBasicInfo!=null) {
                UtilMethods.makeDateChanges(prefBasicInfo, repo);
            }

            setUiFromPreferences(pref);

        };
    }

    private void setUiFromPreferences(@Nullable SharedPreferences pref) {
        Log.e(TAG, "setUiFromPreferences: called");
        if (pref != null) {
            Log.e(TAG, "setUiFromPreferences: calling pref!=null");
            Log.e(TAG, "setUiFromPreferences: calling for progress,max");

            int progress = pref.getInt(KEYS.KEY_TODAY_INTAKE_ACHIEVED, Defaults.TODAY_INTAKE_ACHIEVED);
            int max = pref.getInt(KEYS.KEY_DAILY_TARGET, Defaults.DAILY_TARGET);

            Log.e(TAG, String.format("setUiFromPreferences: progress,max=%d,%d", progress, max));

            progressView.setProgress(progress, true);
            progressView.setMax(max);


            tvTarget.setText(String.format(Locale.ROOT, "%d ml", max));
            int initial = Integer.parseInt(tvAchieved.getText().toString());
            updateTextAnimated(initial, progress, tvAchieved);
        }
        ivProgressViewImage.setImageResource(getProgressImage(pref));
    }


    private void setRepoAndLiveData(View fragView) {
        Log.e(TAG, "setRepoAndLiveData: called");
        repo = WaterRepo.getRepoInstance(fragView.getContext().getApplicationContext());
        LiveData<List<TodayEntry>> liveEntries = repo.getAllTodayEntriesObservable();
        Log.e(TAG, String.format("setRepoAndLiveData: setted repo and livedata as: %s, %s", repo, liveEntries));
        if (liveEntries != null) {
            Log.e(TAG, "setRepoAndLiveData: liventries aren't null");
            liveEntries.observe(this, todayEntries -> {
                Log.e(TAG, "onChanged: recieved list in livedata observer :" + todayEntries);
                if (adpEntries != null) {
                    Log.e(TAG, "onChanged: adpentries is not null");
                    adpEntries.setEntryList(todayEntries);//doesn't matter even if list is null
                }
            });
        }
    }

    private void setListeners(final View fragView) {
        adpEntries.setListener(entry -> {
            Log.e(TAG, "adpEntries:onDeleteButtonClick: button is clicked! entry recieved=" + entry);

            if (repo != null) {
                Log.e(TAG, "onDeleteButtonClick: calling:repo.removeOldTodayEntry(entry)");

                repo.removeOldTodayEntry(entry);
            }
        });

        adpButtons.setClickListener(new QuantityButtonClickListener() {
            @Override
            public void onItemClick(int qty) {
                Log.e(TAG, "adpButtons:onItemClick: button is clicked! qty received=" + qty);
                if (repo != null) {
                    Log.e(TAG, "onItemClick: repo is not null");
                    String time = TimeUtilities.getCurrentTime();
                    TodayEntry entry = new TodayEntry();
                    entry.setTime(time);
                    entry.setAmount(qty);
                    Log.e(TAG, "onItemClick: created new entry:" + entry);
                    Log.e(TAG, "onItemClick: calling repo.addNewTodayEntry(entry)");
                    repo.addNewTodayEntry(entry);
                    //rvTodayEntries.scrollToPosition(0);
                    Log.e(TAG, "onItemClick: calling addQtyToPref(qty)");
                    addQtyToPref(qty);

                }

            }

            @Override
            public void onAddNewItemClick() {
                Log.e(TAG, "onAddNewItemClick: called");
                QuantityDialog dialog;
                dialog = new QuantityDialog(fragView.getContext());
                dialog.setOnPositiveClickListener(
                        data -> {
                            adpButtons.addItemInCentre(data);
                            //  15-08-2019 add item in shared pref
                            //  update: we are not doing that. now we have in memory 5 buttons
                            //  and 1 add new button. user can create temporary buttons but that
                            //  would be removed as soon as the app is killed.
                            rvButtons.scrollToPosition(adpButtons.getItemCount() / 2);
                        });
                dialog.show();
            }
        });
    }

    private void addQtyToPref(int newInsertionQty) {
        if (prefBasicInfo != null) {
            int origQty = prefBasicInfo.getInt(KEYS.KEY_TODAY_INTAKE_ACHIEVED, Defaults.TODAY_INTAKE_ACHIEVED);
            prefBasicInfo.edit().putInt(KEYS.KEY_TODAY_INTAKE_ACHIEVED, origQty + newInsertionQty).apply();

            if (repo != null) {
                UtilMethods.makeDateChanges(prefBasicInfo, repo);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: called");
        if (prefBasicInfo != null && prefListener != null) {
            prefBasicInfo.registerOnSharedPreferenceChangeListener(prefListener);
            setUiFromPreferences(prefBasicInfo);
            if (repo != null) {
                UtilMethods.makeDateChanges(prefBasicInfo, repo);

            }
        }
    }

    @Override
    public void onPause() {
        if (prefBasicInfo != null && prefListener != null) {
            prefBasicInfo.unregisterOnSharedPreferenceChangeListener(prefListener);
            Log.e(TAG, "onPause: listener unregisterd");
        }
        super.onPause();

    }

}
