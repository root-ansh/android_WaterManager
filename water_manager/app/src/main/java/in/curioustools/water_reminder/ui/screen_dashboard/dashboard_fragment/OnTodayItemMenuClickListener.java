package in.curioustools.water_reminder.ui.screen_dashboard.dashboard_fragment;

import in.curioustools.water_reminder.db.db_water.model.TodayEntry;

interface OnTodayItemMenuClickListener {
    void onDeleteButtonClick(TodayEntry entry);
}
