package in.curioustools.water_reminder.UiUtilities;


import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import androidx.annotation.NonNull;

import com.lantouzi.wheelview.WheelView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import in.curioustools.water_reminder.R;
import in.curioustools.water_reminder.db.pref.PrefUserDetails;

import static in.curioustools.water_reminder.db.pref.PrefUserDetails.UserActivity;
import static in.curioustools.water_reminder.db.pref.PrefUserDetails.UserGender;


public class Dialogs {
    private static UserGender gender;
    private static int weight;

    //private static final String TAG="Diaogs>>";

    public interface OnGenderSelectedListener {
        void onUserSelected(UserGender gender);
    }

    public static void showGenderDialog(@NonNull View root, @NonNull final OnGenderSelectedListener listener) {
        final View v = createView(root.getContext(), ViewType.Gender);

        v.findViewById(R.id.dif_ibt_man).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender = UserGender.MALE;
                v.findViewById(R.id.dif_ibt_man).setSelected(true);
                v.findViewById(R.id.dif_ibt_woman).setSelected(false);
            }
        });

        v.findViewById(R.id.dif_ibt_woman).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender = UserGender.FEMALE;
                v.findViewById(R.id.dif_ibt_man).setSelected(false);
                v.findViewById(R.id.dif_ibt_woman).setSelected(true);

            }
        });

        new AlertDialog.Builder(root.getContext())
                .setCancelable(false)
                .setView(v)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onUserSelected(gender);
                    }
                })
                .show();


    }

    public interface OnWeightSelectedListener {
        void onWeightSelected(int weight);
    }

    public static void showWeightDialog(@NonNull View root, @NonNull final OnWeightSelectedListener listener) {
        View v = createView(root.getContext(), ViewType.Weight);
        WheelView wheelView = v.findViewById(R.id.dif_wheelview);

        wheelView.setMinSelectableIndex(20);
        wheelView.setMaxSelectableIndex(500);
        final ArrayList<String> intArr = new ArrayList<>();
        for (int i = 0; i < 505; i++) {
            intArr.add("" + i);
        }
        wheelView.setItems(intArr);
        wheelView.setAdditionCenterMark("Kg");

        wheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onWheelItemChanged(WheelView wheelView, int position) {

            }

            @Override
            public void onWheelItemSelected(WheelView wheelView, int position) {
                weight = position;
            }
        });


        new AlertDialog.Builder(root.getContext())
                .setCancelable(false)
                .setView(v)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onWeightSelected(weight);
                    }
                })
                .show();
    }

    public interface OnUserActivitySelectedListener {
        void onUsrActivitySelected(UserActivity activity);
    }

    public static void showUserActivitySelectionDialog(@NonNull final View root, @NonNull final OnUserActivitySelectedListener listener) {
        final View v = createView(root.getContext(), ViewType.Activityy);

        new AlertDialog.Builder(root.getContext())
                .setCancelable(false)
                .setView(v)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RadioGroup rg = v.findViewById(R.id.dif_rg_user_activity);
                        switch (rg.getCheckedRadioButtonId()) {
                            case R.id.dif_rb_lots:
                                listener.onUsrActivitySelected(UserActivity.LOTS);
                                break;
                            case R.id.dif_rb_some:
                                listener.onUsrActivitySelected(UserActivity.SOME);
                                break;
                            case R.id.dif_rb_few:
                                listener.onUsrActivitySelected(UserActivity.AFEW);
                                break;
                            case R.id.dif_rb_none:
                                listener.onUsrActivitySelected(UserActivity.NONE);
                                break;

                        }
                    }
                })

                .show();
    }

    public interface OnIntakeEditedListener {
        void onIntakeEdited(int newIntake);
    }

    public static void showEditIntakeDialog(@NonNull View root, @NonNull final OnIntakeEditedListener listener, final int initialVal) {
        final View v = createView(root.getContext(), ViewType.Intake);
        final EditText et = v.findViewById(R.id.dif_et_intake);
        String initialValString = String.format(Locale.ROOT, "%d", initialVal);
        et.setText(initialValString);
        new AlertDialog.Builder(root.getContext())
                .setCancelable(false)
                .setView(v)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        int val;
                        try {
                            val = Integer.parseInt((et.getText().toString()));
                        } catch (Exception e) {
                            val = PrefUserDetails.Defaults.DAILY_TARGET;
                        }
                        listener.onIntakeEdited(val);
                    }
                })
                .show();
    }

    public interface OnTimeSelectedListener {
        void onTimeSelected(String time);
    }

    public static void showTimeDialog(@NonNull View root, final OnTimeSelectedListener l2) {
        Context c = root.getContext();
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hr, int min) {
                Calendar date = Calendar.getInstance();
                date.set(Calendar.HOUR_OF_DAY, hr);
                date.set(Calendar.MINUTE, min);
                date.set(Calendar.AM_PM, date.get(Calendar.AM_PM));
                String timeFormatted = new SimpleDateFormat("h:mm a", Locale.ROOT).format(date.getTime());
                l2.onTimeSelected(timeFormatted);
            }
        };

        TimePickerDialog dialog = new TimePickerDialog(c, listener, 6, 0, false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private enum ViewType {Gender, Weight, Intake, Activityy}

    private static View createView(Context ctx, ViewType type) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.dialog_user_info_all, null, false);

        v.findViewById(R.id.dif_ll_gender).setVisibility(View.GONE);
        v.findViewById(R.id.dif_ll_weight).setVisibility(View.GONE);
        v.findViewById(R.id.dif_ll_intake).setVisibility(View.GONE);
        v.findViewById(R.id.dif_ll_activity).setVisibility(View.GONE);

        switch (type) {
            case Gender:
                v.findViewById(R.id.dif_ll_gender).setVisibility(View.VISIBLE);
                break;
            case Intake:
                v.findViewById(R.id.dif_ll_intake).setVisibility(View.VISIBLE);
                break;
            case Weight:
                v.findViewById(R.id.dif_ll_weight).setVisibility(View.VISIBLE);
                break;
            case Activityy:
                v.findViewById(R.id.dif_ll_activity).setVisibility(View.VISIBLE);
                break;

        }
        return v;

    }


}
