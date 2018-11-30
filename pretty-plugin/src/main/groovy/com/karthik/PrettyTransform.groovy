package com.karthik


import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import org.gradle.api.GradleException
import org.gradle.api.logging.Logger
import com.google.common.collect.Lists
import com.google.common.collect.Sets
import com.google.common.base.Joiner
import com.google.common.base.Strings
import com.android.build.api.transform.*

class PrettyTransform extends Transform{

    def project;

    PrettyTransform(def p) {
        this.project = p
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {

        List<File> files = Lists.newArrayList()
        List<File> classpathFiles = Lists.newArrayList()
        Logger logger = project.logger

        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider()

        // clean
        outputProvider.deleteAll()

        //  Referenced Inputs to classpath
        for (TransformInput input : transformInvocation.getReferencedInputs()) {
            input.directoryInputs.each {
                classpathFiles.add(it.file)
            }

            input.jarInputs.each {
                classpathFiles.add(it.file)
            }
        }

        // Scope inputs
        for (TransformInput input : transformInvocation.getInputs()) {

            for (DirectoryInput folder : input.directoryInputs) {
                files.add(folder.file)
            }

            for (JarInput jar : input.jarInputs) {
                files.add(jar.file)
            }
        }


        // Evaluate class paths
        final String inpath = Joiner.on(File.pathSeparator).join(files)
        final String classpath = Joiner.on(File.pathSeparator).join(
                classpathFiles.collect { it.absolutePath })
        final String bootpath = Joiner.on(File.pathSeparator).join(project.android.bootClasspath)
        final File output = outputProvider.getContentLocation("main", outputTypes,
                Sets.immutableEnumSet(QualifiedContent.Scope.PROJECT), Format.DIRECTORY)

        // Weaving args
        def args = [
                "-showWeaveInfo",
                "-1.5",
                "-inpath", inpath,
                "-d", output.absolutePath,
                "-bootclasspath", bootpath]


        // Append classpath argument if any
        if (!Strings.isNullOrEmpty(classpath)) {
            args << '-classpath'
            args << classpath

            args << '-aspectpath'
            args << classpath
        }

        // run aspectj
        MessageHandler handler = new MessageHandler(true)
        new Main().run(args as String[], handler)

        for (IMessage message : handler.getMessages(null, true)) {
            switch (message.getKind()) {
                case IMessage.ABORT:
                case IMessage.ERROR:
                case IMessage.FAIL:
                    logger.error message.message, message.thrown
                    break
                case IMessage.WARNING:
                    logger.warn message.message, message.thrown
                    break
                case IMessage.INFO:
                    logger.info message.message, message.thrown
                    break
                case IMessage.DEBUG:
                    logger.debug message.message, message.thrown
                    break
            }
        }
    }

    @Override
    String getName() {
        return "pretty"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return Sets.immutableEnumSet(QualifiedContent.DefaultContentType.CLASSES)
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return Sets.immutableEnumSet(QualifiedContent.Scope.PROJECT,
                QualifiedContent.Scope.EXTERNAL_LIBRARIES)
    }

    @Override
    Set<? super QualifiedContent.Scope> getReferencedScopes() {
        return Sets.immutableEnumSet(
                QualifiedContent.Scope.SUB_PROJECTS,
                QualifiedContent.Scope.EXTERNAL_LIBRARIES,
                QualifiedContent.Scope.PROVIDED_ONLY)
    }

    @Override
    boolean isIncremental() {
        return false
    }
}
