package com.karthik.pretty_annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Pretty(val headerName:String="")