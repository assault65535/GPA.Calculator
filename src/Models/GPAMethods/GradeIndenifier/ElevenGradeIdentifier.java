package Models.GPAMethods.GradeIndenifier;

/**
 * Created by Tnecesoc on 2016/5/13.
 */
public class ElevenGradeIdentifier implements GradeIdentifier {
    @Override
    public double identify(String grade) {

//        等级制算法
//        成绩	学分	成绩	    学分
//        A	    4.0	A-	    3.7
//        B+	3.3	B	    3
//        B-	2.7	C+	    2.3
//        C	    2.0	C-	    1.7
//        D+	1.3	D	    1.0
//        F	    0


        if(grade.startsWith("A"))
            return 4.0;

        if(grade.startsWith("A-"))
            return 3.7;

        if(grade.startsWith("B+"))
            return 3.3;

        if(grade.startsWith("B"))
            return 3.0;

        if(grade.startsWith("B-"))
            return 2.7;

        if(grade.startsWith("C+"))
            return 2.3;

        if(grade.startsWith("C"))
            return 2.0;

        if(grade.startsWith("C-"))
            return 1.7;

        if(grade.startsWith("D+"))
            return 1.3;

        if(grade.startsWith("D"))
            return 1.0;

        return 0;
    }
}
