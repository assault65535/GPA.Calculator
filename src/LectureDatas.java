import Models.Factories.Excited;
import Models.GPAMethods.LevelIdentifier.ScoreIdentifier;
import Models.PointSystem.Numeric.PointSystem;

import java.util.ArrayList;

/**
 * Created by Tnecesoc on 2016/5/13.
 */
public class LectureDatas {

    private ArrayList<PointSystem> lecs = new ArrayList<PointSystem>();

    private ScoreIdentifier calculator;

    public void addLectureData(Class lec_class, double score, double credit) {
        this.lecs.add(Excited.findShitoryByClass(lec_class).createPointSystem(score, credit));
    }

    public double getGPA(ScoreIdentifier identifier) {
        double gPoint = 0, totalCredit = 0;
        for(PointSystem i:lecs) {
            gPoint += i.getScore(identifier);
            totalCredit += i.getCredit();
        }

        return gPoint / totalCredit;
    }

    public PointSystem getElementAt(int index) {
        return this.lecs.get(index);
    }

    public void pop() {
        this.lecs.remove(this.lecs.size() - 1);
    }

    public void clear() {
        this.lecs.clear();
    }

    public boolean isEmpty() {
        return this.lecs.isEmpty();
    }
}
