package org.prism.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import org.prism.compiler.utils.Constant;
import org.prism.compiler.utils.Logger;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by wangfei on 2016/12/7.
 */

public class ClassGenerater {


    private static final String ACTIVITY_CLASS_NAME = "android.app.Activity";
    private static final String ACTIVITY_ROUTER_INITALIZER_INTERFACE_CLASS_NAME = "com.xiaoxiao.qiaoba.interpreter.router.IActivityRouterInitalizer";


    public static String getClassNameForPackageName(String simpleName) {
        return Constant.PACKAGE_OF_GENERATE_FILE + "." + simpleName;
    }

    public static String getActvivityRouterInitalizerClassName() {
        return Constant.createRouterLinkPackageName + "." + Constant.createRouterLinkSimpleName;
    }

    public static void generateProtocolCode(Elements elementUtils, Filer filer, ElementHolder elementHolder, Logger mLogger) {
        TypeSpec classStub = TypeSpec.classBuilder(elementHolder.getValueName())
                .addModifiers(Modifier.PUBLIC)
                .addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).build())
                .addField(FieldSpec.builder(String.class, "value").addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .initializer("$S", elementHolder.getClazzName())
                        .build())
                .build();

        JavaFile javaFile = JavaFile.builder(Constant.PACKAGE_OF_GENERATE_FILE, classStub).build();
        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            mLogger.warning("Protocol : " + elementHolder.getValueName() + " class write to file fail");
            e.printStackTrace();
        }
    }

    public static void generateRouterLinkCode(Elements elementUtils, Filer filer, Collection<ElementHolder> elementHolders) {
        if (elementHolders.size() > 0) {
            TypeElement activityElement = elementUtils.getTypeElement(ACTIVITY_CLASS_NAME);
            TypeElement activityRouterInterfaceElement = elementUtils.getTypeElement(ACTIVITY_ROUTER_INITALIZER_INTERFACE_CLASS_NAME);

            //参数类型为Map<String, Class<? extend Acvtivity>>
            ParameterizedTypeName mapTypeName = ParameterizedTypeName.get(ClassName.get(Map.class),
                    ClassName.get(String.class), ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(ClassName.get(activityElement))));
            ParameterSpec routerMapParam = ParameterSpec.builder(mapTypeName, "routerMap").build();
            MethodSpec.Builder initRouterTableMethodBuilder = MethodSpec.methodBuilder("initRouterTable")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addParameter(routerMapParam);

            for (ElementHolder holder : elementHolders) {
                initRouterTableMethodBuilder.addStatement("routerMap.put($S, $T.class)", holder.getValueName(),
                        ClassName.get(holder.getTypeElement()));
            }
            TypeSpec.Builder routerLinkBuilder = TypeSpec.classBuilder(Constant.createRouterLinkSimpleName)
                    .addSuperinterface(ClassName.get(activityRouterInterfaceElement))
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(initRouterTableMethodBuilder.build());

            JavaFile javaFile = JavaFile.builder(Constant.createRouterLinkPackageName, routerLinkBuilder.build()).build();

            try {
                javaFile.writeTo(filer);
            } catch (IOException e) {
                System.out.println("router link write to file fail!!");
                e.printStackTrace();
            }
        } else {
            System.out.println("router link uri activity's size is 0");
        }
    }

    public static void generateCallbackImpCode(Elements elementUtils, Filer filer, ElementHolder elementHolder) {
        if (elementHolder == null) {
            return;
        }
        if (elementHolder.getClazzName() == null || "".equals(elementHolder.getClazzName())) {

            return;
        }
        TypeSpec.Builder callbackBuilder = TypeSpec.classBuilder(elementHolder.getValueName())
                .addModifiers(Modifier.PUBLIC)
                .addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).build())
                .addField(FieldSpec.builder(String.class, "value")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .initializer("$S", elementHolder.getClazzName())
                        .build());

        JavaFile javaFile = JavaFile.builder(Constant.CREATE_CALLBACK_OF_COMMUNICATION_PACKAGE_NAME, callbackBuilder.build()).build();
        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            System.out.println("Callback imp class write to file fail!!");
            e.printStackTrace();
        }
    }


    public static String getValueFromClass(Class callerClazz) throws NoSuchFieldException, IllegalAccessException, InstantiationException {
        Field valueField = callerClazz.getDeclaredField("value");
        valueField.setAccessible(true);
        return (String) valueField.get(callerClazz.newInstance());
    }

    public static String getCommunicationCallbackClassName(String simpleName) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, InstantiationException {
        Class stubClazz = Class.forName(Constant.CREATE_CALLBACK_OF_COMMUNICATION_PACKAGE_NAME + "." + simpleName);
        Field valueField = stubClazz.getDeclaredField("value");
        valueField.setAccessible(true);
        return (String) valueField.get(stubClazz.newInstance());
    }
}
