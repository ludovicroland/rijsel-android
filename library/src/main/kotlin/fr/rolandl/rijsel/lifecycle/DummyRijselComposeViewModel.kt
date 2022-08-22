package fr.rolandl.rijsel.lifecycle

import android.app.Application
import androidx.lifecycle.SavedStateHandle

/**
 * A default RijselViewModel implementation in order to provide a default value to the
 * [fr.rolandl.rijsel.activity.RijselComposeSplashscreenActivity] activity
 *
 * @author Ludovic Roland
 * @since 2022.04.11
 */
class DummyRijselComposeViewModel(application: Application, savedStateHandle: SavedStateHandle)
  : RijselComposeViewModel(application, savedStateHandle)
{

  override suspend fun computeViewModel()
  {

  }

}
