package icu.ketal.katexmath.parse.atom

import icu.ketal.katexmath.parse.MTMathAtom
import icu.ketal.katexmath.parse.MTMathAtomType
import icu.ketal.katexmath.parse.MTMathList
import icu.ketal.katexmath.parse.MTMathListBuilder

// UnderLines have no nucleus and are always KMTMathAtomUnderline type
class MTUnderLine : MTMathAtom(MTMathAtomType.KMTMathAtomUnderline, "") {
  /// The inner math list
  var innerList: MTMathList? = null

  override fun toLatexString(): String {
    val il: MTMathList? = this.innerList
    var istr = ""
    if (il != null) {
      istr = MTMathListBuilder.toLatexString(il)
    }

    return "{$istr}"
  }

  override fun copyDeep(): MTUnderLine {
    val atom = MTUnderLine()
    super.copyDeepContent(atom)
    atom.innerList = this.innerList?.copyDeep()
    return atom
  }

  override fun finalized(): MTUnderLine {
    val newUnderLine: MTUnderLine = this.copyDeep()
    super.finalized(newUnderLine)
    newUnderLine.innerList = newUnderLine.innerList?.finalized()
    return newUnderLine
  }
}
