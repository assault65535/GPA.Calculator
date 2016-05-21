package Models.GPAMethods.LevelIdentifier;

/**
 * Created by Tnecesoc on 2016/5/13.
 */
public class BasicIdentifier implements ScoreIdentifier {
    @Override
    public double identify(double score) {

//        成绩	    GPA
//        100～90	4.0
//        89～80 	3.0
//        79～70    2.0
//        69～60	    1.0
//        59～0	    0

        if (score >= 90) {
            return 4.0;
        }

        if (score >= 80) {
            return 3.0;
        }

        if (score >= 70) {
            return 2.0;
        }

        if (score >= 60) {
            return 1.0;
        }

        return 0;
    }
}
