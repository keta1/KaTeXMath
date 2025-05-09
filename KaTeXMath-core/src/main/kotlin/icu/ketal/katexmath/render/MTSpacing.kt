package icu.ketal.katexmath.render

import icu.ketal.katexmath.parse.MTMathAtomType
import icu.ketal.katexmath.parse.MathDisplayException
import icu.ketal.katexmath.render.MTInterElementSpaceType.*

/**
 * Created by greg on 3/13/18.
 */

enum class MTInterElementSpaceType {
    KMTSpaceInvalid,
    KMTSpaceNone,
    KMTSpaceThin,
    KMTSpaceNSThin,    // Thin but not in script mode
    KMTSpaceNSMedium,
    KMTSpaceNSThick
}

val interElementSpaceArray: Array<Array<MTInterElementSpaceType>> = arrayOf(
  //   ordinary             operator             binary               relation            open                 close               punct               // fraction
  arrayOf(KMTSpaceNone, KMTSpaceThin, KMTSpaceNSMedium, KMTSpaceNSThick, KMTSpaceNone, KMTSpaceNone, KMTSpaceNone, KMTSpaceNSThin),    // ordinary
  arrayOf(KMTSpaceThin, KMTSpaceThin, KMTSpaceInvalid, KMTSpaceNSThick, KMTSpaceNone, KMTSpaceNone, KMTSpaceNone, KMTSpaceNSThin),    // operator
  arrayOf(KMTSpaceNSMedium, KMTSpaceNSMedium, KMTSpaceInvalid, KMTSpaceInvalid, KMTSpaceNSMedium, KMTSpaceInvalid, KMTSpaceInvalid, KMTSpaceNSMedium),  // binary
  arrayOf(KMTSpaceNSThick, KMTSpaceNSThick, KMTSpaceInvalid, KMTSpaceNone, KMTSpaceNSThick, KMTSpaceNone, KMTSpaceNone, KMTSpaceNSThick),   // relation
  arrayOf(KMTSpaceNone, KMTSpaceNone, KMTSpaceInvalid, KMTSpaceNone, KMTSpaceNone, KMTSpaceNone, KMTSpaceNone, KMTSpaceNone),      // open
  arrayOf(KMTSpaceNone, KMTSpaceThin, KMTSpaceNSMedium, KMTSpaceNSThick, KMTSpaceNone, KMTSpaceNone, KMTSpaceNone, KMTSpaceNSThin),    // close
  arrayOf(KMTSpaceNSThin, KMTSpaceNSThin, KMTSpaceInvalid, KMTSpaceNSThin, KMTSpaceNSThin, KMTSpaceNSThin, KMTSpaceNSThin, KMTSpaceNSThin),    // punct
  arrayOf(KMTSpaceNSThin, KMTSpaceThin, KMTSpaceNSMedium, KMTSpaceNSThick, KMTSpaceNSThin, KMTSpaceNone, KMTSpaceNSThin, KMTSpaceNSThin),    // fraction
  arrayOf(KMTSpaceNSMedium, KMTSpaceNSThin, KMTSpaceNSMedium, KMTSpaceNSThick, KMTSpaceNone, KMTSpaceNone, KMTSpaceNone, KMTSpaceNSThin)  // radical
)


// Gets the index for the given type. If the row is true, the index is for the row (i.e., left element), otherwise it is for the column (right element)
fun getInterElementSpaceArrayIndexForType(type: MTMathAtomType, row: Boolean): Int = when (type) {
  // A placeholder is treated as ordinary
  MTMathAtomType.KMTMathAtomColor,
  MTMathAtomType.KMTMathAtomTextColor,
  MTMathAtomType.KMTMathAtomOrdinary,
  MTMathAtomType.KMTMathAtomPlaceholder,
  MTMathAtomType.KMTMathAtomOverline,
  MTMathAtomType.KMTMathAtomUnderline,
  MTMathAtomType.KMTMathAtomBoxed -> 0

  MTMathAtomType.KMTMathAtomLargeOperator -> 1
  MTMathAtomType.KMTMathAtomBinaryOperator -> 2
  MTMathAtomType.KMTMathAtomRelation -> 3
  MTMathAtomType.KMTMathAtomOpen -> 4
  MTMathAtomType.KMTMathAtomClose -> 5
  MTMathAtomType.KMTMathAtomPunctuation -> 6
  MTMathAtomType.KMTMathAtomFraction,
  MTMathAtomType.KMTMathAtomInner -> 7

  MTMathAtomType.KMTMathAtomRadical -> if (row) {
    // Radicals have inter-element spaces only when on the left side.
    // Note: This is a departure from latex but we don't want \sqrt{4}4 to look weird so we put a space in between.
    // They have the same spacing as ordinary except with ordinary.
    8
  } else {
    throw MathDisplayException("Interelement space undefined for radical on the right. Treat radical as ordinary.")
  }

  else -> throw MathDisplayException("Interelement space undefined for type $type")
}
