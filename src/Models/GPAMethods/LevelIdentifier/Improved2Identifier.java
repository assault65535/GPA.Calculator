package Models.GPAMethods.LevelIdentifier;

/**
 * Created by Tnecesoc on 2016/5/13.
 */
public class Improved2Identifier implements ScoreIdentifier {
    @Override
    public double identify(double score) {

//        (3)改进4.0(2)
//        成绩	    学分
//        100～85	4.0
//        84～75 	3.0
//        74～60	    2.0
//        59～0	    0

        if (score >= 85) {
            return 4.0;
        }

        if (score >= 75) {
            return 3.0;
        }

        if (score >= 60) {
            return 2.0;
        }

        return 0;
    }
}
