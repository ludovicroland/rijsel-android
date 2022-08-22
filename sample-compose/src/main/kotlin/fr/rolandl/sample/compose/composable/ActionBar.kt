package fr.rolandl.sample.compose.composable

import androidx.annotation.StringRes
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

/**
 * @author Ludovic Roland
 * @since 2022.04.13
 */
@Composable
fun ActionBar(@StringRes titleRes: Int, icon: ImageVector, block: () -> Unit)
{
  TopAppBar(
    title = {
      Text(
        text = stringResource(titleRes),
      )
    },
    navigationIcon = {
      IconButton(
        onClick = { block() }
      ) {
        Icon(
          icon,
          contentDescription = null,
        )
      }
    }
  )
}
