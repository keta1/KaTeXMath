package icu.ketal.katexmath.parse

import icu.ketal.katexmath.parse.atom.MTAccent
import icu.ketal.katexmath.parse.atom.MTBoxed
import icu.ketal.katexmath.parse.atom.MTFraction
import icu.ketal.katexmath.parse.atom.MTInner
import icu.ketal.katexmath.parse.atom.MTLargeOperator
import icu.ketal.katexmath.parse.atom.MTMathColor
import icu.ketal.katexmath.parse.atom.MTMathSpace
import icu.ketal.katexmath.parse.atom.MTOverLine
import icu.ketal.katexmath.parse.atom.MTRadical
import icu.ketal.katexmath.parse.atom.MTUnderLine


class MathDisplayException(override var message: String) : Exception(message)

/**
@typedef MTMathAtomType
@brief The type of atom in a `MTMathList`.

The type of the atom determines how it is rendered, and spacing between the atoms.
 */
enum class MTMathAtomType {
  // A non-atom
  KMTMathAtomNone,
  /// A number or text in ordinary format - Ord in TeX
  KMTMathAtomOrdinary,
  /// A number - Does not exist in TeX
  KMTMathAtomNumber,
  /// A variable (i.e. text in italic format) - Does not exist in TeX
  KMTMathAtomVariable,
  /// A large operator such as (sin/cos, integral etc.) - Op in TeX
  KMTMathAtomLargeOperator,
  /// A binary operator - Bin in TeX
  KMTMathAtomBinaryOperator,
  /// A unary operator - Does not exist in TeX.
  KMTMathAtomUnaryOperator,
  /// A relation, e.g. = > < etc. - Rel in TeX
  KMTMathAtomRelation,
  /// Open brackets - Open in TeX
  KMTMathAtomOpen,
  /// Close brackets - Close in TeX
  KMTMathAtomClose,
  /// An fraction e.g 1/2 - generalized fraction noad in TeX
  KMTMathAtomFraction,
  /// A radical operator e.g. sqrt(2)
  KMTMathAtomRadical,
  /// Punctuation such as , - Punct in TeX
  KMTMathAtomPunctuation,
  /// A placeholder square for future input. Does not exist in TeX
  KMTMathAtomPlaceholder,
  /// An inner atom, i.e. an embedded math list - Inner in TeX
  KMTMathAtomInner,
  /// An underlined atom - Under in TeX
  KMTMathAtomUnderline,
  /// An overlined atom - Over in TeX
  KMTMathAtomOverline,
  /// An accented atom - Accent in TeX
  KMTMathAtomAccent,
  /// A boxed atom - not in original TeX
  KMTMathAtomBoxed,

  // Atoms after this point do not support subscripts or superscripts

  /// A left atom - Left & Right in TeX. We don't need two since we track boundaries separately.
  KMTMathAtomBoundary,

  // Atoms after this are non-math TeX nodes that are still useful in math mode. They do not have
  // the usual structure.

  /// Spacing between math atoms. This denotes both glue and kern for TeX. We do not
  /// distinguish between glue and kern.
  KMTMathAtomSpace,
  /// Denotes style changes during rendering.
  KMTMathAtomStyle,
  KMTMathAtomColor,
  KMTMathAtomTextColor,

  // Atoms after this point are not part of TeX and do not have the usual structure.

  /// An table atom. This atom does not exist in TeX. It is equivalent to the TeX command
  /// halign which is handled outside of the TeX math rendering engine. We bring it into our
  /// math typesetting to handle matrices and other tables.
  KMTMathAtomTable
}

const val NSNotFound: Int = -1

data class NSRange(var location: Int = NSNotFound, var length: Int = 0) {
  // Return true if equal to passed range
  fun equal(cmp: NSRange): Boolean {
    return (cmp.location == this.location && cmp.length == this.length)
  }

  val maxrange
    get() = location + length

  fun union(a: NSRange): NSRange {
    val b = this
    val e = maxOf(a.maxrange, b.maxrange)
    val s = minOf(a.location, b.location)
    return NSRange(s, e - s)
  }
}

enum class MTFontStyle {
  /// The default latex rendering style. i.e. variables are italic and numbers are roman.
  KMTFontStyleDefault,

