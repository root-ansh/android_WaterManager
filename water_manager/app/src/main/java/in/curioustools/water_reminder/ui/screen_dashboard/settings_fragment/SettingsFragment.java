package in.curioustools.water_reminder.ui.screen_dashboard.settings_fragment;



import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Locale;

import in.curioustools.water_reminder.R;
import in.curioustools.water_reminder.UiUtilities.Dialogs;
import in.curioustools.water_reminder.UiUtilities.Dialogs.OnGenderSelectedListener;
import in.curioustools.water_reminder.UiUtilities.Dialogs.OnUserActivitySelectedListener;
import in.curioustools.water_reminder.UiUtilities.Dialogs.OnWeightSelectedListener;
import in.curioustools.water_reminder.db.pref.PrefUserDetails.Defaults;
import in.curioustools.water_reminder.services.ServicesHandler;

import static android.content.Context.*;
import static in.curioustools.water_reminder.db.pref.PrefUserDetails.*;

public class SettingsFragment extends Fragment {
    private static final String TAG="SettingsFragment";

    private LinearLayout llGender, llActivity, llWeight, llDailyNotif, llSleep, llWakeup, llIntake,llAbout;
    private TextView tvGender, tvActivity, tvWeight, tvDailyNotifs, tvSleepTime, tvWakeup, tvIntake;
    private Switch swDailyNotifs;
    private FrameLayout flLadder1, flLadder2;
    @Nullable
    private SharedPreferences prefBasicInfo;
    @Nullable
    private OnSharedPreferenceChangeListener prefListener;

    public SettingsFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        initUI(root);

