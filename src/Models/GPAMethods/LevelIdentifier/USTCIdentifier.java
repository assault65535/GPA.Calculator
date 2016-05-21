package Models.GPAMethods.LevelIdentifier;

/**
 * Created by Tnecesoc on 2016/5/13.
 */
public class USTCIdentifier implements ScoreIdentifier {

//    (6)中科大4.3
//    成绩	    学分
//    100～95	4.3
//    94～90	    4.0
//    89～85 	3.7
//    84～82	    3.3
//    81～78	    3.0
//    77～75	    2.7
//    74～72	    2.3
//    71～68	    2.0
//    67～65	    1.7
//    64～64	    1.5
//    63～61	    1.3
//    60～60	    1.0
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

        if (score >= 82) {
            return 3.3;
        }

        if (score >= 78) {
            return 3.0;
        }

        if (score >= 75) {
            return 2.7;
        }

        if (score >= 72) {
            return 2.3;
        }

        if (score >= 68) {
            return 2.0;
        }

        if (score >= 65) {
            return 1.7;
        }

        if (score >= 64) {
            return 1.5;
        }

        if (score >= 61) {
            return 1.3;
        }

        if (score >= 60) {
            return 1.0;
        }

        return 0;
    }
}
