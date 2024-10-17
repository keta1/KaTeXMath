package icu.ketal.katexmath

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Constraints
import icu.ketal.katexmath.modifiers.KatexMathElement

@Composable
fun KaTeXMath(
  latex: String,
  modifier: Modifier = Modifier,
  style: TextStyle = TextStyle.Default,
) {
  val finalModifier = modifier then KatexMathElement(
    latex = latex,
    style = style
  )
  Layout(finalModifier, EmptyMeasurePolicy)
}

private object EmptyMeasurePolicy : MeasurePolicy {
  private val placementBlock: Placeable.PlacementScope.() -> Unit = {}
  override fun MeasureScope.measure(
    measurables: List<Measurable>,
    constraints: Constraints
  ): MeasureResult {
    return layout(constraints.maxWidth, constraints.maxHeight, placementBlock = placementBlock)
  }
}
