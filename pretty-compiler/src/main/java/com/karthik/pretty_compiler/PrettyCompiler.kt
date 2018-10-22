package com.karthik.pretty_compiler

import com.karthik.pretty_annotation.Pretty
import com.squareup.javapoet.MethodSpec
import java.io.IOException
import java.util.ArrayList
import java.util.LinkedHashSet
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic

class PrettyCompiler: AbstractProcessor() {

    private var messenger:Messager?=null
    private var element:Elements?=null
    private var filer: Filer?=null

    override fun init(processingEnvironment: ProcessingEnvironment?) {
        super.init(processingEnvironment)
        element = processingEnvironment?.elementUtils
        filer = processingEnvironment?.filer
        messenger = processingEnvironment?.messager
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        val annotations = LinkedHashSet<String>()
        annotations.add(Pretty::class.java.canonicalName)
        return annotations
    }

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()


    override fun process(set:MutableSet<out TypeElement>?, roundEnvironment: RoundEnvironment?):Boolean {
        if(roundEnvironment==null || roundEnvironment.getElementsAnnotatedWith(Pretty::class.java)?.isEmpty()!!){
            return false
        }

        val methods = ArrayList<MethodSpec>()
        var typeElement: TypeElement? = null

        try {
            for (annotatedElement in roundEnvironment.getElementsAnnotatedWith(Pretty::class.java)) {
                typeElement = annotatedElement as TypeElement
                methods.add(CodeGenerator.generatePrettyPrintMethods(annotatedElement))
            }
            if (typeElement != null) {
                CodeGenerator.generatePrettyPrintFile(methods, typeElement, element!!, filer!!)
            }
        } catch (exception: ProcessingException) {
            sendError(exception.element, exception.message)
        } catch (ex: IOException) {
            sendError(null, ex.message)
        }


        return false
    }

    private fun sendError(element: Element?, msg: String?) =
            messenger?.printMessage(Diagnostic.Kind.ERROR, msg, element)
}