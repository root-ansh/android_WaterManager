package in.curioustools.water_reminder.ui.screen_intro;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import in.curioustools.water_reminder.R;

@SuppressLint({"SetTextI18n", "InflateParams", "SameParameterValue"})
public class IntroInfoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_info);
        initToolbar();

        if (savedInstanceState == null) {
            Fragment infoFragment = new TakeInfoFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.ll_intro_info_fragroot, infoFragment)
                    .commit();
        }
    }

    private void initToolbar() {
        final Toolbar t = findViewById(R.id.toolbar_main);
        setSupportActionBar(t);
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.layout_collapsing);
        AppBarLayout appBarLayout = findViewById(R.id.layout_appbar);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("Welcome");
                    collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });


    }



}

