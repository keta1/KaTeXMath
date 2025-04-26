package icu.ketal.katexmath.parse.atom

import icu.ketal.katexmath.parse.MTLineStyle
import icu.ketal.katexmath.parse.MTMathAtom
import icu.ketal.katexmath.parse.MTMathAtomType

// Styles are KMTMathAtomStyle with a MTLineStyle and no nucleus
class MTMathStyle(
  val style: MTLineStyle = MTLineStyle.KMTLineStyleDisplay
) : MTMathAtom(MTMathAtomType.KMTMathAtomStyle, nucleus = "") {
  override fun copyDeep(): MTMathStyle {
    val atom = MTMathStyle(style)
    super.copyDeepContent(atom)
    return atom
  }
}
