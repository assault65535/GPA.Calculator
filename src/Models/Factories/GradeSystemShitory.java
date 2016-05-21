package Models.Factories;

import Models.PointSystem.NonNumeric.GradeSystem;
import Models.PointSystem.NonNumeric.NonNumericPointSystem;
import Models.PointSystem.Numeric.PointSystem;

/**
 * Created by Tnecesoc on 2016/5/13.
 */
public class GradeSystemShitory implements Shitory {
    @Override
    public PointSystem createPointSystem(double score, double credit) {
        return null;
    }

    @Override
    public NonNumericPointSystem createNonNumericPointSystem(String grade, double credit) {
        return new GradeSystem(grade, credit);
    }
}
