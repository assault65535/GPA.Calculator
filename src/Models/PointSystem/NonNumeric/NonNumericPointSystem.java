package Models.PointSystem.NonNumeric;

import Models.GPAMethods.GradeIndenifier.GradeIdentifier;

/**
 * Created by Tnecesoc on 2016/5/13.
 */
public interface NonNumericPointSystem {
    double getScore(GradeIdentifier identifier);
    double getCredit();
}
