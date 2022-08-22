package fr.rolandl.rijsel.lifecycle

import android.app.Application
import androidx.lifecycle.SavedStateHandle

/**
 * A default RijselViewModel implementation in order to provide a default value to the
 * [fr.rolandl.rijsel.fragment.app.RijselFragmentConfigurable] interface
 *
 * @author Ludovic Roland
 * @since 2018.12.05
 */
class DummyRijselViewModel(application: Application, savedStateHandle: SavedStateHandle)
  : RijselViewModel(application, savedStateHandle)
{

  override suspend fun computeViewModel()
  {

  }

}
