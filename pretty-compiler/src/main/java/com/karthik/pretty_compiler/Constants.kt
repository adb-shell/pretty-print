package com.karthik.pretty_compiler

import com.squareup.javapoet.ClassName

 val PRINTERSUFFIX = "printExtras"
 val EXTRAHEADER = "ClassName"
 val PRINTERCLASSNAME = "PrettyPrinterExtras"
 val PRINTERMETHODNAME = "printExtras"
 val PRINTBUNDLESIZE = "getBundleSize"
 val SIZE = "SIZE(Bytes)"

 val BUNDLE = ClassName.get("android.os","Bundle")
 val PARCEL = ClassName.get("android.os","Parcel")
 val LOG = ClassName.get("android.util","Log")
 val FLIPTABLE = ClassName.get("com.jakewharton.fliptables",
        "FlipTable")