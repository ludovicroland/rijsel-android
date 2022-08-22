package fr.rolandl.sample.compose.viewmodel.activity

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import fr.rolandl.rijsel.lifecycle.ModelUnavailableException
import fr.rolandl.rijsel.lifecycle.RijselComposeViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.UnknownHostException

/**
 * @author Ludovic Roland
 * @since 2022.04.13
 */
class MainActivityViewModel(application: Application, savedStateHandle: SavedStateHandle, dispatcher: CoroutineDispatcher = Dispatchers.IO)
  : RijselComposeViewModel(application, savedStateHandle, dispatcher)
{

  var count = 0

  var throwError = false

  var throwInternetError = false

  override suspend fun computeViewModel()
  {
    delay(1_000)

    if (throwError == true)
    {
      throwError = false

      throw ModelUnavailableException("Cannot retrieve the model")
    }

    if (throwInternetError == true)
    {
      throwInternetError = false

      throw ModelUnavailableException("Cannot retrieve the model", UnknownHostException())
    }
  }

  fun count(times: Int)
  {
    viewModelScope.launch(dispatcher) {
      for (i in 1..times)
      {
        count++
      }
    }
  }

}