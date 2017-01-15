package org.prism.compiler;

import com.google.auto.service.AutoService;

import org.prism.annotation.Caller;
import org.prism.annotation.Protocol;
import org.prism.compiler.utils.Logger;
import org.prism.compiler.utils.ProcessGenerater;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by fushenghua on 2017/1/15.
 */

@AutoService(Processor.class)
public class ProtocolProcessor extends AbstractProcessor {


    private Filer mFiler;//文件相关的辅助类
    private Elements mElementUtils;//元素相关的辅助类
    private Messager mMessager;//日志相关的辅助类
    private Logger mLogger;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        mLogger = new Logger(mMessager);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(Protocol.class.getCanonicalName());
        types.add(Caller.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Map<String, ElementHolder> holderMap = ProcessGenerater.collectClassInfo(roundEnv, Protocol.class, ElementKind.CLASS);
        System.out.println("-------------__-----------------"+holderMap.size());

        for (Map.Entry<String, ElementHolder> entry : holderMap.entrySet()) {
            ClassGenerater.generateProtocolCode(mElementUtils, mFiler, entry.getValue(), mLogger);
        }
        System.out.println("------------------------------");


        return true;
    }
}
