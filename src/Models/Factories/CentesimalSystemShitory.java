package Models.Factories;

import Models.PointSystem.NonNumeric.NonNumericPointSystem;
import Models.PointSystem.Numeric.CentesimalSystem;
import Models.PointSystem.Numeric.PointSystem;

/**
 * Created by Tnecesoc on 2016/5/13.
 */
public class CentesimalSystemShitory implements Shitory {
    @Override
    public PointSystem createPointSystem(double score, double credit) {
        return new CentesimalSystem(score, credit);
    }

    @Override
    public NonNumericPointSystem createNonNumericPointSystem(String grade, double credit) {
        return null;
    }
}
