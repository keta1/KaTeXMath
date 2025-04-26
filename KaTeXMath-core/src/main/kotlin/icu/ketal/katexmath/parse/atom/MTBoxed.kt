package icu.ketal.katexmath.parse.atom

import icu.ketal.katexmath.parse.MTMathAtom
import icu.ketal.katexmath.parse.MTMathAtomType
import icu.ketal.katexmath.parse.MTMathList
import icu.ketal.katexmath.parse.MTMathListBuilder


// MTBoxed have no nucleus and are always KMTMathAtomBoxed type
class MTBoxed : MTMathAtom(MTMathAtomType.KMTMathAtomBoxed, "") {
  /// The inner math list
  var innerList: MTMathList? = null

  override fun toLatexString(): String {
    val il: MTMathList? = this.innerList
    var istr = ""
    if (il != null) {
      istr = MTMathListBuilder.toLatexString(il)
    }

    return "\\boxed{$istr}"
  }

  override fun copyDeep(): MTBoxed {
    val atom = MTBoxed()
    super.copyDeepContent(atom)
    atom.innerList = this.innerList?.copyDeep()
    return atom
  }

  override fun finalized(): MTBoxed {
    val newBoxed: MTBoxed = this.copyDeep()
    super.finalized(newBoxed)
    newBoxed.innerList = newBoxed.innerList?.finalized()
    return newBoxed
  }
}
