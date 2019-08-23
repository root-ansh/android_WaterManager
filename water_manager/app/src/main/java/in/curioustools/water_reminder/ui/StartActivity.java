package in.curioustools.water_reminder.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import in.curioustools.water_reminder.R;
import in.curioustools.water_reminder.services.ServicesHandler;
import in.curioustools.water_reminder.ui.screen_intro.IntroInfoActivity;
import in.curioustools.water_reminder.ui.screen_dashboard.DashBoardActivity;

import static in.curioustools.water_reminder.db.pref.PrefUserDetails.*;

public class StartActivity extends AppCompatActivity {

    //private static final String TAG = "startActivity";
    Class classToBeLaunched;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        ServicesHandler.updateServices(this);

        SharedPreferences pref= getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        boolean shownOneTime=pref.getBoolean(KEYS.KEY_SHOWN_INFO_ACTIVITY, Defaults.HAS_SHOWN_INTRO_INFO_ACTIVITY);
        classToBeLaunched = shownOneTime ?DashBoardActivity.class:IntroInfoActivity.class;
        startActivity(new Intent(StartActivity.this, classToBeLaunched));
        finish();

        // TODO: 23-08-2019
        //  1. this whole project was started a month ago on 23/7/19
        //  2. a reset today stats button is needed in dashboard fragment
        //  3. some sort of toast is needed to remind user that button pane is available to add new data
        //  4. Dialogs.class should me more used for very dialog in app. currently not fully used b other classes
        //  5. Notif maker needs to make a notification with large icon &/or custom view
        //  6. Somehow needs to show animation for Today entry adapter. currently not showing
        //  7. Use more viewmodel in dashboard fragment
        //  8. Daily Log service enable/disable ui needs to be added in settings frag
        //  9. about page needs to be local and not just a link
        //  10. intro info fragment could be somehow reused since intro info and settings frag are mostly same but not necessary feature


    }

}
