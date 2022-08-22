package fr.rolandl.sample.compose.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import fr.rolandl.sample.compose.theme.SampleTheme
import fr.rolandl.rijsel.lifecycle.RijselComposeViewModel

/**
 * @author Ludovic Roland
 * @since 2022.04.13
 */
@Composable
fun DefaultBody(@StringRes titleRes: Int, icon: ImageVector, onIconClicked: () -> Unit, content: @Composable () -> Unit, viewModel: RijselComposeViewModel?)
{
  SampleTheme {
    Scaffold (
      topBar = {
        ActionBar(
          titleRes = titleRes,
          icon = icon) {
          onIconClicked()
        }
      },
      content = {
        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
          content()

          viewModel?.let {
            LoadingErrorAndRetry(viewModel = it)
          }
        }
      }
    )
  }
}
