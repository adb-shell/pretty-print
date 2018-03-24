package com.karthik.pretty_compiler;

import javax.lang.model.element.Element;

/**
 * Created by karthikrk on 14/03/18.
 */

public class ProcessingException extends Exception{
    private Element element;

    public ProcessingException(Element element,String message){
        super(message);
        this.element = element;
    }

    public Element getElement() {
        return element;
    }
}
