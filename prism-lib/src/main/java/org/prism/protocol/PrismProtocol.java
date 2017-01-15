package org.prism.protocol;

import android.text.TextUtils;

import org.prism.ClassGenerater;
import org.prism.annotation.Caller;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by fushenghua on 2017/1/15.
 */

public class PrismProtocol {

    public static <T> T create(Class<T> stub) {
        InvocationHandler handler = null;
        try {
            handler = findHandler(stub);
        } catch (Exception e) {
            throw new RuntimeException("error! findHandler!");
        }
        T result = (T) Proxy.newProxyInstance(stub.getClassLoader(), new Class[]{stub}, handler);
        return result;
    }

    private static <T> InvocationHandler findHandler(Class<T> stubClazz) throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException, InstantiationException {
        Caller caller = stubClazz.getAnnotation(Caller.class);

        if (!TextUtils.isEmpty(caller.value())) {
            Class protocolStubClazz = Class.forName(ClassGenerater.getClassNameForPackageName(caller.value()));
            String realClassName = ClassGenerater.getValueFromClass(protocolStubClazz);
            final Class realClazz = Class.forName(realClassName);

            Object realInstant = null;
            if (realInstant == null) {
                realInstant = createDefaultRealInstant(realClazz);//创建无参的实例（默认的方式）
            }

            final Object finalInstant = realInstant;
            InvocationHandler handler = new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    Class[] realClazzParamTypes = method.getParameterTypes();
//                Annotation[][] parameterAnnotations = method.getParameterAnnotations();
//                for (int i = 0; i < parameterAnnotations.length; i++) {
//                    Annotation[] annos = parameterAnnotations[i];
//                    if (annos != null && annos.length > 0) {
//                        if (annos[0] instanceof CallbackParam) {
//                            CallbackParam callbackParamAnno = (CallbackParam) annos[0];
//                            String communicationCallbackClassName = DataClassCreator.getCommunicationCallbackClassName(callbackParamAnno.value());
//                            ClassGenerater<?> communicationCallbackClazz = ClassGenerater.forName(communicationCallbackClassName);
//                            realClazzParamTypes[i] = communicationCallbackClazz;
//                            //这里可以使用缓存，来提升性能
//                            Object callbackProxyInstant = createCallbackProxyInstant(communicationCallbackClazz, args[i]);
//                            args[i] = callbackProxyInstant;
//                        }
//                    }
//                }
                    Method realMethod = realClazz.getDeclaredMethod(method.getName(), realClazzParamTypes);
                    realMethod.setAccessible(true);
                    return realMethod.invoke(finalInstant, args);
                }
            };
            return handler;
        }


        return null;
    }

    /**
     * 创建无参的构造函数
     *
     * @param realClassName
     * @return
     */
    private static Object createDefaultRealInstant(Class realClassName) {
        try {
            return realClassName.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
