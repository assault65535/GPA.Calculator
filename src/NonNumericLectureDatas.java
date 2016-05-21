import Models.Factories.Excited;
import Models.GPAMethods.GradeIndenifier.GradeIdentifier;
import Models.PointSystem.NonNumeric.NonNumericPointSystem;

import java.util.ArrayList;

/**
 * Created by Tnecesoc on 2016/5/13.
 */
public class NonNumericLectureDatas {

    private ArrayList<NonNumericPointSystem> lecs = new ArrayList<NonNumericPointSystem>();

    public void addLectureData(Class lec_class, String grade, double credit) {
        this.lecs.add(Excited.findShitoryByClass(lec_class).createNonNumericPointSystem(grade, credit));
    }

    public double getGPA(GradeIdentifier identifier) {
        double gPoint = 0, totalCredit = 0;
        for(NonNumericPointSystem i:lecs) {
            gPoint += i.getScore(identifier);
            totalCredit += i.getCredit();
        }

        return gPoint / totalCredit;
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
