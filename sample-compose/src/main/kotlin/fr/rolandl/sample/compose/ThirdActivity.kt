package fr.rolandl.sample.compose

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.rolandl.sample.compose.composable.DefaultBody
import fr.rolandl.sample.compose.viewmodel.activity.ThirdActivityViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * @author Ludovic Roland
 * @since 2022.04.04
 */
class ThirdActivity
  : SampleActivity<ThirdActivityViewModel>()
{

  companion object
  {

    const val MY_EXTRA = "myExtra"

    const val ANOTHER_EXTRA = "anotherExtra"

  }

  override fun getViewModelClass(): Class<ThirdActivityViewModel> =
    ThirdActivityViewModel::class.java

  override fun canRotate(): Boolean =
      true

  @Preview
  @Composable
  override fun Body()
  {
    DefaultBody(
      viewModel = viewModel,
      titleRes = R.string.app_name,
      icon = Icons.Filled.ArrowBack,
      onIconClicked = { onBackPressed() },
      content = {
        Content()
      }
    )
  }

  @Preview
  @Composable
  private fun Content()
  {
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    LaunchedEffect(true) {
      scope.launch {
        navController.currentBackStackEntryFlow.collect {
          Timber.d("current backstack entry: ${it.destination.route}")
        }
      }
    }

    NavHost(navController, startDestination = "third") {
      composable(route = "third") {
        ThirdContent(navController)
      }

      composable(route = "backstack") {
        Backstack()
      }
    }
  }

  @OptIn(ExperimentalLifecycleComposeApi::class)
  @Composable
  private fun ThirdContent(navController: NavController)
  {
    val theItems = viewModel?.persons?.collectAsStateWithLifecycle()?.value ?: listOf()

    Box(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(16.dp)
    ) {
      Column {
        Text(text = viewModel?.myString ?: "")
        Text(text = viewModel?.anotherString?.collectAsStateWithLifecycle()?.value ?: "")
        Text(text = stringResource(viewModel?.resString?.collectAsStateWithLifecycle()?.value ?: throw IllegalArgumentException()))
        Button(
          onClick = {
            viewModel?.refreshViewModel(true) {
              Toast.makeText(this@ThirdActivity, "Finish !", Toast.LENGTH_SHORT).show()
            }
          },
        ) {
          Text(text = "refresh loading")
        }
        Button(
          onClick = {
            viewModel?.refreshViewModel(false) {
              Toast.makeText(this@ThirdActivity, "Finish !", Toast.LENGTH_SHORT).show()
            }
          },
        ) {
          Text(text = "refresh no loading")
        }
        Button(
          onClick = {
            viewModel?.apply {
              throwError = true
              refreshViewModel(true) {
                Toast.makeText(this@ThirdActivity, "Finish !", Toast.LENGTH_SHORT).show()
              }
            }
          },
        ) {
          Text(text = "refresh with error")
        }
        Button(
          onClick = {
            viewModel?.apply {
              throwInternetError = true
              refreshViewModel(true) {
                Toast.makeText(this@ThirdActivity, "Finish !", Toast.LENGTH_SHORT).show()
              }
            }
          },
        ) {
          Text(text = "refresh with internet error")
        }
        Button(
          onClick = {
            viewModel?.updateStateFlow()
          },
        ) {
          Text(text = "MutableStateFlow field")
        }
        Button(
          onClick = {
            navController.navigate("backstack")
          },
        ) {
          Text(text = "Replace fragment with backstack")
        }
        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
        ) {
          items(theItems) {
            Text(it)
          }
        }
      }
    }
  }

  @Preview
  @Composable
  private fun Backstack()
  {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(16.dp)
    ) {
      Text(
        text = "Try to go back! I'am a composable in the backstack!",
        Modifier.background(colorResource(if(LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) android.R.color.holo_red_light else android.R.color.holo_green_light))
      )
    }
  }

}
