package com.example.comlier;

import com.example.ResourcesColor;
import com.example.ResourcesDimen;
import com.example.ResourcesID;
import com.example.ResourcesR;
import com.example.ResourcesString;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;


/**
 * 这个注解是谷歌提供了，快速实现注解处理器，会帮你生成配置文件啥的 。直接用就好
 * @author by spc on 17/6/6.
 */
@AutoService(Processor.class)
public class ActivityInjectProcesser extends AbstractProcessor {
    /**
     * *文件相关的辅助类*/
    private Filer mFiler;
    /**
     * 元素相关的辅助类  许多元素
     */
    private Elements mElementUtils;
    /**
     *  日志相关的辅助类
     */
    private Messager mMessager;

    private Map<String, AnnotatedClass> mAnnotatedClassMap;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        mAnnotatedClassMap = new TreeMap<>();
    }

    /**
     *
     * @param annotations
     * @param roundEnv
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        mAnnotatedClassMap.clear();

        try {
            processActivityCheck(roundEnv);
        } catch (Exception e) {
            e.printStackTrace();
            error(e.getMessage());
        }

        for (AnnotatedClass annotatedClass : mAnnotatedClassMap.values()) {
            try {
                JavaFile javaFile = annotatedClass.generateActivityFile();
                if( javaFile != null ) {
                    javaFile.writeTo(mFiler);
                }
            } catch (Exception e) {
                error("Generate file failed, reason: %s", e.getMessage());
            }
        }
        return true;
    }


    private void processActivityCheck(RoundEnvironment roundEnv) throws IllegalArgumentException, ClassNotFoundException {
        //check ruleslass forName(String className
        for (Element element : roundEnv.getElementsAnnotatedWith(ResourcesR.class)) {
            if (element.getKind() == ElementKind.CLASS) {
                getAnnotatedClass(roundEnv, element);
            } else {
                error("ActivityInject only can use  in ElementKind.CLASS");
            }
        }
    }

    private AnnotatedClass getAnnotatedClass(RoundEnvironment roundEnv,Element element) throws ClassNotFoundException {
        // tipe . can not use chines  so  ....
        // get TypeElement  element is class's --->class  TypeElement typeElement = (TypeElement) element
        //  get TypeElement  element is method's ---> TypeElement typeElement = (TypeElement) element.getEnclosingElement();
        TypeElement typeElement = (TypeElement) element;
        ResourcesR annotation = typeElement.getAnnotation(ResourcesR.class);
        String fullName = typeElement.getQualifiedName().toString();
        log("method getAnnotatedClass fullName: " + fullName + " ,annotation.value(): " + annotation.value() ) ;
        AnnotatedClass annotatedClass = mAnnotatedClassMap.get(fullName);
        if (annotatedClass == null) {
            annotatedClass = new AnnotatedClass(typeElement, mElementUtils, mMessager);
            mAnnotatedClassMap.put(fullName, annotatedClass);
        }

        return annotatedClass;
    }


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(ResourcesR.class.getName());
        types.add(ResourcesColor.class.getName());
        types.add(ResourcesString.class.getName());
        types.add(ResourcesDimen.class.getName());
        types.add(ResourcesID.class.getName());
        return types;
    }

    private void error(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }

    private void log(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(msg, args));
    }
}
