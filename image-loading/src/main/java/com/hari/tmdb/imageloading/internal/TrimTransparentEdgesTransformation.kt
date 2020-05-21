package com.hari.tmdb.imageloading.internal

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import androidx.core.graphics.alpha
import coil.bitmappool.BitmapPool
import coil.size.Size
import coil.transform.Transformation

/**
 * A [Transformation] that trims transparent edges from an image.
 */
object TrimTransparentEdgesTransformation : Transformation {
    override fun key(): String = TrimTransparentEdgesTransformation::class.java.name

    override suspend fun transform(pool: BitmapPool, input: Bitmap, size: Size): Bitmap {
        val inputWidth = input.width
        val inputHeight = input.height

        var firstX = 0
        var firstY = 0
        var lastX = inputWidth
        var lastY = inputHeight

        val pixels = IntArray(inputWidth * inputHeight)
        input.getPixels(pixels, 0, inputWidth, 0, 0, inputWidth, inputHeight)

        loop@
        for (x in 0 until inputWidth) {
            for (y in 0 until inputHeight) {
                if (pixels[x + y * inputWidth].alpha > 0) {
                    firstX = x
                    break@loop
                }
            }
        }

        loop@
        for (y in 0 until inputHeight) {
            for (x in firstX until inputWidth) {
                if (pixels[x + y * inputWidth].alpha > 0) {
                    firstY = y
                    break@loop
                }
            }
        }

        loop@
        for (x in inputWidth - 1 downTo firstX) {
            for (y in inputHeight - 1 downTo firstY) {
                if (pixels[x + y * inputWidth].alpha > 0) {
                    lastX = x
                    break@loop
                }
            }
        }

        loop@
        for (y in inputHeight - 1 downTo firstY) {
            for (x in inputWidth - 1 downTo firstX) {
                if (pixels[x + y * inputWidth].alpha > 0) {
                    lastY = y
                    break@loop
                }
            }
        }

        if (firstX == 0 && firstY == 0 && lastX == inputWidth && lastY == inputHeight) {
            return input
        }

        val output = pool.get(1 + lastX - firstX, 1 + lastY - firstY, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val src = Rect(firstX, firstY, firstX + output.width, firstY + output.height)
        val dst = Rect(0, 0, output.width, output.height)

        canvas.drawBitmap(input, src, dst, null)

        // Put the original bitmap in the pool
        pool.put(input)

        return output
    }
}
