package Models.GPAMethods.LevelIdentifier;

/**
 * Created by Tnecesoc on 2016/5/13.
 */
public class CanadaIdentifier implements ScoreIdentifier {

//    (5)加拿大4.3
//    成绩	    学分
//    100～90	4.3
//    89～85	    4.0
//    84～80 	3.7
//    79～75 	3.3
//    74～70 	3.0
//    69～65	    2.7
//    64～60	    2.3
//    59～0	    0

    @Override
    public double identify(double score) {

        if (score >= 90) {
            return 4.3;
        }

        if (score >= 85) {
            return 4.0;
        }

        if (score >= 80) {
            return 3.7;
        }

        if (score >= 75) {
            return 3.3;
        }

        if (score >= 70) {
            return 3.0;
        }

        if (score >= 65) {
            return 2.7;
        }

        if (score >= 60) {
            return 2.3;
        }

        return 0;
    }
}
