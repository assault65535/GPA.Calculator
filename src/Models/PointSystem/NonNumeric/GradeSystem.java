package Models.PointSystem.NonNumeric;

import Models.GPAMethods.GradeIndenifier.GradeIdentifier;

/**
 * Created by Tnecesoc on 2016/5/13.
 */
public class GradeSystem implements NonNumericPointSystem {

    private String grade;
    private double credit;

    public GradeSystem(String grade, double credit) {
        this.grade = grade;
        this.credit = credit;
    }

    @Override
    public double getScore(GradeIdentifier identifier) {
        return identifier.identify(grade) * credit;
    }

    @Override
    public double getCredit() {
        return credit;
    }
}
