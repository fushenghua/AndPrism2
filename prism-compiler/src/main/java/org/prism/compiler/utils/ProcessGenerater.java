package org.prism.compiler.utils;


import org.prism.annotation.Caller;
import org.prism.annotation.Protocol;
import org.prism.annotation.RouterUri;
import org.prism.compiler.ElementHolder;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

/**
 * Created by fushenghua on 2017/1/15.
 */


public class ProcessGenerater {

    public static Map<String, ElementHolder> collectClassInfo(RoundEnvironment roundEnv, Class<? extends Annotation> clazz, ElementKind kind) {
        Map<String, ElementHolder> map = new HashMap<>();
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(clazz);
        for (Element element : elements) {
            if(element.getKind() != kind){
//                throw new AnnotationTargetUseException(element.getSimpleName() + "'s annotation must be on a " + kind.name());
//                logger.error("A interceptor verify failed, its " + element.asType());
                continue;
            }

            try {
                TypeElement typeElement = (TypeElement) element;
                Annotation annotation = typeElement.getAnnotation(clazz);
                Method method = clazz.getDeclaredMethod("value");
                method.setAccessible(true);
                Object value = method.invoke(annotation);
                String clazzName = typeElement.getQualifiedName().toString();
                String simpleName = typeElement.getSimpleName().toString();
                if (Caller.class.equals(clazz) || RouterUri.class.equals(clazz)) {
//                    if (!(value instanceof String) || StringUtils.isEmpty((String)value)) {
//                        throw new AnnotationValueNullException(element.getSimpleName() + "'s " + clazz.getSimpleName() + " annotation's value is null!!");
//                    }
                    map.put((String) value, new ElementHolder(typeElement, (String) value, clazzName, simpleName));
                } else if (Protocol.class.equals(clazz)) {
//                    if(value == null || !(value instanceof  String[])){
//                        throw new AnnotationValueNullException(element.getSimpleName() + "'s " + clazz.getSimpleName() + " annotation's value is null!!");
//                    }
//                    String vals = (String) value;
//                    for (int i = 0; i < vals.length; i++) {
//                        if(StringUtils.isEmpty(vals[i])){
////                            throw new AnnotationValueNullException(element.getSimpleName() + "'s " + clazz.getSimpleName() +
////                                " annotation's number " + i + " value is null.");
//                            continue;
//                        }
//                        //过滤掉重复的值
//                        if (!isRepeat(vals[i], i, vals)) {
//                            map.put(vals[i], new ElementHolder(typeElement, vals[i], clazzName, simpleName));
//                        }
//                    }
                    map.put((String) value,new ElementHolder(typeElement, (String) value, clazzName, simpleName));
                }

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return map;
    }

    private static boolean isRepeat(String val, int currentPos, String[] vals) {
        for (int i = 0; i < currentPos; i++) {
            if (vals[i] == val) {
                return true;
            }
        }
        return false;
    }
}
