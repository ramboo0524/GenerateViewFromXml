package com.example.xmlparser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by liangjianhua on 2017/12/27.
 */
public class SaxDemo implements XmlDocument {

    private XMLElementHandler elementHandler ;
    public SaxDemo(XMLElementHandler elementHandler) {
        this.elementHandler = elementHandler;
    }

    @Override
    public void createXml(String fileName) {
        System.out.println("<<" + fileName + ">>");
    }

    @Override
    public void parserXml(String filePath) {

        SAXParserFactory saxFac = SAXParserFactory.newInstance();
        try {

            SAXParser saxparser = saxFac.newSAXParser();
            File file = new File( filePath );
            InputStream is = new FileInputStream(file);
            saxparser.parse(is, new MySAXHandler(elementHandler));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

class MySAXHandler extends DefaultHandler {
    boolean hasAttribute = false;
    Attributes attributes = null;
    private String text ;
    XMLElementHandler xmlElementHandler ;

    public MySAXHandler(XMLElementHandler elementHandler){
        xmlElementHandler = elementHandler ;
    }

    @Override
    public void startDocument() throws SAXException {
        System.out.println("start print document");
        xmlElementHandler.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println("print document finished");
        xmlElementHandler.endDoucument();

    }

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        System.out.println("startElement uri: " + uri + ",qName: " + qName + ",isResType: " + xmlElementHandler.isResType());
        if( xmlElementHandler.isRetrict() && !qName.equals( xmlElementHandler.getType() ) ){
            return ;
        }
        if( xmlElementHandler.isResType() ){
            if (attributes.getLength() > 0) {
                this.attributes = attributes;
                this.hasAttribute = true;
            }
        }else{
            if ( attributes != null ) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    System.out.println("startElement uri: " + uri + ",attributes.getQName( i ): " + attributes.getQName( i ) + ",attributes.getValue(i): " + attributes.getValue(i));
                    xmlElementHandler.doHandler(attributes.getQName( i ), attributes.getValue(i), text);
                }
            }
        }



    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if( xmlElementHandler.isResType() ) {
            if (xmlElementHandler.isRetrict() && !qName.equals(xmlElementHandler.getType())) {
                System.out.println("endElement isRetrict : " + qName);
                return;
            }

            if (hasAttribute && attributes != null) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    xmlElementHandler.doHandler(attributes.getQName(i), attributes.getValue(i), text);
                }
            }
        }
    }
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        if( xmlElementHandler.isResType() ) {
            text = new String(ch, start, length).trim();
        }
    }


}

