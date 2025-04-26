package icu.ketal.katexmath.parse.atom

import icu.ketal.katexmath.parse.MTMathAtom
import icu.ketal.katexmath.parse.MTMathAtomType

// Spaces are KMTMathAtomSpace with a float for space and no nucleus
class MTMathSpace(
  val space: Float = 0f
) : MTMathAtom(MTMathAtomType.KMTMathAtomSpace, "") {
  override fun copyDeep(): MTMathSpace {
    val atom = MTMathSpace(space)
    super.copyDeepContent(atom)
    return atom
  }
}
