package com.karthik.pretty_compiler;


import com.karthik.pretty_annotation.Pretty;
import com.squareup.javapoet.MethodSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Created by karthikrk on 14/03/18.
 */

public class PrettyCompiler extends AbstractProcessor{

    private Messager messager;
    private Elements elementUtils;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
        elementUtils = processingEnvironment.getElementUtils();
        filer = processingEnvironment.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<String>();
        annotations.add(Pretty.class.getCanonicalName());
        return annotations;
    }


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        ArrayList<MethodSpec> methods = new ArrayList<>();
        TypeElement typeElement = null;
        try{
            for (Element annotatedElement:roundEnvironment.getElementsAnnotatedWith(Pretty.class)) {
                typeElement = (TypeElement) annotatedElement;
                methods.add(CodeGenerator.generatePrettyActivityFragmentMethods(annotatedElement));
            }
            if(typeElement!=null) {
                CodeGenerator.generatePrettyPrintFile(methods,typeElement,elementUtils,filer);
            }
        }catch (ProcessingException exception){
            sendError(exception.getElement(),exception.getMessage());
        }catch (IOException ex){
            sendError(null,ex.getMessage());
        }
        return false;
    }


    private void sendError(Element element,String msg){
        messager.printMessage(Diagnostic.Kind.ERROR,msg,element);
    }
}
