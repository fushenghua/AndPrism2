package org.prism.router;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/**
 * Created by fushenghua on 2017/1/14.
 */

public class ARouterUtils {

    public static void openActivity(Context context, String nUri) {
        if (null == context) return;
        if (TextUtils.isEmpty(nUri)) return;

        ActivityRouter.getInstance()
                .start(RouterContext.builder()
                        .setContext(context).
                                setUri(nUri));
    }

    public static void openActivity(Context context, Intent intent) {
        if (null == intent || null == context) return;
        ActivityRouter.getInstance()
                .start(RouterContext.builder()
                        .setContext(context).
                                setIntent(intent));
    }


    public static class ActivityConstants {
        public static String ACTIVITY_MOTIOR_ACTION = "activity://junk/motior";
        public static String ACTIVITY_JUNK_ACTION = "activity://junk/junk";
        public static String ACTIVITY_JUNKADVANCEED_ACTION = "activity://junk/junkadvanceed";
        public static String ACTIVITY_BOOST_ACTION = "activity://boost/boost";
        public static String ACTIVITY_QUICKBOOST_ACTION = "activity://boost/quickboost";
        public static String ACTIVITY_CPU_ACTION = "activity://cpu/cpu";
        public static String ACTIVITY_APPMGR_ACTION = "activity://appmgr/appmgr";
        public static String ACTIVITY_SETTING_ACTION = "activity://cleaner/setting";
    }
}
