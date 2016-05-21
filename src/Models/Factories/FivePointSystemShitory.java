package Models.Factories;

import Models.PointSystem.NonNumeric.NonNumericPointSystem;
import Models.PointSystem.Numeric.FivePointSystem;
import Models.PointSystem.Numeric.PointSystem;

/**
 * Created by Tnecesoc on 2016/5/13.
 */
public class FivePointSystemShitory implements Shitory {
    @Override
    public PointSystem createPointSystem(double score, double credit) {
        return new FivePointSystem(score, credit);
    }

    @Override
    public NonNumericPointSystem createNonNumericPointSystem(String grade, double credit) {
        return null;
    }
}
