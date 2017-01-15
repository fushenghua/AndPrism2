package org.prism.router;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import static org.prism.router.RouterUtils.queryIntentActivities;


/**
 * Created by fushenghua on 2017/1/14.
 */

public class ActivityRouter implements IRouter {

    private static final String ACTIVITY_SCHEME = "activity://";
    private static ActivityRouter instance;

    private ActivityRouter() {
    }

    public static ActivityRouter getInstance() {
        if (instance == null) {
            synchronized (ActivityRouter.class) {
                if (instance == null) {
                    instance = new ActivityRouter();
                }
            }
        }
        return instance;
    }


    @Override
    public void start(RouterContext routerContext) {
        Context context = routerContext.getContext();
        Intent intent = routerContext.getIntent();

        if (intent == null) {
            intent = new Intent();
        }

        String uri = routerContext.getUri();
        if (null == uri)
            return;

        intent.setData(Uri.parse(ACTIVITY_SCHEME + uri));
        intent.setAction(Intent.ACTION_VIEW);
        intent.setAction(Intent.ACTION_DEFAULT);

        start(context, intent);
    }

    public void start(Context context, Intent intent) {
        ResolveInfo targetActivity = queryIntentActivities(context, intent);
        if (targetActivity == null) return;

        String packageName = targetActivity.activityInfo.packageName;
        String className = targetActivity.activityInfo.name;
        intent.setClassName(packageName, className);

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }
}
