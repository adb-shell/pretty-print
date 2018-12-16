package com.karthik

import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin


class PrettyPlugin implements Plugin<Project>{

    @Override
    void apply(Project pro) {
        makeSureProjectIsAndroidAppOrLib(pro)

        pro.extensions.add("prettyEx",PrettyPluginExtension)

        pro.android.registerTransform(new PrettyTransform(pro))

        pro.repositories {
            maven {
                url 'https://dl.bintray.com/nullpointerguy/maven/'
            }
        }

        pro.dependencies {
            implementation 'org.aspectj:aspectjrt:1.9.1'
            implementation 'com.karthik.prettyruntime:pretty-runtime:0.1.0'
            api 'com.karthik.pretty_annotation:pretty-annotation:0.5.0'
        }
    }

    private void makeSureProjectIsAndroidAppOrLib(Project project) {
        def hasApp = project.plugins.withType(AppPlugin)
        def hasLib = project.plugins.withType(LibraryPlugin)

        if (!hasApp && !hasLib) {
            throw new IllegalStateException("'android' or 'android-library' plugin required.")
        }
    }
}