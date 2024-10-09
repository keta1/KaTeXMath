package icu.ketal.katexmath.example

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
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
        ActionItem("Font")
        ActionItem("Mode")
        ActionItem("Color")
        ActionItem("Size")
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
      .verticalScroll(rememberScrollState())
  ) {
    lines.filter { it.isNotBlank() }.forEach { line ->
      if (line[0] == '#') {
        Text(
          text = line.substring(1),
          style = TextStyle(
            fontSize = 15.sp,
            color = Color.Gray
          )
        )
      } else {
        KaTeXMath(
          latex = line,
          style = style,
          modifier = Modifier.padding(vertical = 16.dp)
        )
      }
    }
  }
}

@Composable
private fun ActionItem(
  text: String,
  onClick: () -> Unit = {}
) {
  Text(
    text = text,
    style = TextStyle(
      fontSize = 16.sp,
      color = Color.White,
    ),
    modifier = Modifier
      .clickable(onClick = onClick)
      .padding(8.dp)
  )
}
