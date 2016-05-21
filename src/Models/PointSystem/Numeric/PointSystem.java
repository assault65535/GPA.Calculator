package Models.PointSystem.Numeric;

import Models.GPAMethods.LevelIdentifier.ScoreIdentifier;

/**
 * Created by Tnecesoc on 2016/5/13.
 */
public interface PointSystem {
    double getScore(ScoreIdentifier identifier);
    double getCredit();
}
