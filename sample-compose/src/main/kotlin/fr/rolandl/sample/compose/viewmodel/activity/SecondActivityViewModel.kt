package fr.rolandl.sample.compose.viewmodel.activity

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import fr.rolandl.sample.compose.SecondActivity
import fr.rolandl.rijsel.lifecycle.ModelUnavailableException
import fr.rolandl.rijsel.lifecycle.RijselComposeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import java.util.*

/**
 * @author Ludovic Roland
 * @since 2018.11.09
 */
class SecondActivityViewModel(application: Application, savedStateHandle: SavedStateHandle)
  : RijselComposeViewModel(application, savedStateHandle)
{

  var myString: String? = null

  val anotherString = MutableStateFlow<String?>(null)

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

    myString = savedStateHandle.get<String?>(SecondActivity.MY_EXTRA)
    anotherString.value = savedStateHandle.get(SecondActivity.ANOTHER_EXTRA)
  }

  fun updateStateFlow()
  {
    viewModelScope.launch(dispatcher) {
      anotherString.value = UUID.randomUUID().toString()
    }
  }

}
