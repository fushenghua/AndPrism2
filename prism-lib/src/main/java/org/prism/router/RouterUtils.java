package org.prism.router;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import android.util.Log;

import org.prism.annotation.RouterUri;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by fushenghua on 2017/1/14.
 */

public class RouterUtils {

    public static ResolveInfo queryIntentActivities(Context context, Intent intent) {
        if (context == null || intent == null)
            return null;
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfoList == null || resolveInfoList.size() == 0)
            return null;
        int size = resolveInfoList.size();
        if (size == 1)
            return resolveInfoList.get(0);
        String appPackageName = context.getApplicationContext().getPackageName();
        for (int i = 0; i < size; i++) {
            ResolveInfo resolveInfo = resolveInfoList.get(i);
            String activityName = resolveInfo.activityInfo.name;
            if (TextUtils.isEmpty(activityName))
                continue;
            if (activityName.startsWith(appPackageName)) {
                return resolveInfo;
            }

            if (resolveInfo.activityInfo != null) {
                String activityPackageName = resolveInfo.activityInfo.packageName;
                if (TextUtils.isEmpty(activityPackageName)) {
                    continue;
                }

                if (TextUtils.equals(appPackageName, activityPackageName)) {
                    return resolveInfo;
                }
            }

        }

        return resolveInfoList.get(0);
    }

    private static <T> T create(Class<T> aClass) {
        return (T) Proxy.newProxyInstance(aClass.getClassLoader(), new Class<?>[]{aClass},
                new InvocationHandler() {

                    @Override
                    public Object invoke(Object proxy, Method method, Object... args) throws Throwable {
                        StringBuilder stringBuilder = new StringBuilder();
                        RouterUri reqUrl = method.getAnnotation(RouterUri.class);
                        Log.e(TAG, "IReqApi---reqUrl->" + reqUrl.routerUri());
                        stringBuilder.append(reqUrl.routerUri());
                        //Type[] parameterTypes = method.getGenericParameterTypes();//获取注解参数类型
                        Annotation[][] parameterAnnotationsArray = method.getParameterAnnotations();//拿到参数注解
                        //Annotation[] annotation = method.getDeclaredAnnotations();
                        int pos = 0;
                        for (int i = 0; i < parameterAnnotationsArray.length; i++) {
                            Annotation[] annotations = parameterAnnotationsArray[i];
                            if (annotations != null && annotations.length != 0) {
                                if (pos == 0) {
                                    stringBuilder.append("?");
                                } else {
                                    stringBuilder.append("&");
                                }
//                                pos++;
//                                RouterParam reqParam = (RouterParam) annotations[0];
//                                stringBuilder.append(reqParam.value());
//                                stringBuilder.append("=");
//                                stringBuilder.append(args[i]);
//                                Log.e(TAG, "reqParam---reqParam->" + reqParam.value() + "=" + args[i]);
                            }
                        }
                        //下面就可以执行相应的跳转操作
                        return null;
                    }
                });
    }

}
