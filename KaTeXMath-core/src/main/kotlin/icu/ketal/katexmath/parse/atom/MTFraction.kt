package icu.ketal.katexmath.parse.atom

import icu.ketal.katexmath.parse.MTMathAtom
import icu.ketal.katexmath.parse.MTMathAtomType
import icu.ketal.katexmath.parse.MTMathList
import icu.ketal.katexmath.parse.MTMathListBuilder


// Fractions have no nucleus and are always KMTMathAtomFraction type
class MTFraction() : MTMathAtom(MTMathAtomType.KMTMathAtomFraction, "") {
  /// Numerator of the fraction
  var numerator: MTMathList? = null

  /// Denominator of the fraction
  var denominator: MTMathList? = null

  /**If true, the fraction has a rule (i.e., a line) between the numerator and denominator.
  The default value is true. */
  var hasRule: Boolean = true

  /** An optional delimiter for a fraction on the left. */
  var leftDelimiter: String? = null

  /** An optional delimiter for a fraction on the right. */
  var rightDelimiter: String? = null

  // fractions have no nucleus
  constructor(rule: Boolean) : this() {
    hasRule = rule
  }

  override fun toLatexString(): String = buildString {
    append(if (hasRule) "\\frac" else "\\atop")

    if (leftDelimiter != null || rightDelimiter != null) {
      append("[$leftDelimiter][$rightDelimiter]")
    }

    val nstr = numerator?.let { MTMathListBuilder.toLatexString(it) } ?: ""
    val dstr = denominator?.let { MTMathListBuilder.toLatexString(it) } ?: ""

    append("{$nstr}{$dstr}")

    toString().let { super.toStringSubs(it) }
  }

  override fun copyDeep(): MTFraction {
    val atom = MTFraction(this.hasRule)
    super.copyDeepContent(atom)
    atom.hasRule = this.hasRule
    atom.numerator = this.numerator?.copyDeep()
    atom.denominator = this.denominator?.copyDeep()
    atom.leftDelimiter = this.leftDelimiter
    atom.rightDelimiter = this.rightDelimiter
    return atom
  }

  override fun finalized(): MTFraction {
    val newFrac: MTFraction = this.copyDeep()
    super.finalized(newFrac)
    newFrac.numerator = newFrac.numerator?.finalized()
    newFrac.denominator = newFrac.denominator?.finalized()
    return newFrac
  }
}
