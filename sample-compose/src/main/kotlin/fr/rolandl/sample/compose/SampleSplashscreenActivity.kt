package fr.rolandl.sample.compose

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import fr.rolandl.sample.compose.app.SampleActivityAggregate
import fr.rolandl.sample.compose.theme.Purple700
import fr.rolandl.sample.compose.theme.SampleTheme
import fr.rolandl.rijsel.activity.RijselComposeSplashscreenActivity
import kotlin.reflect.KClass

/**
 * @author Ludovic Roland
 * @since 2022.04.12
 */
class SampleSplashscreenActivity
  : RijselComposeSplashscreenActivity<SampleActivityAggregate>()
{

  override fun getNextActivity(): KClass<out ComponentActivity> =
      MainActivity::class

  override fun onRetrieveModelCustom()
  {
    Thread.sleep(1_000)
  }

  @Preview
  @Composable
  override fun Body()
  {
    SampleTheme {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .fillMaxHeight()
          .background(colorResource(android.R.color.holo_blue_bright))
      ) {
        CircularProgressIndicator(
          color = Purple700,
          modifier = Modifier.align(Alignment.Center)
        )
      }
    }
  }

}
