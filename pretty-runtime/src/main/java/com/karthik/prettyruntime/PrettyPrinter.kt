package com.karthik.prettyruntime

import android.os.Bundle
import android.os.Parcel
import com.jakewharton.fliptables.FlipTable

object PrettyPrinter{
    private fun getBundleSize(bundle: Bundle?): String {
        val parcel = Parcel.obtain()
        val bytes: ByteArray
        parcel.writeBundle(bundle)
        bytes = parcel.marshall()
        parcel.recycle()
        return bytes.size.toString()
    }

    fun printExtras(bundle: Bundle?, headers: Array<String>) {
        if (bundle == null || bundle.isEmpty) {
            return
        }
        val valueKeys = Array<Array<String?>>(bundle.keySet().size + 1) { arrayOfNulls(2) }
        var col = 0
        for ((row, key) in bundle.keySet().withIndex()) {
            col = 0
            valueKeys[row][col] = key
            ++col
            valueKeys[row][col] = bundle.get(key).toString()
        }
        valueKeys[bundle.keySet().size][0] = SIZE
        valueKeys[bundle.keySet().size][1] = getBundleSize(bundle) + " Bytes"

        println(FlipTable.of(headers, valueKeys))
    }
}