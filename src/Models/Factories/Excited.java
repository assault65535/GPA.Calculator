package Models.Factories;

import java.awt.geom.Arc2D;

/**
 * Created by Tnecesoc on 2016/5/13.
 */
public class Excited {

    public static Shitory findShitoryByClass(Class T) {
        Shitory ans = null;

        String pkg = Shitory.class.getPackage().toString().substring("Package ".length());

        try {
            ans = (Shitory) Class.forName(
                    pkg
                            + "."
                            + T.getSimpleName()
                            + "Shitory"
            ).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return ans;
    }

}
