package icu.ketal.katexmath.modifiers

import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.text.TextStyle

internal class KatexMathElement(
  private val latex: String,
  private val style: TextStyle
) : ModifierNodeElement<KatexMathNode>() {
  override fun create(): KatexMathNode = KatexMathNode(
    latex = latex,
    style = style,

  )

  override fun equals(other: Any?): Boolean {
    if (this === other) return true

    if (other !is KatexMathElement) return false

    if (latex != other.latex) return false
    if (style != other.style) return false

    return true
  }

  override fun hashCode(): Int {
    var result = latex.hashCode()
    result = 31 * result + style.hashCode()
    return result
  }

  override fun update(node: KatexMathNode) {
    // todo
  }
}
