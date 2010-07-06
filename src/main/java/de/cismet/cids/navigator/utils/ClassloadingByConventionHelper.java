package de.cismet.cids.navigator.utils;

import de.cismet.tools.BlacklistClassloading;
import java.util.List;

/**
 *
 * @author stefan
 */
public class ClassloadingByConventionHelper {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ClassloadingByConventionHelper.class);

    public static String camelizeTableName(String tableName) {
        boolean upperCase = true;
        char[] result = new char[tableName.length()];
        int resultPosition = 0;
        for (int i = 0; i < tableName.length(); ++i) {
            char current = tableName.charAt(i);
            if (Character.isLetterOrDigit(current)) {
                if (upperCase) {
                    current = Character.toUpperCase(current);
                    upperCase = false;
                } else {
                    current = Character.toLowerCase(current);
                }
                result[resultPosition++] = current;
            } else {
                upperCase = true;
            }
        }
        return String.valueOf(result, 0, resultPosition);
    }

    public static Class<?> loadClassFromCandidates(List<String> candidateClassNames) {
        for (String candidateClassName : candidateClassNames) {
            Class<?> result = BlacklistClassloading.forName(candidateClassName);
            log.fatal("try: " + candidateClassName);
            if (result != null) {
                log.fatal("returning for: " + candidateClassName);
                return result;
            }
        }
        return null;
    }
}
