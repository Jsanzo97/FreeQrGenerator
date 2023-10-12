package com.example.freeqrgenerator

import android.graphics.Bitmap
import android.graphics.Color
import androidx.annotation.ColorInt
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

@Throws(WriterException::class)
fun String.encodeAsQrCodeBitmap(
    dimension: Int,
    @ColorInt color1: Int = Color.BLACK,
    @ColorInt color2: Int = Color.WHITE
): Bitmap? {

    val result: BitMatrix
    try {
        result = MultiFormatWriter().encode(
            this,
            BarcodeFormat.QR_CODE,
            dimension,
            dimension,
            hashMapOf(EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.H)
        )
    } catch (e: IllegalArgumentException) {
        return null
    }

    val w = result.width
    val h = result.height
    val pixels = IntArray(w * h)
    for (y in 0 until h) {
        val offset = y * w
        for (x in 0 until w) {
            pixels[offset + x] = if (result.get(x, y)) color1 else color2
        }
    }
    val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
    bitmap.setPixels(pixels, 0, dimension, 0, 0, w, h)

    return bitmap
}
