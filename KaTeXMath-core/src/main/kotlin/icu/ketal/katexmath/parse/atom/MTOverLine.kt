package icu.ketal.katexmath.parse.atom

import icu.ketal.katexmath.parse.MTMathAtom
import icu.ketal.katexmath.parse.MTMathAtomType
import icu.ketal.katexmath.parse.MTMathList
import icu.ketal.katexmath.parse.MTMathListBuilder


// OverLines have no nucleus and are always KMTMathAtomOverline type
class MTOverLine : MTMathAtom(MTMathAtomType.KMTMathAtomOverline, "") {
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

  override fun copyDeep(): MTOverLine {
    val atom = MTOverLine()
    super.copyDeepContent(atom)
    atom.innerList = this.innerList?.copyDeep()
    return atom
  }

  override fun finalized(): MTOverLine {
    val newOverLine: MTOverLine = this.copyDeep()
    super.finalized(newOverLine)
    newOverLine.innerList = newOverLine.innerList?.finalized()
    return newOverLine
  }
}
