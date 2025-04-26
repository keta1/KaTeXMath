package icu.ketal.katexmath.parse.atom

import icu.ketal.katexmath.parse.MTMathAtom
import icu.ketal.katexmath.parse.MTMathAtomType
import icu.ketal.katexmath.parse.MTMathList
import icu.ketal.katexmath.parse.MTMathListBuilder


// Accents are always KMTMathAtomUnderline type
class MTAccent(nucleus: String) : MTMathAtom(MTMathAtomType.KMTMathAtomAccent, nucleus) {
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

  override fun copyDeep(): MTAccent {
    val atom = MTAccent(nucleus)
    super.copyDeepContent(atom)
    atom.innerList = this.innerList?.copyDeep()
    return atom
  }

  override fun finalized(): MTAccent {
    val newAccent: MTAccent = this.copyDeep()
    super.finalized(newAccent)
    newAccent.innerList = newAccent.innerList?.finalized()
    return newAccent
  }
}
