package com.example.xmlparser;

import com.example.comlier.RandomIDHolder;
import com.example.comlier.utils.IDUtils;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

/**
 * Created by liangjianhua on 2018/1/3.
 */

public class XMLIDElementHandler extends XMLElementHandler {

    private TypeSpec.Builder idClass;

    private List<FieldSpec> fieldSpecList = new ArrayList<FieldSpec>();

    private RandomIDHolder idHolder ;

    public XMLIDElementHandler(String type, boolean isRetrict, TypeSpec.Builder idClass, RandomIDHolder idHolder) {
        super(type, isRetrict, false);
        this.idClass = idClass ;
        this.idHolder = idHolder ;
    }

    @Override
    public void startDocument() {

    }

    @Override
    public void endDoucument() {


    }

    @Override
    public void doHandler(String attrName, String attrValue, String text) {
        if( attrName.startsWith("android:id") ) {
            String idStr = attrValue.substring("@+id/".length());
            if( !idHolder.isIDNameOccupied(idStr) ) {
                System.out.println("XMLIDElementHandler attrName " + attrName + ", attrvalue: " + attrValue + ",text: " + text);
                FieldSpec.Builder fieldSpecBuilder = FieldSpec.builder(TypeName.INT, idStr, Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC);
                fieldSpecBuilder.initializer("$L", "0x" + Integer.toHexString( IDUtils.generateViewId() ));
                FieldSpec fieldSpec = fieldSpecBuilder.build();
                fieldSpecList.add( fieldSpec );
            }
        }
    }


    public List<FieldSpec> getFieldSpecList() {
        return fieldSpecList;
    }
}
