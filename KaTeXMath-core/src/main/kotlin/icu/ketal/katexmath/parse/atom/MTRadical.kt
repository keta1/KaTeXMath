package icu.ketal.katexmath.parse.atom

import icu.ketal.katexmath.parse.MTMathAtom
import icu.ketal.katexmath.parse.MTMathAtomType
import icu.ketal.katexmath.parse.MTMathList
import icu.ketal.katexmath.parse.MTMathListBuilder


// Radicals have no nucleus and are always KMTMathAtomRadical type
class MTRadical : MTMathAtom(MTMathAtomType.KMTMathAtomRadical, "") {
  /// Denotes the degree of the radical, i.e., the value to the top left of the radical sign
  /// This can be null if there is no degree.
  var degree: MTMathList? = null

  /// Denotes the term under the square root sign
  ///
  var radicand: MTMathList? = null


  override fun toLatexString(): String {
    var str = "\\sqrt"

    val deg: MTMathList? = this.degree
    if (deg != null) {
      val dstr = MTMathListBuilder.toLatexString(deg)
      str += "[$dstr]"
    }

    val rad: MTMathList? = this.radicand
    var rstr = ""
    if (rad != null) {
      rstr = MTMathListBuilder.toLatexString(rad)
    }

    str += "{$rstr}"

    return super.toStringSubs(str)
  }

  override fun copyDeep(): MTRadical {
    val atom = MTRadical()
    super.copyDeepContent(atom)
    atom.radicand = this.radicand?.copyDeep()
    atom.degree = this.degree?.copyDeep()
    return atom
  }

  override fun finalized(): MTRadical {
    val newRad: MTRadical = this.copyDeep()
    super.finalized(newRad)
    newRad.radicand = newRad.radicand?.finalized()
    newRad.degree = newRad.degree?.finalized()
    return newRad
  }
}
