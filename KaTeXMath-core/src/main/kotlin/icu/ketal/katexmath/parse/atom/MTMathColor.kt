package icu.ketal.katexmath.parse.atom

import icu.ketal.katexmath.parse.MTMathAtom
import icu.ketal.katexmath.parse.MTMathAtomType
import icu.ketal.katexmath.parse.MTMathList

// Colors are always KMTMathAtomColor type with a string for the color
class MTMathColor : MTMathAtom(MTMathAtomType.KMTMathAtomColor, "") {
  /// The inner math list
  var innerList: MTMathList? = null
  var colorString: String? = null

  override fun toLatexString(): String {
    var str = "\\color"

    str += "{$this.colorString}{$this.innerList}"

    return super.toStringSubs(str)
  }

  override fun copyDeep(): MTMathColor {
    val atom = MTMathColor()
    super.copyDeepContent(atom)
    atom.innerList = this.innerList?.copyDeep()
    atom.colorString = this.colorString
    return atom
  }

  override fun finalized(): MTMathColor {
    val newColor: MTMathColor = this.copyDeep()
    super.finalized(newColor)
    newColor.innerList = newColor.innerList?.finalized()
    return newColor
  }
}
