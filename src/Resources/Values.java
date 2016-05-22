package Resources;

import Models.GPAMethods.GradeIndenifier.ElevenGradeIdentifier;
import Models.GPAMethods.GradeIndenifier.GradeIdentifier;
import Models.GPAMethods.LevelIdentifier.*;
import Models.PointSystem.NonNumeric.GradeSystem;
import Models.PointSystem.NonNumeric.NonNumericPointSystem;
import Models.PointSystem.Numeric.CentesimalSystem;
import Models.PointSystem.Numeric.FivePointSystem;
import Models.PointSystem.Numeric.PointSystem;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Tnecesoc on 2016/5/13.
 */
public class Values {

    public static final String lbl_GPATextIfEmpty = "wait for +1s";

    public static final String levelIdentifierPackage = Lets.getSimplePkgName(ScoreIdentifier.class);

    public static final String gradeIdentifierPackage = Lets.getSimplePkgName(GradeIdentifier.class);

    public static final String numericSystemPackage = Lets.getSimplePkgName(PointSystem.class);

    public static final String nonNumericSystemPackage = Lets.getSimplePkgName(NonNumericPointSystem.class);

    public static final String[] modelsForCentesimalSystem = {
            Lets.getIdentifierName(BasicIdentifier.class),
            Lets.getIdentifierName(CanadaIdentifier.class),
            Lets.getIdentifierName(Improved1Identifier.class),
            Lets.getIdentifierName(Improved2Identifier.class),
            Lets.getIdentifierName(PKUIdentifier.class),
            Lets.getIdentifierName(SJTUIdentifier.class),
            Lets.getIdentifierName(USTCIdentifier.class),
    };

    public static final String[] modelsForFivePointSystem = {
            Lets.getIdentifierName(FivePointIdentifier.class),
    };

    public static final String[] modelsForGradeSystem = {
            Lets.getIdentifierName(ElevenGradeIdentifier.class),
    };

    public static final HashMap<String, String[]> inUsePointSystems = new HashMap<String, String[]>() {{
        put(
                CentesimalSystem.class.getSimpleName(),
                Values.modelsForCentesimalSystem
        );

        put(
                FivePointSystem.class.getSimpleName(),
                Values.modelsForFivePointSystem
        );

        put(
                GradeSystem.class.getSimpleName(),
                Values.modelsForGradeSystem
        );
    }};

    public static final HashSet<String> nonNumericSystems = new HashSet<String>() {{
        add(GradeSystem.class.getSimpleName());
    }};

    public static final HashSet<String> numericSystems = new HashSet<String>() {{
        add(CentesimalSystem.class.getSimpleName());
        add(FivePointSystem.class.getSimpleName());
    }};

    public static final HashMap<String, String> threadNameOf = new HashMap<String, String>() {{
        put("turnLabelValuesAnimation","turnLabelValuesAnimation");
    }};

    private static class Lets {
        public static String getIdentifierName(Class T) {
            return getSimpleNameWithoutPostfix(T,"Identifier");
        }

        public static String getSimpleNameWithoutPostfix(Class T,String Postfix) {
            return T.getSimpleName().substring(0, T.getSimpleName().length() - Postfix.length());
        }

        public static String getSimplePkgName(Class T) {
            return T.getPackage().toString().substring("Package ".length());
        }
    }
}

