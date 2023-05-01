package fr.rolandl.sample.compose.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.rolandl.rijsel.lifecycle.RijselComposeViewModel
import fr.rolandl.sample.compose.theme.Purple600

/**
 * @author Ludovic Roland
 * @since 2022.04.13
 */
@Composable
fun LoadingErrorAndRetry(viewModel: RijselComposeViewModel)
{
  val visibility by viewModel.stateManager.isErrorAndLoadingViewVisible.collectAsStateWithLifecycle()

  AnimatedVisibility(
    visible = visibility,
    enter = fadeIn(),
    exit = fadeOut()
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .background(MaterialTheme.colors.background)
    ) {
      ErrorAndRetry(viewModel)
      Loading(viewModel.stateManager)
    }
  }
}

@Composable
fun ErrorAndRetry(viewModel: RijselComposeViewModel)
{
  val errorMessage by viewModel.stateManager.errorMessage.collectAsStateWithLifecycle()

  ConstraintLayout(modifier = Modifier
    .fillMaxHeight()
    .fillMaxWidth()
    .padding(16.dp)
    .background(MaterialTheme.colors.background)
  ) {
    val (text, retry) = createRefs()

    Text(
      text = stringResource(errorMessage),
      style = MaterialTheme.typography.body1,
      modifier = Modifier
        .constrainAs(text) {
          top.linkTo(parent.top)
          bottom.linkTo(retry.top)
          start.linkTo(parent.start)
          end.linkTo(parent.end)
        }
        .padding(bottom = 40.dp)
    )

    Button(
      onClick = { viewModel.refreshViewModel(true) },
      contentPadding = PaddingValues(10.dp),
      modifier = Modifier
        .constrainAs(retry) {
          bottom.linkTo(parent.bottom)
          top.linkTo(text.bottom)
          start.linkTo(parent.start)
          end.linkTo(parent.end)
        }
        .width(250.dp)
        .padding(10.dp)
    ) {
      Text(
        text = stringResource(fr.rolandl.sample.compose.R.string.retry),
      )
    }

    createVerticalChain(
      text,
      retry,
      chainStyle = ChainStyle.Packed
    )
  }
}

@Composable
fun Loading(stateManager: RijselComposeViewModel.StateManager)
{
  val visibility by stateManager.isLoadingViewVisible.collectAsStateWithLifecycle()

  AnimatedVisibility(
      visible = visibility,
      enter = fadeIn(),
      exit = fadeOut()
  ) {
    ConstraintLayout(modifier = Modifier
      .fillMaxHeight()
      .fillMaxWidth()
      .padding(16.dp)
      .background(MaterialTheme.colors.background)
    ) {
      val (progress, text) = createRefs()

      CircularProgressIndicator(
          color = Purple600,
          modifier = Modifier.constrainAs(progress) {
            top.linkTo(parent.top)
            bottom.linkTo(text.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
          },
      )

      Text(
        text = stringResource(fr.rolandl.sample.compose.R.string.loading),
        style = MaterialTheme.typography.body1,
        modifier = Modifier
          .constrainAs(text) {
            bottom.linkTo(parent.bottom)
            top.linkTo(progress.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
          }
          .padding(10.dp)
      )

      createVerticalChain(
          progress,
          text,
          chainStyle = ChainStyle.Packed
      )
    }
  }
}
