package org.prism.compiler.utils;

/**
 * Created by fushenghua on 2017/1/15.
 */


public class StringUtils {

    public static boolean isEmpty(final CharSequence cs){
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(final CharSequence cs){
        return !isEmpty(cs);
    }

}
