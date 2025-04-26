package icu.ketal.katexmath.parse.atom

import icu.ketal.katexmath.parse.MTMathAtom
import icu.ketal.katexmath.parse.MTMathAtomType
import icu.ketal.katexmath.parse.MTMathList
import icu.ketal.katexmath.parse.MTMathListBuilder
import icu.ketal.katexmath.parse.MathDisplayException


// Inners have no nucleus and are always KMTMathAtomInner type
class MTInner : MTMathAtom(MTMathAtomType.KMTMathAtomInner, "") {
  /// The inner math list
  var innerList: MTMathList? = null

  /// The left boundary atom. This must be a node of type KMTMathAtomBoundary
  var leftBoundary: MTMathAtom? = null
    set(value) {
      if (value != null && value.type != MTMathAtomType.KMTMathAtomBoundary) {
        throw MathDisplayException("Left boundary must be of type KMTMathAtomBoundary $value")
      }
      field = value
    }

  /// The right boundary atom. This must be a node of type KMTMathAtomBoundary
  var rightBoundary: MTMathAtom? = null
    set(value) {
      if (value != null && value.type != MTMathAtomType.KMTMathAtomBoundary) {
        throw MathDisplayException("Right boundary must be of type KMTMathAtomBoundary $value")
      }
      field = value
    }


  override fun toLatexString(): String {
    var str = "\\inner"

    val lb = this.leftBoundary
    if (lb != null) {
      str += "[" + lb.nucleus + "]"
    }

    val il: MTMathList? = this.innerList
    var istr = ""
    if (il != null) {
      istr = MTMathListBuilder.toLatexString(il)
    }

    str += "{$istr}"

    val rb = this.rightBoundary
    if (rb != null) {
      str += "[" + rb.nucleus + "]"
    }
    return super.toStringSubs(str)
  }

  override fun copyDeep(): MTInner {
    val atom = MTInner()
    super.copyDeepContent(atom)
    atom.innerList = this.innerList?.copyDeep()
    atom.leftBoundary = this.leftBoundary?.copyDeep()
    atom.rightBoundary = this.rightBoundary?.copyDeep()
    return atom
  }

  override fun finalized(): MTInner {
    val newInner: MTInner = this.copyDeep()
    super.finalized(newInner)
    newInner.innerList = newInner.innerList?.finalized()
    newInner.leftBoundary = newInner.leftBoundary?.finalized()
    newInner.rightBoundary = newInner.rightBoundary?.finalized()
    return newInner
  }
}
