package com.example.xmlparser;

/**
 * Created by liangjianhua on 2018/1/3.
 */

public abstract class XMLElementHandler {

    public String type ;

    public boolean isOK ;

    public boolean isRetrict ;

    public XMLElementHandler( String type , boolean isRetrict){
        this.type = type ;
        this.isRetrict = isRetrict ;
    }

    public abstract void startDocument();

    public abstract void endDoucument();

    public abstract void doHandler(String attrName, String attrValue, String text);

    public String getType() {
        return type;
    }

    public void setOK() {
        isOK = true;
    }

    public boolean isOK() {
        return isOK;
    }

    public boolean isRetrict() {
        return isRetrict;
    }

}
