package fr.rolandl.rijsel.activity

import android.content.pm.ActivityInfo
import androidx.activity.ComponentActivity
import fr.rolandl.rijsel.app.RijselApplication

/**
 * The basis class for all Compose Activity Aggregate available in the framework.
 *
 * @author Ludovic Roland
 * @since 2022.04.11
 */
abstract class RijselComposeActivityAggregate(val activity: ComponentActivity, val activityConfigurable: RijselComposeActivityConfigurable?)
{

  fun onCreate()
  {
    if (activityConfigurable?.canRotate() == false && RijselApplication.getApplicationConstants<RijselApplication.ApplicationConstants>().canRotate == false)
    {
      // This Activity is not authorized to rotate
      val requestedOrientation = activity.requestedOrientation

      if (requestedOrientation != ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT)
      {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
      }
    }
  }

}
