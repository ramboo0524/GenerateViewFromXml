package com.example.comlier;


import com.example.ResourcesColor;
import com.example.ResourcesID;
import com.example.ResourcesR;
import com.example.xmlparser.SaxDemo;
import com.example.xmlparser.XMLElementHandler;
import com.example.xmlparser.XMLIDElementHandler;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
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

    private TypeElement mTypeElement;
    private Elements mElements;
    /**
     * 日志打印
     */
    private Messager mMessager;
    private String rootFloder ;
    private RandomIDHolder idHolder ;


    public AnnotatedClass(TypeElement typeElement, Elements elements, Messager messager) {
        mTypeElement = typeElement;
        ResourcesR resourcesR =  mTypeElement.getAnnotation(ResourcesR.class);
        if( resourcesR != null ){
            rootFloder = resourcesR.value() ;
        }
        mElements = elements;
        this.mMessager = messager;
        idHolder = new RandomIDHolder();
    }


    public JavaFile generateActivityFile() throws ClassNotFoundException {

        final TypeSpec.Builder injectClass = TypeSpec.classBuilder( "RR" );
        injectClass.addModifiers(Modifier.PUBLIC) ;

        List<? extends Element> enclosedElements = mTypeElement.getEnclosedElements();
        for( int i = 0 ; i < enclosedElements.size() ; i ++ )
        {
            final  Element enclosedElement = enclosedElements.get(i);
            log( "Element enclosedElement.getSimpleName() : " + enclosedElement.getSimpleName()  ) ;
            if( enclosedElement.getKind() == ElementKind.CLASS ){

                ResourcesColor colorAnnotation = enclosedElement.getAnnotation(ResourcesColor.class);
                log( "Element annotation : " + colorAnnotation  ) ;
                if( colorAnnotation != null ){
                    XMLElementHandler colorHandler = new XMLElementHandler( "color" , true ) {

                        TypeSpec.Builder colorClass ;

                        @Override
                        public void startDocument() {
                            colorClass = TypeSpec.classBuilder( enclosedElement.getSimpleName().toString() );
                            colorClass.addModifiers( Modifier.PUBLIC, Modifier.STATIC );
                        }

                        @Override
                        public void endDoucument() {
                            injectClass.addType( colorClass.build() );
                        }

                        @Override
                        public void doHandler(String attrKey , String attrValue, String text) {

                            if (text != null && text.length() > 0) {
                                    System.out.println("endElement  qQname: " + attrKey + ",aValue: " + attrValue);
                                    ClassName String = ClassName.bestGuess("java.lang.String");
                                    FieldSpec.Builder fieldSpec = FieldSpec.builder(String, attrValue, Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC);
                                    fieldSpec.initializer("$S", text);
                                    colorClass.addField(fieldSpec.build());
                                    setOK();
                                }
                            }
                    };

                    SaxDemo demo = new SaxDemo( colorHandler );
                    demo.parserXml( "./Demo/" + colorAnnotation.value() );
                    System.out.println("i: " + i + "./Demo/" + colorAnnotation.value());
                    continue;
                }
                ResourcesID idAnnotation = enclosedElement.getAnnotation(ResourcesID.class);
                if( idAnnotation != null ){
                    TypeSpec.Builder idClass = TypeSpec.classBuilder( enclosedElement.getSimpleName().toString() );
                    idClass.addModifiers( Modifier.PUBLIC, Modifier.STATIC );
                    XMLIDElementHandler idHandler = new XMLIDElementHandler("", false, idClass, idHolder);
                    File file = new File( "./Demo/" + idAnnotation.value() );
                     log( "file.getPath() : " + file.getPath() ) ;
                    if( file.exists() && file.isDirectory() ){
                        File[] files = file.listFiles();
                        SaxDemo demo = new SaxDemo( idHandler );
                        for(int j = 0 ; j < files.length ; j ++ ){
                            File aFile = files[j] ;
                            log( "aFile : " + aFile.getPath() ) ;
                            demo.parserXml( aFile.getPath() );
                        }
                    }
                    idClass.addFields( idHandler.getFieldSpecList() );
                    injectClass.addType( idClass.build() );
                    continue;
                }

            }
        }

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