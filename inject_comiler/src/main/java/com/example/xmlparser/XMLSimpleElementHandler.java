package com.example.xmlparser;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

/**
 * Created by liangjianhua on 2018/1/8.
 */

public class XMLSimpleElementHandler extends XMLElementHandler {


    TypeSpec.Builder superClassBuilder ;
        TypeSpec.Builder colorClass ;
    String className ;

    public XMLSimpleElementHandler(String type, boolean isRetrict, TypeSpec.Builder superClassBuilder, String className) {
        super(type, isRetrict, true);
        this.className = className ;
        this.superClassBuilder = superClassBuilder;
    }

    @Override
        public void startDocument() {
            colorClass = TypeSpec.classBuilder( className );
            colorClass.addModifiers( Modifier.PUBLIC, Modifier.STATIC );
        }

        @Override
        public void endDoucument() {
            superClassBuilder.addType( colorClass.build() );
        }

        @Override
        public void doHandler(String attrKey , String attrValue, String text) {
            System.out.println("XMLSimpleElementHandler  qQname: " + attrKey + ",aValue: " + attrValue + ",text " + text);
            if (text != null && text.length() > 0) {
//                System.out.println("endElement  qQname: " + attrKey + ",aValue: " + attrValue);
                ClassName String = ClassName.bestGuess("java.lang.String");
                FieldSpec.Builder fieldSpec = FieldSpec.builder(String, attrValue, Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC);
                fieldSpec.initializer("$S", text);
                colorClass.addField(fieldSpec.build());
                setOK();
            }
        }
}