        initPrefAndListener(root);
        setDataOnUi(prefBasicInfo);
        setUiClickListeners(root);
    }


    private void initUI(View root) {
        llAbout =root.findViewById(R.id.stf_layout_about);
        llGender = root.findViewById(R.id.stf_layout_gender);
        llActivity = root.findViewById(R.id.stf_layout_activity);
        llWeight = root.findViewById(R.id.stf_layout_weight);
        llDailyNotif = root.findViewById(R.id.stf_layout_daily_notif);
        llSleep = root.findViewById(R.id.stf_layout_sleep_time);
        llWakeup = root.findViewById(R.id.stf_layout_wakeup_time);
        llIntake = root.findViewById(R.id.stf_layout_intake);

        tvGender = root.findViewById(R.id.stf_tv_gender);
        tvActivity = root.findViewById(R.id.stf_tv_activity);
        tvWeight = root.findViewById(R.id.stf_tv_weight);

        tvDailyNotifs = root.findViewById(R.id.stf_tv_daily_notif);
        tvSleepTime = root.findViewById(R.id.stf_tv_sleep_time);
        tvWakeup = root.findViewById(R.id.stf_tv_wakeup_time);
        tvIntake = root.findViewById(R.id.stf_tv_intake);
        swDailyNotifs = root.findViewById(R.id.stf_sw_daily_notif);

        flLadder2 = root.findViewById(R.id.stf_fl_ladder2);
        flLadder1 = root.findViewById(R.id.stf_fl_ladder);


    }

    private void setDataOnUi(@Nullable SharedPreferences pref) {
        Log.e(TAG, "setDataOnUi: called" );
        boolean showDailyNotif = Defaults.SHOW_NOTIF;
        String userGender = Defaults.GENDER;
        String userActivity = Defaults.USER_ACTIVITY;
        int weight = Defaults.WEIGHT;
        int intake = Defaults.DAILY_TARGET;
        String wakeUpTime = Defaults.WAKEUP_TIME;
        String sleepTime = Defaults.SLEEP_TIME;

        if (pref != null) {
            showDailyNotif = pref.getBoolean(KEYS.KEY_SHOW_NOTIFS, Defaults.SHOW_NOTIF);
            weight = pref.getInt(KEYS.KEY_WEIGHT, Defaults.WEIGHT);
            userGender = pref.getString(KEYS.KEY_GENDER, Defaults.GENDER);
            wakeUpTime = pref.getString(KEYS.KEY_WAKEUP_TIME, Defaults.WAKEUP_TIME);
            sleepTime = pref.getString(KEYS.KEY_SLEEP_TIME, Defaults.SLEEP_TIME);
            userActivity = pref.getString(KEYS.KEY_ACTIVITY, Defaults.USER_ACTIVITY);
            intake =pref.getInt(KEYS.KEY_DAILY_TARGET,Defaults.DAILY_TARGET);
        }
        tvActivity.setText(userActivity);
        tvGender.setText(userGender);
        tvWeight.setText(String.format(Locale.ROOT, "%d kg", weight));
        tvIntake.setText(String.format(Locale.ROOT, "%d ml", intake));

        if (!showDailyNotif) {
            tvDailyNotifs.setText("Show notification reminder to drink water every few hours is OFF");
            swDailyNotifs.setChecked(false);
            llSleep.setVisibility(View.GONE);
            llWakeup.setVisibility(View.GONE);
            flLadder1.setVisibility(View.GONE);
            flLadder2.setVisibility(View.GONE);
        } else {
            tvDailyNotifs.setText("Show notification reminder to drink water every few hours is ON");
            swDailyNotifs.setChecked(true);
            llSleep.setVisibility(View.VISIBLE);
            llWakeup.setVisibility(View.VISIBLE);
            flLadder1.setVisibility(View.VISIBLE);
            flLadder2.setVisibility(View.VISIBLE);

            tvSleepTime.setText(sleepTime);
            tvWakeup.setText(wakeUpTime);

        }

    }

    private void setUiClickListeners(final View root) {
        llAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://github.com/root-ansh/WaterManager"));
                String title ="Open With:";
                Intent chooser = Intent.createChooser(intent, title);
                root.getContext().startActivity(chooser);
            }
        });

        llGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialogs.showGenderDialog(root, new OnGenderSelectedListener() {
                    @Override
                    public void onUserSelected(UserGender gender) {
                        updatePref(KEYS.KEY_GENDER, gender.name());
                    }
                });
            }
        });
        llWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialogs.showWeightDialog(root, new OnWeightSelectedListener() {
                    @Override
                    public void onWeightSelected(int weight) {
                        updatePref(KEYS.KEY_WEIGHT, weight);
                    }

                });
            }
        });

        llActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialogs.showUserActivitySelectionDialog(root, new OnUserActivitySelectedListener() {
                    @Override
                    public void onUsrActivitySelected(UserActivity userActivity) {
                        updatePref(KEYS.KEY_ACTIVITY, userActivity.name());
                    }
                });
            }
        });

        llIntake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentVal=Defaults.DAILY_TARGET;
                if(prefBasicInfo!=null) {
                    currentVal = prefBasicInfo.getInt(KEYS.KEY_DAILY_TARGET, Defaults.DAILY_TARGET);
                }
                Dialogs.showEditIntakeDialog(root,new Dialogs.OnIntakeEditedListener(){
                    @Override
                    public void onIntakeEdited(int newIntake) {
                        updatePref(KEYS.KEY_DAILY_TARGET, newIntake);
                    }
                },currentVal);
            }
        });

        llDailyNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(prefBasicInfo!=null) {
                    boolean showNotif = prefBasicInfo.getBoolean(KEYS.KEY_SHOW_NOTIFS, Defaults.SHOW_NOTIF);
                    updatePref(KEYS.KEY_SHOW_NOTIFS, !showNotif);
                }
            }
        });

        llWakeup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialogs.showTimeDialog(root, new Dialogs.OnTimeSelectedListener() {
                    @Override
                    public void onTimeSelected(String time) {
                        updatePref(KEYS.KEY_WAKEUP_TIME,time);
                    }
                });
            }
        });

        llSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialogs.showTimeDialog(root, new Dialogs.OnTimeSelectedListener() {
                    @Override
                    public void onTimeSelected(String time) {
                        updatePref(KEYS.KEY_SLEEP_TIME,time);
                    }
                });
            }
        });
    }

    private void initPrefAndListener(final View root) {

        prefBasicInfo = root.getContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        prefListener = new OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                onPreferenceUpdated(sharedPreferences,root);

            }
        };
    }

    private void onPreferenceUpdated(SharedPreferences sharedPreferences,View root) {
        setDataOnUi(sharedPreferences);
        ServicesHandler.updateServices(root.getContext());
    }

    private void updatePref(String key, Object value) {//warning value should be string integer
        if (prefBasicInfo != null) {
            SharedPreferences.Editor spEditior = prefBasicInfo.edit();
            if (value.getClass() == String.class) {
                spEditior.putString(key, (String) value);
            } else if (value.getClass() == Integer.class) {
                spEditior.putInt(key, (Integer) value);
            } else if (value.getClass() == Boolean.class) {
                spEditior.putBoolean(key, (Boolean) value);
            }
            spEditior.apply();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: called" );
        if (prefBasicInfo != null && prefListener != null) {
            prefBasicInfo.registerOnSharedPreferenceChangeListener(prefListener);
        }
    }

    @Override
    public void onPause() {
        if (prefBasicInfo != null && prefListener != null) {
            prefBasicInfo.unregisterOnSharedPreferenceChangeListener(prefListener);
        }
        super.onPause();

    }

}
