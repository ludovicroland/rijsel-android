package fr.rolandl.sample

import androidx.appcompat.app.AppCompatActivity
import fr.rolandl.sample.app.SampleActivityAggregate
import fr.rolandl.sample.databinding.ActivitySplashscreenBinding
import fr.rolandl.rijsel.appcompat.app.RijselSplashscreenActivity
import kotlin.reflect.KClass

/**
 * @author Ludovic Roland
 * @since 2018.11.08
 */
class SampleSplashscreenActivity
  : RijselSplashscreenActivity<SampleActivityAggregate, ActivitySplashscreenBinding>()
{

  override fun inflateViewBinding(): ActivitySplashscreenBinding =
      ActivitySplashscreenBinding.inflate(layoutInflater)

  override fun getNextActivity(): KClass<out AppCompatActivity> =
      MainActivity::class

  override fun onRetrieveModelCustom()
  {
    Thread.sleep(1_000)
  }

}