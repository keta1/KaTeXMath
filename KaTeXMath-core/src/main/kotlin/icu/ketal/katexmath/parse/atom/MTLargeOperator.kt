package icu.ketal.katexmath.parse.atom

import icu.ketal.katexmath.parse.MTMathAtom
import icu.ketal.katexmath.parse.MTMathAtomType

class MTLargeOperator(
  nucleus: String,
  var hasLimits: Boolean = false
) : MTMathAtom(MTMathAtomType.KMTMathAtomLargeOperator, nucleus) {
  override fun copyDeep(): MTLargeOperator {
    val atom = MTLargeOperator(nucleus, hasLimits)
    super.copyDeepContent(atom)
    return atom
  }
}
