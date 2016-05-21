package Models.GPAMethods.LevelIdentifier;

/**
 * Created by Tnecesoc on 2016/5/13.
 */
public class Improved1Identifier implements ScoreIdentifier {
    @Override
    public double identify(double score) {

//        (2)改进4.0(1)
//        成绩	    学分
//        100～85	4.0
//        84～70 	3.0
//        69～60 	2.0
//        59～0	    0

        if (score >= 85) {
            return 4.0;
        }

        if (score >= 70) {
            return 3.0;
        }

        if (score >= 60) {
            return 2.0;
        }

        return 0;
    }
}
