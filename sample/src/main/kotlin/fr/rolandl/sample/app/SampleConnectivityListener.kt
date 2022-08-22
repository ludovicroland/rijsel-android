package fr.rolandl.sample.app

import android.content.Context
import fr.rolandl.rijsel.app.RijselApplication
import fr.rolandl.rijsel.app.RijselConnectivityListener

/**
 * @author Ludovic Roland
 * @since 2018.11.07
 */
class SampleConnectivityListener(context: Context)
  : RijselConnectivityListener(context)
{

  override fun notifyServices(hasConnectivity: Boolean)
  {
    if (RijselApplication.isOnCreatedDone == false)
    {
      return
    }
  }

}