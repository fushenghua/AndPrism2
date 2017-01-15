package org.prism;

import java.lang.reflect.Field;

/**
 * Created by fushenghua on 2017/1/15.
 */

public class ClassGenerater {


    public static final String PACKAGE_OF_GENERATE_FILE = "org.prism.protocol.data";

    public static String getClassNameForPackageName(String simpleName) {
        return PACKAGE_OF_GENERATE_FILE + "." + simpleName;
    }

    public static String getValueFromClass(Class callerClazz) throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        Field valueField = callerClazz.getDeclaredField("value");
        valueField.setAccessible(true);
        return (String) valueField.get(callerClazz.newInstance());
    }
}
