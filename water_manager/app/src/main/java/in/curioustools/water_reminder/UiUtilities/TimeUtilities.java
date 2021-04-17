package in.curioustools.water_reminder.UiUtilities;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtilities {
    private static final String PATTERN_DATE = "EEEE, MMM dd, yyyy";
    private static final String PATTERN_TIME = "hh:mm a";
    private static final String TAG = "TimeUtilities>>";


    public static String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        Date currentDateTime = cal.getTime();
        return getDate(currentDateTime);
    }

    private static String getDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(PATTERN_DATE, Locale.ROOT);
        return formatter.format(date);//String dateString = formatter.format(currentDateTime);
    }

    public static String getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        Date currentDateTime = cal.getTime();
        return getTime(currentDateTime);
    }

    private static String getTime(Date datetime) {

        SimpleDateFormat formatter = new SimpleDateFormat(PATTERN_TIME, Locale.ROOT);
        return formatter.format(datetime);//String timeString = formatter.format(datetime);
    }

    public static boolean isTimeInBetween2Times(String startTime, String endTime, String checkTime) {
        Log.e(TAG, String.format("isTimeInBetween2Times: called for times:%s, %s, %s", startTime, endTime, checkTime));
        //warning :checks for hours only!
        String pattern24 = "HH:mm";

        SimpleDateFormat sdf12 = new SimpleDateFormat(PATTERN_TIME,Locale.ROOT);
        SimpleDateFormat sdf24 = new SimpleDateFormat(pattern24,Locale.ROOT);

        boolean isBetween;
        try {
            String st24 = sdf24.format(sdf12.parse(startTime));//sdf.parse(string) -> nullable date so giving a warning
            String et24 = sdf24.format(sdf12.parse(endTime));
            String ct24 = sdf24.format(sdf12.parse(checkTime));

            int stHour24 = Integer.parseInt(st24.substring(0, 2));
            int etHour24 = Integer.parseInt(et24.substring(0, 2));
            int ctHour24 = Integer.parseInt(ct24.substring(0, 2));

            //time slabs= [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23]

            //case 1 start<end, ct between start and end? eg ct=1pm, start= 3pm, end=6pm , 3<6
            if ((stHour24 < etHour24) && (ctHour24 >= stHour24) && (ctHour24 <= etHour24)) {
                isBetween = true;
            }
            //case2 start>end (time slab is 8pm to 5 am ie 16 to 05) , ct between start and end?
            else if ((stHour24 > etHour24) && (ctHour24 > stHour24 || ctHour24 < etHour24)) {
                isBetween = true;
            }
            //case3: both start and end are same hours,only the the time having same hour is the same time
            else isBetween = stHour24 == etHour24 && etHour24 == ctHour24;

        } catch (Exception e) {
            isBetween = false;
            e.printStackTrace();
        }

        return isBetween;

    }


}

