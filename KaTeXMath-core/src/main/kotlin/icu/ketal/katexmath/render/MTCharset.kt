package icu.ketal.katexmath.render

import icu.ketal.katexmath.parse.MTFontStyle
import icu.ketal.katexmath.parse.MathDisplayException

/**
 * Created by greg on 3/13/18.
 */

/*
   A string is a sequence of characters that could be 1 or 2 in length to represent a unicode charater.
   Given a string return the number of characters compensating
 */
fun numberOfGlyphs(s: String): Int = s.codePointCount(0, s.length)

data class CGGlyph(
  var gid: Int = 0,
  var glyphAscent: Float = 0f,
  var glyphDescent: Float = 0f,
  var glyphWidth: Float = 0f
) {
  val isValid: Boolean
    get() = gid != 0
}

const val kMTUnicodeGreekLowerStart: Char = '\u03B1'
const val kMTUnicodeGreekLowerEnd = '\u03C9'
const val kMTUnicodeGreekCapitalStart = '\u0391'
const val kMTUnicodeGreekCapitalEnd = '\u03A9'

// Note this is not equivalent to ch.isLowerCase() delta is a test case
fun isLowerEn(ch: Char): Boolean = (ch) >= 'a' && (ch) <= 'z'

fun isUpperEn(ch: Char): Boolean = (ch) >= 'A' && (ch) <= 'Z'

fun isNumber(ch: Char): Boolean = (ch) >= '0' && (ch) <= '9'

fun isLowerGreek(ch: Char): Boolean =
  (ch) >= kMTUnicodeGreekLowerStart && (ch) <= kMTUnicodeGreekLowerEnd

fun isCapitalGreek(ch: Char): Boolean =
  (ch) >= kMTUnicodeGreekCapitalStart && (ch) <= kMTUnicodeGreekCapitalEnd


fun greekSymbolOrder(ch: Char): Int {
  // These greek symbols that always appear in Unicode in this particular order after the alphabet
  // The symbols are epsilon, vartheta, varkappa, phi, varrho, varpi.
  val greekSymbols: Array<Int> = arrayOf(0x03F5, 0x03D1, 0x03F0, 0x03D5, 0x03F1, 0x03D6)
  return greekSymbols.indexOf(ch.code)
}

fun isGREEKSYMBOL(ch: Char): Boolean = (greekSymbolOrder(ch) != -1)

class MTCodepointChar(val codepoint: Int) {
  fun toUnicodeString(): String = buildString {
    val cs = Character.toChars(codepoint)
    append(cs)
  }
}


// mathit
const val kMTUnicodePlanksConstant = 0x210e
const val kMTUnicodeMathCapitalItalicStart = 0x1D434
const val kMTUnicodeMathLowerItalicStart = 0x1D44E
const val kMTUnicodeGreekCapitalItalicStart = 0x1D6E2
const val kMTUnicodeGreekLowerItalicStart = 0x1D6FC
const val kMTUnicodeGreekSymbolItalicStart = 0x1D716

fun getItalicized(ch: Char): MTCodepointChar = when {
  // italic h (plank's constant)
  ch == 'h' -> MTCodepointChar(kMTUnicodePlanksConstant)
  isUpperEn(ch) -> MTCodepointChar(kMTUnicodeMathCapitalItalicStart + (ch - 'A'))
  isLowerEn(ch) -> MTCodepointChar(kMTUnicodeMathLowerItalicStart + (ch - 'a'))
  // Capital Greek characters
  isCapitalGreek(ch) -> MTCodepointChar(kMTUnicodeGreekCapitalItalicStart + (ch - kMTUnicodeGreekCapitalStart))
  // Greek characters
  isLowerGreek(ch) -> MTCodepointChar(kMTUnicodeGreekLowerItalicStart + (ch - kMTUnicodeGreekLowerStart))
  isGREEKSYMBOL(ch) -> MTCodepointChar(kMTUnicodeGreekSymbolItalicStart + greekSymbolOrder(ch))
  // Note there are no italicized numbers in unicode so we don't support italicizing numbers.
  else -> MTCodepointChar(ch.code)
}

// mathbf
const val kMTUnicodeMathCapitalBoldStart = 0x1D400
const val kMTUnicodeMathLowerBoldStart = 0x1D41A
const val kMTUnicodeGreekCapitalBoldStart = 0x1D6A8
const val kMTUnicodeGreekLowerBoldStart = 0x1D6C2
const val kMTUnicodeGreekSymbolBoldStart = 0x1D6DC
const val kMTUnicodeNumberBoldStart = 0x1D7CE

