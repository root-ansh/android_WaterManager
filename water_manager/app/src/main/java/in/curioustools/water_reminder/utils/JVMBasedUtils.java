package in.curioustools.water_reminder.utils;

public class JVMBasedUtils {
    /// https://opentextbc.ca/basickitchenandfoodservicemanagement/chapter/imperial-and-u-s-systems-of-measurement/
    public static int convertToFluidOunces(int millilitres){
        return (int) (millilitres/29.57);
    }

    public static int convertToMillis(int fluidOunces){
        return (int) (fluidOunces * 29.57);
    }
}
