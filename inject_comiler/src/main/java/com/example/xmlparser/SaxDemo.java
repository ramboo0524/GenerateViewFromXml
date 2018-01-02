package com.example.xmlparser;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeSpec;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by liangjianhua on 2017/12/27.
 */
public class SaxDemo implements XmlDocument {

    TypeSpec.Builder injectClass ;
    Element colorElement;

    public SaxDemo(TypeSpec.Builder injectClass, Element element) {
        this.injectClass = injectClass ;
        this.colorElement = element ;
    }

    @Override
    public void createXml(String fileName) {
        System.out.println("<<" + fileName + ">>");
    }

    @Override
    public void parserXml(String filePath) {

        SAXParserFactory saxfac = SAXParserFactory.newInstance();
        try {

            SAXParser saxparser = saxfac.newSAXParser();
            File file = new File( filePath );
            InputStream is = new FileInputStream(file);
            saxparser.parse(is, new MySAXHandler(injectClass, colorElement));
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
    TypeSpec.Builder injectClass ;
    TypeSpec.Builder colorClass ;
    Element colorElement;

    public MySAXHandler(TypeSpec.Builder injectClass, Element element){
        this.injectClass = injectClass ;
        this.colorElement = element;
        colorClass = TypeSpec.classBuilder( colorElement.getSimpleName().toString() );
        colorClass.addModifiers( Modifier.PUBLIC, Modifier.STATIC );
    }

    @Override
    public void startDocument() throws SAXException {
        System.out.println("start print document");
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println("print document finished");

        injectClass.addType( colorClass.build() );
    }

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        System.out.println("startElement uri: " + uri + ",localName: " + localName + ",qName: " + qName);
        if( !qName.equals( "color") ){
            return ;
        }
        if (attributes.getLength() > 0) {
            this.attributes = attributes;
            this.hasAttribute = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if( !qName.equals( "color") ){
            return ;
        }
        if (hasAttribute && (attributes != null) && text != null && text.length() > 0) {
            for (int i = 0; i < attributes.getLength(); i++) {
                System.out.println("endElement: " + i + " ,qQname: " + attributes.getQName(i) + ",aValue: " + attributes.getValue(i));
                ClassName String = ClassName.bestGuess("java.lang.String");
                FieldSpec.Builder fieldSpec = FieldSpec.builder(String, attributes.getValue(i), Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC );
                fieldSpec.initializer("$S", text);
                colorClass.addField(fieldSpec.build());
            }
        }
    }
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        text = new String(ch, start, length) ;
        System.out.println("characters: " + new String(ch, start, length));
    }
}

