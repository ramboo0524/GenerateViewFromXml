package com.example.comlier;


import com.example.ResourcesColor;
import com.example.xmlparser.SaxDemo;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.util.List;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;


/**
 * @author Created by spc on 17/6/6.
 */
public class AnnotatedClass {
    //activity  //fragmemt
    private TypeElement mTypeElement;
    private Elements mElements;
    //日志打印
    private Messager mMessager;


    public AnnotatedClass(TypeElement typeElement, Elements elements, Messager messager) {
        mTypeElement = typeElement;
        mElements = elements;
        this.mMessager = messager;
    }


    public JavaFile generateActivityFile() throws ClassNotFoundException {

        TypeSpec.Builder injectClass = TypeSpec.classBuilder( "RR" );
        injectClass.addModifiers(Modifier.PUBLIC) ;

        List<? extends Element> enclosedElements = mTypeElement.getEnclosedElements();
        for( int i = 0 ; i < enclosedElements.size() ; i ++ )
        {
            Element enclosedElement = enclosedElements.get(i);
            log( "Element enclosedElement.getSimpleName() : " + enclosedElement.getSimpleName() ) ;
            if( enclosedElement.getKind() == ElementKind.CLASS ){
                ResourcesColor annotation = enclosedElement.getAnnotation(ResourcesColor.class);
                if( annotation != null ){
                    SaxDemo demo = new SaxDemo( injectClass , enclosedElement );
                    demo.parserXml( "./Demo/res/values/color.xml" );
                }
            }
        }

        // build inject method
//        MethodSpec.Builder injectMethod = MethodSpec.methodBuilder(TypeUtil.METHOD_NAME)
//                .addModifiers(Modifier.PUBLIC)
//                .addParameter(TypeName.get(mTypeElement.asType()), "activity", Modifier.FINAL);
//        injectMethod.addStatement("android.widget.Toast.makeText" +
//                "(activity, $S,android.widget.Toast.LENGTH_SHORT).show();", "from build");
//        //generaClass

        String packgeName = mElements.getPackageOf(mTypeElement).getQualifiedName().toString();
        return JavaFile.builder(packgeName, injectClass.build()).build();
//        return  null;
    }

    private void error(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }

    private void log(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(msg, args));
    }
}
