package com.karthik.pretty_annotation

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Pretty(val headerName:String="")