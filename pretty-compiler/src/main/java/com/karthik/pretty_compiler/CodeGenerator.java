package com.karthik.pretty_compiler;



import com.karthik.pretty_annotation.Pretty;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by karthikrk on 17/03/18.
 */

public class CodeGenerator {

    private static final String PRINTERSUFFIX = "printExtras";
    private static final String EXTRAHEADER = "ClassName";
    private static final String PRINTERCLASSNAME = "PrettyPrinterExtras";
    private static final String PRINTERMETHODNAME = "printExtras";


    private static final ClassName BUNDLE = ClassName.get("android.os",
            "Bundle");
    private static final ClassName FLIPTABLE = ClassName.get("com.jakewharton.fliptables",
            "FlipTable");



    public static MethodSpec generatePrettyActivityFragmentMethods(Element annotatedElement) throws ProcessingException{

        if (annotatedElement.getKind() != ElementKind.CLASS) {
            throw new ProcessingException(annotatedElement,String.format("Only Class can be annotated with @%s",
                    Pretty.class.getSimpleName()));
        }

        Pretty prettyElement = annotatedElement.getAnnotation(Pretty.class);

        MethodSpec method = MethodSpec.methodBuilder(prettyElement.headerName()+PRINTERSUFFIX)
                .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
                .addParameter(BUNDLE,"bundle")
                .addStatement("$T[] headers = {$S,$S}",String.class,EXTRAHEADER,prettyElement.headerName())
                .addCode("printExtras(bundle,headers);\n")
                .returns(void.class)
                .build();
        return method;
    }

    public static void generatePrettyPrintFile(ArrayList<MethodSpec> methods,
                                               TypeElement typeElement,
                                               Elements elementUtils,
                                               Filer filer) throws IOException {

        MethodSpec printer = MethodSpec.methodBuilder(PRINTERMETHODNAME)
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .addParameter(BUNDLE, "bundle")
                .addParameter(String[].class, "headers")
                .addCode("" + "if(bundle==null || bundle.isEmpty()){\n"
                        + "\treturn;\n"
                        + "}\n")
                .addStatement("$T[][] valueKeys = new $T[bundle.keySet().size()][2]", String.class, String.class)
                .addStatement("int row=0,col=0")
                .beginControlFlow("for(String key:bundle.keySet())")
                .addCode("col=0;\n valueKeys[row][col] = key;\n ++col;\n " +
                        "valueKeys[row][col] = bundle.get(key).toString();\n ++row;\n")
                .endControlFlow()
                .addStatement("$T.out.println($T.of(headers,valueKeys))",System.class,FLIPTABLE)
                .returns(void.class)
                .build();


        TypeSpec prettyPrinterClass = TypeSpec
                .classBuilder(PRINTERCLASSNAME)
                .addJavadoc("Generated code to print extras,from Activity/Fragment\n")
                .addMethods(methods)
                .addMethod(printer)
                .build();

        JavaFile javaFile = JavaFile
                .builder(elementUtils.getPackageOf(typeElement)
                        .getQualifiedName().toString(), prettyPrinterClass)
                .build();

        javaFile.writeTo(filer);
    }
}
