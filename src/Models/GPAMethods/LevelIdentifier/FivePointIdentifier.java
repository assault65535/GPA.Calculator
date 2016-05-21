package Models.GPAMethods.LevelIdentifier;

/**
 * Created by Tnecesoc on 2016/5/13.
 */
public class FivePointIdentifier implements ScoreIdentifier {
    @Override
    public double identify(double score) {

//        五分制算法
//        成绩	学分
//        5	    4.0
//        4	    3.0
//        2	    1.0
//        1～0	0

        if (score > 4)
            return 4.0;

        if(score > 2)
            return 3.0;

        if(score > 1)
            return 1.0;

        return 0.0;
    }
}
