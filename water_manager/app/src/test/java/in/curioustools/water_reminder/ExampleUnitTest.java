package in.curioustools.water_reminder;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        updateWave(23, 1500);
    }


    void updateWave(int achieved,int target) {

        int progress = Math.round((float) achieved / target*100);
        progress= progress>100?100:progress;

        System.out.println(progress);


    }
}