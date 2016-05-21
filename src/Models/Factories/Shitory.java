package Models.Factories;

import Models.PointSystem.NonNumeric.NonNumericPointSystem;
import Models.PointSystem.Numeric.PointSystem;

/**
 * Created by Tnecesoc on 2016/5/13.
 */
public interface Shitory {
    PointSystem createPointSystem(double score, double credit);
    NonNumericPointSystem createNonNumericPointSystem(String grade,double credit);
}