fun getBold(ch: Char): MTCodepointChar = when {
  isUpperEn(ch) -> MTCodepointChar(kMTUnicodeMathCapitalBoldStart + (ch - 'A'))
  isLowerEn(ch) -> MTCodepointChar(kMTUnicodeMathLowerBoldStart + (ch - 'a'))
  // Capital Greek characters
  isCapitalGreek(ch) -> MTCodepointChar(kMTUnicodeGreekCapitalBoldStart + (ch - kMTUnicodeGreekCapitalStart))
  // Greek characters
  isLowerGreek(ch) -> MTCodepointChar(kMTUnicodeGreekLowerBoldStart + (ch - kMTUnicodeGreekLowerStart))
  isGREEKSYMBOL(ch) -> MTCodepointChar(kMTUnicodeGreekSymbolBoldStart + greekSymbolOrder(ch))
  isNumber(ch) -> MTCodepointChar(kMTUnicodeNumberBoldStart + (ch - '0'))
  else -> MTCodepointChar(ch.code)
}
// mathbfit
const val kMTUnicodeMathCapitalBoldItalicStart = 0x1D468
const val kMTUnicodeMathLowerBoldItalicStart = 0x1D482
const val kMTUnicodeGreekCapitalBoldItalicStart = 0x1D71C
const val kMTUnicodeGreekLowerBoldItalicStart = 0x1D736
const val kMTUnicodeGreekSymbolBoldItalicStart = 0x1D750

fun getBoldItalic(ch: Char): MTCodepointChar {
  return when {
    isUpperEn(ch) -> MTCodepointChar(kMTUnicodeMathCapitalBoldItalicStart + (ch - 'A'))
    isLowerEn(ch) -> MTCodepointChar(kMTUnicodeMathLowerBoldItalicStart + (ch - 'a'))
    isCapitalGreek(ch) -> {
      // Capital Greek characters
      MTCodepointChar(kMTUnicodeGreekCapitalBoldItalicStart + (ch - kMTUnicodeGreekCapitalStart))
    }
    isLowerGreek(ch) -> {
      // Greek characters
      MTCodepointChar(kMTUnicodeGreekLowerBoldItalicStart + (ch - kMTUnicodeGreekLowerStart))
    }
    isGREEKSYMBOL(ch) -> MTCodepointChar(kMTUnicodeGreekSymbolBoldItalicStart + greekSymbolOrder(ch))
    // No bold italic for numbers, so we just bold them.
    isNumber(ch) -> getBold(ch)
    else -> MTCodepointChar(ch.code)
  }
}

// LaTeX default
fun getDefaultStyle(ch: Char): MTCodepointChar = when {
  isLowerEn(ch) || isUpperEn(ch) || isLowerGreek(ch) || isGREEKSYMBOL(ch) -> getItalicized(ch)
  isNumber(ch) || isCapitalGreek(ch) -> MTCodepointChar(ch.code)
  // . is treated as a number in our code, but it doesn't change fonts.
  ch == '.' -> MTCodepointChar(ch.code)
  else -> throw MathDisplayException("Unknown character $ch for default style.")
}

const val kMTUnicodeMathCapitalScriptStart = 0x1D49C
// TODO(kostub): Unused in Latin Modern Math - if another font is used determine if
// this should be applicable.
// static const MTCodepointChar kMTUnicodeMathLowerScriptStart = 0x1D4B6;

// mathcal/mathscr (caligraphic or script)
fun getCaligraphic(ch: Char): MTCodepointChar {
  // Caligraphic has lots of exceptions:
  return when (ch) {
    'B' -> MTCodepointChar(0x212C)   // Script B (bernoulli)
    'E' -> MTCodepointChar(0x2130)   // Script E (emf)
    'F' -> MTCodepointChar(0x2131)   // Script F (fourier)
    'H' -> MTCodepointChar(0x210B)   // Script H (hamiltonian)
    'I' -> MTCodepointChar(0x2110)   // Script I
    'L' -> MTCodepointChar(0x2112)   // Script L (laplace)
    'M' -> MTCodepointChar(0x2133)   // Script M (M-matrix)
    'R' -> MTCodepointChar(0x211B)   // Script R (Riemann integral)
    'e' -> MTCodepointChar(0x212F)   // Script e (Natural exponent)
    'g' -> MTCodepointChar(0x210A)   // Script g (real number)
    'o' -> MTCodepointChar(0x2134)   // Script o (order)
    else -> when {
      isUpperEn(ch) -> MTCodepointChar(kMTUnicodeMathCapitalScriptStart + (ch - 'A'))
      // Latin Modern Math does not have lower case caligraphic characters, so we use
      // the default style instead of showing a?
      isLowerEn(ch) -> getDefaultStyle(ch)

      // Caligraphic characters don't exist for greek or numbers, we give them the
      // default treatment.
      else -> getDefaultStyle(ch)
    }
  }
}

const val kMTUnicodeMathCapitalTTStart = 0x1D670
const val kMTUnicodeMathLowerTTStart = 0x1D68A
const val kMTUnicodeNumberTTStart = 0x1D7F6

