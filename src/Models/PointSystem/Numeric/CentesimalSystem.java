package Models.PointSystem.Numeric;

import Models.GPAMethods.LevelIdentifier.ScoreIdentifier;

/**
 * Created by Tnecesoc on 2016/5/13.
 */
public class CentesimalSystem implements PointSystem {

    private double score, credit;

    private double modify(double score) {
        if(score > 100)
            return 100;
        if(score < 0)
            return 0;
        return score;
    }

    public CentesimalSystem(double score, double credit) {
        this.score = modify(score);
        this.credit = credit;
    }

    @Override
    public double getScore(ScoreIdentifier identifier) {
        return identifier.identify(score) * credit;
    }

    @Override
    public double getCredit() {
        return credit;
    }
}
