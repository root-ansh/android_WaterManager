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
        //  01. this whole project was started a month ago on 23/7/19
        //  02. a reset today stats button is needed in dashboard fragment
        //  03. some sort of toast is needed to remind user that button pane is available to add new data
        //  04. Dialogs.class should be more used for very dialog in app. currently not fully used b other classes
        //  05. Notif maker needs to make a notification with large icon &/or custom view
        //  06. Somehow needs to show animation for Today entry adapter. currently not showing
        //  07. Use more viewmodel in dashboard fragment
        //  08. Daily Log service enable/disable ui needs to be added in settings frag
        //  09. about page needs to be local and not just a link
        //  10. intro info fragment could be somehow reused since intro info and settings frag are mostly same but not necessary feature
        //  11. notification is showing a small bug when first launched. it shows 0/2000 even though user's calculated amount is 3204ml
        //  12. need to replace stop notifications button in notification with "stop notification for the day" (plus the logic)
        //  13. notification is not making a log in today entry database.
        //  14. notification on click should open app.
        //  15. [SEVERE]:SETTINGS HAS SLEEP TIME WRITTEN TWICE!
        //  16. Notification should have an option to customize(like in settings , user can change the glass qty)
        //  17. Notification **INTERVAL** should have an option to customize(like in settings , user can select weather they want notifications every 15mins/1 hour/2 hour etc)
        

    }

}
