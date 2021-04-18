package in.curioustools.water_reminder.ui.screen_dashboard;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class DashboardFragmentsAdapter extends FragmentStateAdapter {

    private final ArrayList<Fragment> fragList = new ArrayList<>();

    DashboardFragmentsAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    void addFragment(Fragment fragment) {
        fragList.add(fragment);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return fragList.size();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragList.get(position);
    }
}
