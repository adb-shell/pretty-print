package com.karthik.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.karthik.pretty_annotation.Pretty

/**
 * Created by karthikrk on 25/03/18.
 */
@Pretty(headerName = "KotlinClass")
class KotlinActivity :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        PrettyPrinterExtras.KotlinClassprintExtras(intent.extras)
    }
}