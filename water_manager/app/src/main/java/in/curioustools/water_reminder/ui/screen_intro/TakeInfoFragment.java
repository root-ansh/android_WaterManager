package in.curioustools.water_reminder.ui.screen_intro;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lantouzi.wheelview.WheelView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import in.curioustools.water_reminder.R;
import in.curioustools.water_reminder.UiUtilities.TimeUtilities;
import in.curioustools.water_reminder.db.pref.PrefUserDetails;
import in.curioustools.water_reminder.services.ServicesHandler;
import in.curioustools.water_reminder.ui.custom.wave_loader.WaveLoadingView;
import in.curioustools.water_reminder.ui.screen_dashboard.DashBoardActivity;

import static android.content.Context.MODE_PRIVATE;
import static in.curioustools.water_reminder.db.pref.PrefUserDetails.*;

public class TakeInfoFragment extends Fragment {
    //views : gender
    private LinearLayout llGender;
    private ImageButton ibtMan, ibtWomen;

    //views : weight
    private LinearLayout llWeight;
    private WheelView wheelView;
    private LinearLayout llTimings;
    private TextView tvWakeup, tvSleepTime;

    //views : user activity
    private LinearLayout llActivity;
    private Button btLots, btSome, btFew, btNone;

    //views : other
    private Button btContinue;

    private boolean allBoxesShown = false;


    @Nullable
    private SharedPreferences prefBasicInfo;

    @Nullable
    private OnSharedPreferenceChangeListener prefListener;


