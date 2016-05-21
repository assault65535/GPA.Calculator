package Models.GPAMethods.LevelIdentifier;

/**
 * Created by Tnecesoc on 2016/5/13.
 */
public class SJTUIdentifier implements ScoreIdentifier {

//    (7)上海交大4.3
//    成绩	    学分
//    100～95	4.3
//    94～90 	4.0
//    89～85	    3.7
//    84～80	    3.3
//    79～75	    3.0
//    74～70	    2.7
//    69～67	    2.3
//    66～65	    2.0
//    64～62	    1.7
//    61～60	    1.0
//    59～0	    0

    @Override
    public double identify(double score) {

        if (score >= 95) {
            return 4.3;
        }

        if (score >= 90) {
            return 4.0;
        }

        if (score >= 85) {
            return 3.7;
        }

        if (score >= 80) {
            return 3.3;
        }

        if (score >= 75) {
            return 3.0;
        }

        if (score >= 70) {
            return 2.7;
        }

        if (score >= 67) {
            return 2.3;
        }

        if (score >= 65) {
            return 2.0;
        }

        if (score >= 62) {
            return 1.7;
        }

        if (score >= 60) {
            return 1.0;
        }

        return 0;
    }
}
