package com.karthik.pretty_compiler

import com.karthik.pretty_annotation.Pretty
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import javax.annotation.processing.Filer
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

object CodeGenerator {

    fun generatePrettyPrintMethods(annotatedElement: Element):MethodSpec {
        if (annotatedElement.kind != ElementKind.CLASS) {
            throw ProcessingException(annotatedElement,
                    String.format("Only Class can be annotated with @%s",Pretty::class.java.simpleName))
        }

        val prettyElement = annotatedElement.getAnnotation(Pretty::class.java)

        return MethodSpec.methodBuilder(prettyElement.headerName + PRINTERSUFFIX)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(BUNDLE,"bundle")
                .addStatement("\$T[] headers = {\$S,\$S}",String::class.java,EXTRAHEADER,prettyElement.headerName)
                .addCode("printExtras(bundle,headers);\n")
                .build()
    }

    fun generatePrettyPrintFile(methods:ArrayList<MethodSpec>,typeElement: TypeElement,
                                element: Elements,filer:Filer){

        val prettyPrinterClass = generatePrettyPrintClass(methods,getBundleSize(),generateGenricPrintMethod())

        val javaFile = JavaFile
                .builder(element.getPackageOf(typeElement)
                        .qualifiedName.toString(), prettyPrinterClass)
                .build()

        javaFile.writeTo(filer)
    }

    private fun generatePrettyPrintClass(methods: ArrayList<MethodSpec>, bundleSize: MethodSpec, printer: MethodSpec): TypeSpec {
        return TypeSpec
                .classBuilder(PRINTERCLASSNAME)
                .addJavadoc("Generated code to print extras,from Activity/Fragment\n")
                .addMethods(methods)
                .addMethod(bundleSize)
                .addMethod(printer)
                .build()
    }

    private fun generateGenricPrintMethod(): MethodSpec {
        return MethodSpec.methodBuilder(PRINTERMETHODNAME)
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .addParameter(BUNDLE, "bundle")
                .addParameter(Array<String>::class.java, "headers")
                .addCode("" + "if(bundle==null || bundle.isEmpty()){\n"
                        + "\treturn;\n"
                        + "}\n")
                .addStatement("\$T[][] valueKeys = new \$T[bundle.keySet().size()+1][2]", String::class.java, String::class.java)
                .addStatement("int row=0,col=0")
                .beginControlFlow("for(String key:bundle.keySet())")
                .addCode("col=0;\n valueKeys[row][col] = key;\n ++col;\n " + "valueKeys[row][col] = bundle.get(key).toString();\n ++row;\n")
                .endControlFlow()
                .addCode("valueKeys[bundle.keySet().size()][0]=\$S;\n", SIZE)
                .addCode("valueKeys[bundle.keySet().size()][1]=getBundleSize(bundle);\n\n")
                .addCode("if(BuildConfig.DEBUG)\n")
                .addStatement("\$T.d(\$S,\$T.of(headers,valueKeys))", LOG, "pretty", FLIPTABLE)
                .returns(Void.TYPE)
                .build()
    }

    private fun getBundleSize(): MethodSpec {
        return MethodSpec.methodBuilder(PRINTBUNDLESIZE)
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .addParameter(BUNDLE, "bundle")
                .addCode("\$T parcel = \$T.obtain();\n"
                        + "byte[] bytes;\n" + "parcel.writeBundle(bundle);\n"
                        + "bytes = parcel.marshall();\n"
                        + "parcel.recycle();\n"
                        + "return \$T.valueOf(bytes.length);\n", PARCEL, PARCEL, String::class.java)
                .returns(String::class.java)
                .build()
    }
}