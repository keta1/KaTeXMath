package icu.ketal.katexmath.render

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.pvporbit.freetype.FreeTypeConstants
import icu.ketal.katexmath.parse.MathDisplayException


class MTDrawFreeType(val mathfont: MTFontMathTable) {
  fun drawGlyph(canvas: Canvas, p: Paint, gid: Int, x: Float, y: Float) {
    val face = mathfont.checkFontSize()

    /* load glyph image into the slot and render (erase previous one) */
    if (gid != 0 && !face.loadGlyph(gid, FreeTypeConstants.FT_LOAD_RENDER)) {
      val gSlot = face.getGlyphSlot()
      val plainBitmap = gSlot.getBitmap()
      if (plainBitmap != null) {
        if (plainBitmap.width == 0 || plainBitmap.rows == 0) {
          if (gid != 1 && gid != 33) {
            throw MathDisplayException("missing glyph slot $gid.")
          }
        } else {
          val bitmap =
            Bitmap.createBitmap(plainBitmap.width, plainBitmap.rows, Bitmap.Config.ALPHA_8)
          bitmap.copyPixelsFromBuffer(plainBitmap.buffer)
          val metrics = gSlot.metrics
          val offX = metrics.horiBearingX / 64.0f  // 26.6 fixed point integer from freetype
          val offY = metrics.horiBearingY / 64.0f
          canvas.drawBitmap(bitmap, x + offX, y - offY, p)
        }
      }
    }
  }
  // val enclosing = BoundingBox()

  /*
  val numGrays: Short
      get() = FreeType.FT_Bitmap_Get_num_grays(pointer)

  val paletteMode: Char
      get() = FreeType.FT_Bitmap_Get_palette_mode(pointer)

  val pixelMode: Char
      get() = FreeType.FT_Bitmap_Get_pixel_mode(pointer)
      */
}