// mathtt (monospace)
fun getTypewriter(ch: Char): MTCodepointChar = when {
  isUpperEn(ch) -> MTCodepointChar(kMTUnicodeMathCapitalTTStart + (ch - 'A'))
  isLowerEn(ch) -> MTCodepointChar(kMTUnicodeMathLowerTTStart + (ch - 'a'))
  isNumber(ch) -> MTCodepointChar(kMTUnicodeNumberTTStart + (ch - '0'))
  // Monospace characters don't exist for greek, we give them the
  // default treatment.
  else -> getDefaultStyle(ch)
}

const val kMTUnicodeMathCapitalSansSerifStart = 0x1D5A0
const val kMTUnicodeMathLowerSansSerifStart = 0x1D5BA
const val kMTUnicodeNumberSansSerifStart = 0x1D7E2

// mathsf
fun getSansSerif(ch: Char): MTCodepointChar = when {
  isUpperEn(ch) -> MTCodepointChar(kMTUnicodeMathCapitalSansSerifStart + (ch - 'A'))
  isLowerEn(ch) -> MTCodepointChar(kMTUnicodeMathLowerSansSerifStart + (ch - 'a'))
  isNumber(ch) -> MTCodepointChar(kMTUnicodeNumberSansSerifStart + (ch - '0'))
  // Sans-serif characters don't exist for greek, we give them the
  // default treatment.
  else -> getDefaultStyle(ch)
}

const val kMTUnicodeMathCapitalFrakturStart = 0x1D504
const val kMTUnicodeMathLowerFrakturStart = 0x1D51E

// mathfrak
fun getFraktur(ch: Char): MTCodepointChar = when (ch) {
  // Fraktur has exceptions:
  'C' -> MTCodepointChar(0x212D)   // C Fraktur
  'H' -> MTCodepointChar(0x210C)   // Hilbert space
  'I' -> MTCodepointChar(0x2111)   // Imaginary
  'R' -> MTCodepointChar(0x211C)   // Real
  'Z' -> MTCodepointChar(0x2128)   // Z Fraktur
  else -> when {
    isUpperEn(ch) -> MTCodepointChar(kMTUnicodeMathCapitalFrakturStart + (ch - 'A'))
    isLowerEn(ch) -> MTCodepointChar(kMTUnicodeMathLowerFrakturStart + (ch - 'a'))
    // Fraktur characters don't exist for greek & numbers, we give them the
    // default treatment.
    else -> getDefaultStyle(ch)
  }
}

const val kMTUnicodeMathCapitalBlackboardStart = 0x1D538
const val kMTUnicodeMathLowerBlackboardStart = 0x1D552
const val kMTUnicodeNumberBlackboardStart = 0x1D7D8

// mathbb (double struck)
fun getBlackboard(ch: Char): MTCodepointChar = when (ch) {
  // Blackboard has lots of exceptions:
  'C' -> MTCodepointChar(0x2102)  // Complex numbers
  'H' -> MTCodepointChar(0x210D)  // Quarternions
  'N' -> MTCodepointChar(0x2115)  // Natural numbers
  'P' -> MTCodepointChar(0x2119)  // Primes
  'Q' -> MTCodepointChar(0x211A)  // Rationals
  'R' -> MTCodepointChar(0x211D)  // Reals
  'Z' -> MTCodepointChar(0x2124)  // Integers
  else -> when {
    isUpperEn(ch) -> MTCodepointChar(kMTUnicodeMathCapitalBlackboardStart + (ch - 'A'))
    isLowerEn(ch) -> MTCodepointChar(kMTUnicodeMathLowerBlackboardStart + (ch - 'a'))
    isNumber(ch) -> MTCodepointChar(kMTUnicodeNumberBlackboardStart + (ch - '0'))
    // Blackboard characters don't exist for greek, we give them the
    // default treatment.
    else -> getDefaultStyle(ch)
  }
}

fun styleCharacter(ch: Char, fontStyle: MTFontStyle): MTCodepointChar = when (fontStyle) {
  MTFontStyle.KMTFontStyleDefault -> getDefaultStyle(ch)
  MTFontStyle.KMTFontStyleRoman -> MTCodepointChar(ch.code)
  MTFontStyle.KMTFontStyleBold -> getBold(ch)
  MTFontStyle.KMTFontStyleItalic -> getItalicized(ch)
  MTFontStyle.KMTFontStyleBoldItalic -> getBoldItalic(ch)
  MTFontStyle.KMTFontStyleCaligraphic -> getCaligraphic(ch)
  MTFontStyle.KMTFontStyleTypewriter -> getTypewriter(ch)
  MTFontStyle.KMTFontStyleSansSerif -> getSansSerif(ch)
  MTFontStyle.KMTFontStyleFraktur -> getFraktur(ch)
  MTFontStyle.KMTFontStyleBlackboard -> getBlackboard(ch)
}

// This can only take a single Unicode character sequence as input.
// Should never be called with a codepoint that requires 2 escaped characters to represent
fun changeFont(str: String, fontStyle: MTFontStyle): String = buildString {
  str.forEach { ch ->
    val codepoint = styleCharacter(ch, fontStyle)
    append(codepoint.toUnicodeString())
  }
}