  /// Roman font style i.e. \mathrm
  KMTFontStyleRoman,

  /// Bold font style i.e. \mathbf
  KMTFontStyleBold,

  /// Caligraphic font style i.e. \mathcal
  KMTFontStyleCaligraphic,

  /// Typewriter (monospace) style i.e. \mathtt
  KMTFontStyleTypewriter,

  /// Italic style i.e. \mathit
  KMTFontStyleItalic,

  /// San-serif font i.e. \mathss
  KMTFontStyleSansSerif,

  /// Fractur font i.e \mathfrak
  KMTFontStyleFraktur,

  /// Blackboard font i.e. \mathbb
  KMTFontStyleBlackboard,

  /// Bold italic
  KMTFontStyleBoldItalic,
}

/** A `MTMathAtom` is the basic unit of a math list. Each atom represents a single character
or mathematical operator in a list. However, certain atoms can represent more complex structures
such as fractions and radicals. Each atom has a type which determines how the atom is rendered and
a nucleus. The nucleus contains the character(s) that need to be rendered. However the nucleus may
be empty for certain types of atoms. An atom has an optional subscript or superscript which represents
the subscript or superscript that is to be rendered.

Certain types of atoms inherit from `MTMathAtom` and may have additional fields.
 */
/*
constructor
/** Factory function to create an atom with a given type and value.
@param type The type of the atom to instantiate.
@param value The value of the atoms nucleus. The value is ignored for fractions and radicals.
 */
+ (instancetype) atomWithType: (MTMathAtomType) type value:(NSString*) value;

 */

open class MTMathAtom(var type: MTMathAtomType, var nucleus: String) {
  /** Returns a string representation of the MTMathAtom */
  /** The nucleus of the atom. */

  /** An optional superscript. */
  var superScript: MTMathList? = null
    set(value) {
      if (!this.scriptsAllowed()) {
        throw MathDisplayException("Superscripts not allowed for atom $this")
      }
      field = value
    }

  /** An optional subscript. */
  var subScript: MTMathList? = null
    set(value) {
      if (!this.scriptsAllowed()) {
        throw MathDisplayException("Subscripts not allowed for atom $this")
      }
      field = value
    }


  /** The font style to be used for the atom. */
  var fontStyle: MTFontStyle = MTFontStyle.KMTFontStyleDefault

  /// If this atom was formed by fusion of multiple atoms, then this stores the list of atoms that were fused to create this one.
  /// This is used in the finalizing and preprocessing steps.
  var fusedAtoms = mutableListOf<MTMathAtom>()

  /// The index range in the MTMathList this MTMathAtom tracks. This is used by the finalizing and preprocessing steps
  /// which fuse MTMathAtoms to track the position of the current MTMathAtom in the original list.
  // This will be the zero Range until finalize is called on the MTMathList
  var indexRange: NSRange = NSRange(0, 0)

  internal fun dumpstr(s: String) {
    val ca = s.toCharArray()
    val cp = Character.codePointAt(ca, 0)
    println("str $s codepoint $cp")
    for (c in ca) {
      println("c $c")
    }
  }

