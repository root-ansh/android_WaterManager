package in.curioustools.water_reminder;



public class TestTime {
//
//    @Test
//    public void Test() {
//
//        String currentTime = "12:44 AM";
//
//        System.out.println(currentTime + ":");
//
//        boolean test1 = isTimeInBetween2Times("11:04 PM", "02:03 AM", currentTime);
//        System.out.println((test1 ? "is in between" : "is not in between") + " 11:04 pm and 2:03 am");//should be true
//
//
//        boolean test2 = isTimeInBetween2Times("01:04 AM", "03:03 AM", currentTime);
//        System.out.println((test2 ? "is in between" : "is not in between") + " 1:04 am and 3:03 am"); //should be false
//
//        System.out.println("---------------------------------------------");
//    }
//
//    @Test
//    public void Test2() {
//
//        String currentTime = "01:44 AM";
//
//        System.out.println(currentTime + ":");
//
//        boolean test1 = isTimeInBetween2Times("11:04 PM", "02:03 AM", currentTime);
//        System.out.println((test1 ? "is in between" : "is not in between") + " 11:04 pm and 2:03 am");//should be true
//
//
//        boolean test2 = isTimeInBetween2Times("01:04 AM", "03:03 AM", currentTime);
//        System.out.println((test2 ? "is in between" : "is not in between") + " 1:04 am and 3:03 am"); //should be false
//
//        System.out.println("---------------------------------------------");
//
//
//    }
//
//    @Test
//    public void Test3() {
//
//        String currentTime = "02:44 AM";
//
//        System.out.println(currentTime + ":");
//
//        boolean test1 = isTimeInBetween2Times("11:04 PM", "02:03 AM", currentTime);
//        System.out.println(test1 ? "is in between" : "is not in between" + " 11:04 pm and 2:03 am");//should be true
//
//
//        boolean test2 = isTimeInBetween2Times("01:04 AM", "03:03 AM", currentTime);
//        System.out.println((test2 ? "is in between" : "is not in between") + " 1:04 am and 3:03 am"); //should be false
//
//        System.out.println("---------------------------------------------");
//
//
//    }
//
//
//    private static boolean isTimeInBetween2Times(String startTime, String endTime, String checkTime) {
//        //warning :checks for hours only!
//        String pattern12 = "hh:mm a";
//        String pattern24 = "HH:mm";
//
//        SimpleDateFormat sdf24 = new SimpleDateFormat(pattern24);
//        SimpleDateFormat sdf12 = new SimpleDateFormat(pattern12);
//        boolean isBetween;
//        try {
//            String st24 = sdf24.format(sdf12.parse(startTime));
//            String et24 = sdf24.format(sdf12.parse(endTime));
//            String ct24 = sdf24.format(sdf12.parse(checkTime));
//
//            int stHour24 = Integer.parseInt(st24.substring(0, 2));
//            int etHour24 = Integer.parseInt(et24.substring(0, 2));
//            int ctHour24 = Integer.parseInt(ct24.substring(0, 2));
//
//            //time slabs= [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23]
//
//            //case 1 start<end, ct between start and end? eg ct=1pm, start= 3pm, end=6pm , 3<6
//            if ((stHour24 < etHour24) && (ctHour24 >= stHour24) && (ctHour24 <= etHour24)) {
//                isBetween = true;
//            }
//            //case2 start>end (time slab is 8pm to 5 am ie 16 to 05) , ct between start and end?
//            else if ((stHour24 > etHour24) && (ctHour24 > stHour24 || ctHour24 < etHour24)) {
//                isBetween = true;
//            }
//            //case3: both start and end are same hours,only the the time having same hour is the same time
//            else isBetween = stHour24 == etHour24 && etHour24 == ctHour24;
//
//        } catch (Exception e) {
//            isBetween = false;
//            e.printStackTrace();
//        }
//
//
//        return isBetween;
//
//    }
//
//    @Test
//    public void Test4() {
//        System.out.println(TimeUtilities.getCurrentDate());
//
//    }
//
//
}
