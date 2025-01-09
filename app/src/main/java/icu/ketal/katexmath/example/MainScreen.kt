package icu.ketal.katexmath.example

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import icu.ketal.katexmath.KaTeXMath
import icu.ketal.katexmath.example.ui.theme.Purple40

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun MainScreen(
  viewModel: MainViewModel = viewModel()
) = Scaffold(
  topBar = {
    TopAppBar(
      colors = TopAppBarDefaults.topAppBarColors(
        containerColor = Purple40,
        titleContentColor = Color.White,
      ),
      title = {
        Text("KaTeXMath")
      },
      actions = {
        ToolbarActions()
      },
    )
  },
) { innerPadding ->
  val context = LocalContext.current
  val uiState by viewModel.uiState.collectAsState()
  val style = TextStyle(
    fontSize = uiState.size,
    color = uiState.color,
  )
  val lines = remember {
    context.resources.openRawResource(R.raw.samples).bufferedReader().use {
      it.readLines()
    }
  }
  Column(
    modifier = Modifier
      .padding(innerPadding)
      .padding(horizontal = 16.dp)
      .verticalScroll(rememberScrollState()),
  ) {
    lines.filter { it.isNotBlank() }.forEach { line ->
      if (line[0] == '#') {
        Text(
          text = line.substring(1),
          style = TextStyle(
            fontSize = 15.sp,
            color = Color.Gray,
          ),
        )
      } else {
        KaTeXMath(
          latex = line,
          style = style,
          modifier = Modifier.padding(vertical = 16.dp).border(1.dp, Color.Red),
        )
      }
    }
  }
}

@Composable
private fun ToolbarActions() {
  var display by remember { mutableStateOf(false) }
  var items by remember { mutableStateOf(listOf<String>()) }
  var offsetX by remember { mutableStateOf(0.dp) }
  ActionItem(
    text = "Font",
  ) {
    offsetX = it
    display = true
    items = listOf(
      "Latin Modern Math",
      "TeX Gyre Termes",
      "XITS Math"
    )
  }
  ActionItem(
    text = "Mode",
  ){
    offsetX = it
    display = true
    items = listOf("Display", "Text")
  }
  ActionItem("Color"){
    offsetX = it
    display = true
    items = listOf("Black", "Purple")
  }
  ActionItem("Size") {
    offsetX = it
    display = true
    items = listOf("12dp", "14dp", "20dp", "40dp")
  }
  DropdownMenu(
    offset= DpOffset(offsetX, 0.dp),
    expanded = display,
    onDismissRequest = {
      display = false
    },
  ) {
    items.forEachIndexed { index, text ->
      DropdownMenuItem(
        text = {
          Row(
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(text = text)
            Spacer(modifier = Modifier.weight(1f))
            Checkbox(
              checked = false,
              onCheckedChange = null,
              modifier = Modifier.padding(8.dp),
            )
          }
        },
        onClick = {

        },
      )
    }
  }
}

@Composable
private fun ActionItem(
  text: String,
  onClick: (offsetX: Dp) -> Unit = {},
) {
  var offsetX by remember { mutableStateOf(0.dp) }
  val density = LocalDensity.current
  Text(
    text = text,
    style = TextStyle(
      fontSize = 16.sp,
      color = Color.White,
    ),
    modifier = Modifier.onGloballyPositioned { layoutCoordinates ->
        val position = layoutCoordinates.positionInParent()
        offsetX = with(density) { position.x.toDp() }
      }
      .clickable(onClick = { onClick(offsetX) })
      .padding(8.dp),
  )
}