  companion object Factory : MTMathAtomFactory() {
    // Returns true if the current binary operator is not really binary.
    fun isNotBinaryOperator(prevNode: MTMathAtom?): Boolean = when (prevNode?.type) {
      null -> true
      MTMathAtomType.KMTMathAtomBinaryOperator,
      MTMathAtomType.KMTMathAtomRelation,
      MTMathAtomType.KMTMathAtomOpen,
      MTMathAtomType.KMTMathAtomPunctuation,
      MTMathAtomType.KMTMathAtomLargeOperator -> true
      else -> false
    }

    fun typeToText(type: MTMathAtomType) = when (type) {
      MTMathAtomType.KMTMathAtomNone -> "None"
      MTMathAtomType.KMTMathAtomOrdinary -> "Ordinary"
      MTMathAtomType.KMTMathAtomNumber -> "Number"
      MTMathAtomType.KMTMathAtomVariable -> "Variable"
      MTMathAtomType.KMTMathAtomBinaryOperator -> "Binary Operator"
      MTMathAtomType.KMTMathAtomUnaryOperator -> "Unary Operator"
      MTMathAtomType.KMTMathAtomRelation -> "Relation"
      MTMathAtomType.KMTMathAtomOpen -> "Open"
      MTMathAtomType.KMTMathAtomClose -> "Close"
      MTMathAtomType.KMTMathAtomFraction -> "Fraction"
      MTMathAtomType.KMTMathAtomRadical -> "Radical"
      MTMathAtomType.KMTMathAtomPunctuation -> "Punctuation"
      MTMathAtomType.KMTMathAtomPlaceholder -> "Placeholder"
      MTMathAtomType.KMTMathAtomLargeOperator -> "Large Operator"
      MTMathAtomType.KMTMathAtomInner -> "Inner"
      MTMathAtomType.KMTMathAtomUnderline -> "Underline"
      MTMathAtomType.KMTMathAtomOverline -> "Overline"
      MTMathAtomType.KMTMathAtomAccent -> "Accent"
      MTMathAtomType.KMTMathAtomBoxed -> "Boxed"
      MTMathAtomType.KMTMathAtomBoundary -> "Boundary"
      MTMathAtomType.KMTMathAtomSpace -> "Space"
      MTMathAtomType.KMTMathAtomStyle -> "Style"
      MTMathAtomType.KMTMathAtomColor -> "Color"
      MTMathAtomType.KMTMathAtomTextColor -> "TextColor"
      MTMathAtomType.KMTMathAtomTable -> "Table"
    }

    /*
      Some types have special classes instead of MTMathAtom. Based on the type create the correct class
     */
    fun atomWithType(type: MTMathAtomType, value: String): MTMathAtom = when (type) {
      // Default setting of rule is true
      MTMathAtomType.KMTMathAtomFraction -> MTFraction(true)
      // A placeholder is created with a white square.
      MTMathAtomType.KMTMathAtomPlaceholder -> MTMathAtom(type, "\u25A1")
      MTMathAtomType.KMTMathAtomRadical -> MTRadical()
      // Default setting of limits is true
      MTMathAtomType.KMTMathAtomLargeOperator -> MTLargeOperator(value, true)
      MTMathAtomType.KMTMathAtomInner -> MTInner()
      MTMathAtomType.KMTMathAtomOverline -> MTOverLine()
      MTMathAtomType.KMTMathAtomUnderline -> MTUnderLine()
      MTMathAtomType.KMTMathAtomAccent -> MTAccent(value)
      MTMathAtomType.KMTMathAtomBoxed -> MTBoxed()
      MTMathAtomType.KMTMathAtomSpace -> MTMathSpace(0f)
      MTMathAtomType.KMTMathAtomColor -> MTMathColor()
      else -> MTMathAtom(type, value)
    }

    fun atomForCharacter(ch: Char): MTMathAtom? {
      val chStr = ch.toString()

      return when {
        // skip non-ascii characters and spaces
        ch.code < 0x21 || ch.code > 0x7E -> null

        // These are latex control characters that have special meanings. We don't support them.
        ch in setOf('$', '%', '#', '&', '~') -> null

        // Handle prime symbol for derivatives
        ch == '\'' -> atomWithType(MTMathAtomType.KMTMathAtomOrdinary, "\u2032")

        // more special characters for Latex.
        ch in setOf('^', '_', '{', '}', '\\') -> null

        // Open brackets
        ch in setOf('(', '[') -> atomWithType(MTMathAtomType.KMTMathAtomOpen, chStr)

        // Close brackets
        ch in setOf(')', ']', '!', '?') -> atomWithType(MTMathAtomType.KMTMathAtomClose, chStr)

        // Punctuation
        ch in setOf(',', ';') -> atomWithType(MTMathAtomType.KMTMathAtomPunctuation, chStr)

        // Relations
        ch in setOf('=', '>', '<') -> atomWithType(MTMathAtomType.KMTMathAtomRelation, chStr)

        // Math colon is ratio. Regular colon is \colon
        ch == ':' -> atomWithType(MTMathAtomType.KMTMathAtomRelation, "\u2236")

        // Use the math minus sign
        ch == '-' -> atomWithType(MTMathAtomType.KMTMathAtomBinaryOperator, "\u2212")

        // Binary operators
        ch in setOf('+', '*') -> atomWithType(MTMathAtomType.KMTMathAtomBinaryOperator, chStr)

        // Numbers
        ch == '.' || ch in '0'..'9' -> atomWithType(MTMathAtomType.KMTMathAtomNumber, chStr)

        // Variables
        ch in 'a'..'z' || ch in 'A'..'Z' -> atomWithType(MTMathAtomType.KMTMathAtomVariable, chStr)

        // Just an ordinary character. The following are allowed ordinary chars: | / ` @ "
        ch in setOf('"', '/', '@', '`', '|') -> atomWithType(
          MTMathAtomType.KMTMathAtomOrdinary,
          chStr,
        )

        else -> throw MathDisplayException("Unknown ascii character $ch. Should have been accounted for.")
        }
    }
  }