    public TakeInfoFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater
                .inflate(
                        R.layout.fragment_take_info_fragment_animated,
                        container,
                        false
                );
    }

    @Override
    public void onViewCreated(@NonNull final View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        initUi(rootView);
        initPref(rootView);
        setUiData(prefBasicInfo);

        ibtMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                putInfoInPref(KEYS.KEY_GENDER, UserGender.MALE);
            }
        });
        ibtWomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                putInfoInPref(KEYS.KEY_GENDER, UserGender.FEMALE);

            }
        });
        wheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onWheelItemChanged(WheelView wheelView, int position) {
                //do nothing
            }

            @Override
            public void onWheelItemSelected(WheelView wheelView, int position) {
                putInfoInPref(KEYS.KEY_WEIGHT, position);

            }
        });
        tvSleepTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker(KEY_TIME.SLEEP_TIME, 23, rootView);
            }
        });
        tvWakeup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker(KEY_TIME.WAKE_UP_TIME, 6, rootView);
            }
        });
        btLots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                putInfoInPref(KEYS.KEY_ACTIVITY, UserActivity.LOTS);

            }
        });

        btSome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                putInfoInPref(KEYS.KEY_ACTIVITY, UserActivity.SOME);

            }
        });

        btFew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                putInfoInPref(KEYS.KEY_ACTIVITY, UserActivity.AFEW);

            }
        });

        btNone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                putInfoInPref(KEYS.KEY_ACTIVITY, UserActivity.NONE);
            }
        });

        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //handle show/hide animations
                if (!allBoxesShown) {
                    showInformationBoxes(rootView);
                } else {
                    putInfoInPref(KEYS.KEY_DATE_TODAY, TimeUtilities.getCurrentDate());
                    putInfoInPref(KEYS.KEY_SHOWN_INFO_ACTIVITY,true);
                    putInfoInPref(KEYS.KEY_SHOW_NOTIFS,true);
                    putInfoInPref(KEYS.KEY_SHOW_LOGS,true);
                    ServicesHandler.updateServices(rootView.getContext());

                    showContinueDialog(rootView.getContext());

                }
            }
        });
    }

    private enum KEY_TIME {SLEEP_TIME, WAKE_UP_TIME}

    private void showTimePicker(final KEY_TIME sleepOrWake, int defTimeHour, View rootView) {
        Context c = rootView.getContext();
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hr, int min) {
                Calendar date = Calendar.getInstance();
                date.set(Calendar.HOUR_OF_DAY, hr);
                date.set(Calendar.MINUTE, min);
                date.set(Calendar.AM_PM, date.get(Calendar.AM_PM));
                String timeFormatted = new SimpleDateFormat("h:mm a", Locale.ROOT).format(date.getTime());
                if (prefBasicInfo != null) {
                    if (sleepOrWake == KEY_TIME.SLEEP_TIME) {
                        putInfoInPref(KEYS.KEY_SLEEP_TIME, timeFormatted);
                    } else {
                        putInfoInPref(KEYS.KEY_WAKEUP_TIME, timeFormatted);
                    }
                }
            }
        };

        TimePickerDialog dialog = new TimePickerDialog(c, listener, defTimeHour, 0, false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void showContinueDialog(Context ctx) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ctx);

        View v = LayoutInflater.from(ctx)
                .inflate(R.layout.dialog_target_picker, null, false);

        WaveLoadingView loadingView = v.findViewById(R.id.wave_loader_dialog);
        final EditText etDialog = v.findViewById(R.id.et_water_amount_dialog);
        int capacity = calculateWaterCapacity();
        UiAnimations.animateWaveLoader(loadingView, 0, 95);
        UiAnimations.animateNumEditText(etDialog, 0, capacity);


        dialogBuilder
                .setView(v)
                .setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int dailyIntake = Integer.parseInt(etDialog.getText().toString());
                        putInfoInPref(KEYS.KEY_DAILY_TARGET, dailyIntake);

                        Intent intent = new Intent(TakeInfoFragment.this.getActivity(), DashBoardActivity.class);
                        startActivity(intent);
                        if (getActivity() != null) getActivity().finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                })
        ;

        AlertDialog dialog = dialogBuilder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


    }

    private static class UiAnimations {
        static void animateWaveLoader(final WaveLoadingView loadingView, int start, int end) {
            ValueAnimator animator = ValueAnimator.ofInt(start, end);
            animator.setDuration(1000);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    loadingView.setProgressValue((int) valueAnimator.getAnimatedValue());
                }
            });
            animator.start();
        }

        static void animateNumEditText(final EditText etDialog, int start, int end) {
            ValueAnimator animator = ValueAnimator.ofInt(start, end);
            animator.setDuration(1500);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    etDialog.setText(String.format("%s", valueAnimator.getAnimatedValue().toString()));
                }
            });
            animator.start();
        }

    }


    private int calculateWaterCapacity() {

        int weight = Defaults.WEIGHT;
        UserActivity activity = UserActivity.valueOf(Defaults.USER_ACTIVITY);
        UserGender userGender = UserGender.valueOf(Defaults.GENDER);
        if (prefBasicInfo != null) {
            weight = prefBasicInfo.getInt(KEYS.KEY_WEIGHT,Defaults.WEIGHT);
            userGender = UserGender.valueOf(prefBasicInfo.getString(KEYS.KEY_GENDER,Defaults.GENDER));
            activity =  UserActivity.valueOf(prefBasicInfo.getString(KEYS.KEY_ACTIVITY,Defaults.USER_ACTIVITY));
        }
        int resultInMl;
        float genderMultiplier = (userGender == PrefUserDetails.UserGender.MALE) ? 20 : 10;
        float resultInOunces = weight * genderMultiplier / 28.3f;
        float activityAdder = 0;
        switch (activity) {
            case LOTS: {
                activityAdder = 100;
                break;
            }
            case SOME: {
                activityAdder = 150;
                break;
            }
            case AFEW: {
                activityAdder = 200;
                break;
            }
            case NONE: {
                activityAdder = 250;
                break;
            }
        }
        resultInOunces += activityAdder;
        resultInMl = Math.round(resultInOunces * 500 / 40);
        return resultInMl;

    }

    private void initPref(View root) {
        prefBasicInfo = root.getContext().getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        prefListener = new OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

                setUiData(sharedPreferences);
            }
        };

    }

    private void initUi(View v) {
        llGender = v.findViewById(R.id.ll_gender);
        llGender.setVisibility(View.GONE);
        ibtMan = v.findViewById(R.id.ibt_man);
        ibtWomen = v.findViewById(R.id.ibt_woman);
        ibtMan.setSelected(false);
        ibtWomen.setSelected(false);


        llWeight = v.findViewById(R.id.ll_weight);
        llWeight.setVisibility(View.GONE);

        wheelView = v.findViewById(R.id.wheelview);
        List<String> wheelDataList = new ArrayList<>();
        for (int i = 0; i < 1005; i++) {
            wheelDataList.add("" + i);
        }
        wheelView.setItems(wheelDataList);
        wheelView.setMinSelectableIndex(10);
        wheelView.setMaxSelectableIndex(200);
        wheelView.setAdditionCenterMark(" Kgs");

        llTimings = v.findViewById(R.id.ll_sleep_times);
        llTimings.setVisibility(View.GONE);
        tvSleepTime = v.findViewById(R.id.tv_timepicker_sleep);
        tvWakeup = v.findViewById(R.id.tv_timepicker_wakeup);

        llActivity = v.findViewById(R.id.ll_activity);
        llActivity.setVisibility(View.GONE);
        btLots = v.findViewById(R.id.bt_lots);
        btSome = v.findViewById(R.id.bt_some);
        btFew = v.findViewById(R.id.bt_few);
        btNone = v.findViewById(R.id.bt_none);

        btContinue = v.findViewById(R.id.bt_continue);

    }

    private void setUiData(SharedPreferences pref) {

        String genderEnum = Defaults.GENDER;
        String dailyActivityEnum = Defaults.USER_ACTIVITY;
        int weight = Defaults.WEIGHT;
        String wakeupTime = Defaults.WAKEUP_TIME;
        String sleepTime = Defaults.SLEEP_TIME;
        if (pref != null) {
            weight = pref.getInt(KEYS.KEY_WEIGHT, Defaults.WEIGHT);
            genderEnum = pref.getString(KEYS.KEY_GENDER, Defaults.GENDER);
            wakeupTime = pref.getString(KEYS.KEY_WAKEUP_TIME, Defaults.WAKEUP_TIME);
            sleepTime = pref.getString(KEYS.KEY_SLEEP_TIME, Defaults.SLEEP_TIME);
            dailyActivityEnum = pref.getString(KEYS.KEY_ACTIVITY, Defaults.USER_ACTIVITY);
        }

        if (genderEnum.equals(PrefUserDetails.UserGender.FEMALE.name())) {//1 set gender and show ui chang
            ibtWomen.setSelected(true);
            ibtMan.setSelected(false);
        } else {
            ibtWomen.setSelected(false);
            ibtMan.setSelected(true);
        }//1 set gender and show ui change
        wheelView.selectIndex(weight);//2 set weight and show ui change
        tvSleepTime.setText(sleepTime);//3a set sleep time and show ui change
        tvWakeup.setText(wakeupTime);//3b set wakeup time and show ui change
        PrefUserDetails.UserActivity activityRate = Enum.valueOf(PrefUserDetails.UserActivity.class, dailyActivityEnum);//4 set activities button and show ui change
        switch (activityRate) {
            case LOTS: {
                btLots.setSelected(true);
                btSome.setSelected(false);
                btFew.setSelected(false);
                btNone.setSelected(false);
                break;
            }
            case SOME: {
                btLots.setSelected(false);
                btSome.setSelected(true);
                btFew.setSelected(false);
                btNone.setSelected(false);
                break;
            }
            case AFEW: {
                btLots.setSelected(false);
                btSome.setSelected(false);
                btFew.setSelected(true);
                btNone.setSelected(false);
                break;
            }
            case NONE: {
                btLots.setSelected(false);
                btSome.setSelected(false);
                btFew.setSelected(false);
                btNone.setSelected(true);
                break;
            }
            default: {
                btLots.setSelected(false);
                btSome.setSelected(false);
                btFew.setSelected(false);
                btNone.setSelected(false);
                break;
            }
        }

    }

    private void showInformationBoxes(View v) {


        LinearLayout llroot = v.findViewById(R.id.ll_root_of_content);
        TransitionManager.beginDelayedTransition(llroot);

        if (llGender.getVisibility() == View.GONE) {
            llGender.setVisibility(View.VISIBLE);

        } else if (llWeight.getVisibility() == View.GONE) {
            llWeight.setVisibility(View.VISIBLE);
        } else if (llTimings.getVisibility() == View.GONE) {
            llTimings.setVisibility(View.VISIBLE);
        } else {
            llActivity.setVisibility(View.VISIBLE);// all boxes are already being shown, so simply set all boxes shown to true
            allBoxesShown = true;
            btContinue.setText("FINISH");
        }
        //NestedScrollView nsv = findViewById(R.id.nested_scroll_view);
        //nsv.fullScroll(View.FOCUS_DOWN);

    }


    private void putInfoInPref(String key, Object value) {
        if (prefBasicInfo != null) {
            SharedPreferences.Editor et = prefBasicInfo.edit();
            if (value instanceof Integer) {
                et.putInt(key, (Integer) value);
            } else if (value instanceof String) {
                et.putString(key, (String) value);
            } else if (value instanceof Boolean) {
                et.putBoolean(key, (Boolean) value);
            } else if (value instanceof UserGender) {
                et.putString(key, ((UserGender) value).name());
            } else if (value instanceof UserActivity) {
                et.putString(key, ((UserActivity) value).name());
            }

            et.apply();
        }

    }


    @Override
    public void onResume() {
        super.onResume();
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