  open fun toLatexString(): String {
    var str = nucleus

    str = toStringSubs(str)
    return str
  }

  fun toStringSubs(s: String): String = buildString {
    append(s)

    superScript?.let {
      append("^{${MTMathListBuilder.toLatexString(it)}}")
    }

    subScript?.let {
      append("_{${MTMathListBuilder.toLatexString(it)}}")
    }
  }


  fun copyDeepContent(atom: MTMathAtom): MTMathAtom {
    if (this.subScript != null) {
      atom.subScript = this.subScript?.copyDeep()
    }
    if (this.superScript != null) {
      atom.superScript = this.superScript?.copyDeep()
    }
    // fusedAtoms are only used in preprocessing which comes after finalized which uses copyDeep()
    // No need to copy fusedAtoms but assert here to find any coding error
    assert(atom.fusedAtoms.isEmpty())
    atom.fontStyle = this.fontStyle
    atom.indexRange = this.indexRange.copy()
    return atom
  }

  open fun copyDeep(): MTMathAtom = MTMathAtom(this.type, this.nucleus).apply {
    copyDeepContent(this)
  }

  fun finalized(newNode: MTMathAtom): MTMathAtom {
    if (this.superScript != null) {
      newNode.superScript = newNode.superScript?.finalized()
    }
    if (this.subScript != null) {
      newNode.subScript = newNode.subScript?.finalized()
    }
    newNode.fontStyle = this.fontStyle
    newNode.indexRange = this.indexRange.copy()
    return newNode
  }

  open fun finalized(): MTMathAtom {
    val atom = this.copyDeep()
    return finalized(atom)
  }


  /** Returns true if this atom allows scripts (sub or super). */

  fun scriptsAllowed(): Boolean = this.type < MTMathAtomType.KMTMathAtomBoundary


  fun description(): String = typeToText(this.type) + " " + this

  /// Fuse the given atom with this one by combining their nucleii.
  fun fuse(atom: MTMathAtom) {
    if (this.subScript != null) throw MathDisplayException("Cannot fuse into an atom which has a subscript: $this")
    if (this.superScript != null) throw MathDisplayException("Cannot fuse into an atom which has a superscript: $this")
    if (this.type != atom.type) throw MathDisplayException("Only atoms of the same type can be fused: $this $atom")

    // Update the fused atoms list
    if (this.fusedAtoms.isEmpty()) {
      this.fusedAtoms.add(this.copyDeep())
    }
    if (atom.fusedAtoms.isNotEmpty()) {
      this.fusedAtoms.addAll(atom.fusedAtoms.toTypedArray())
    } else {
      this.fusedAtoms.add(atom)
    }

    // Update the nucleus
    this.nucleus += atom.nucleus

    // Update the range
    this.indexRange.length += atom.indexRange.length

    // Update super/sub scripts
    this.subScript = atom.subScript
    this.superScript = atom.superScript
  }
}

/**
@typedef MTLineStyle
@brief Styling of a line of math
 */
enum class MTLineStyle {
  /// Display style
  KMTLineStyleDisplay,

  /// Text style (inline)
  KMTLineStyleText,

  /// Script style (for sub/super scripts)
  KMTLineStyleScript,

  /// Script script style (for scripts of scripts)
  KMTLineStyleScriptScript
}
