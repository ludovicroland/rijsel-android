package fr.rolandl.sample.compose.viewmodel.activity

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import fr.rolandl.sample.compose.R
import fr.rolandl.sample.compose.ThirdActivity
import fr.rolandl.rijsel.lifecycle.ModelUnavailableException
import fr.rolandl.rijsel.lifecycle.RijselComposeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import java.util.*

/**
 * @author Ludovic Roland
 * @since 2022.04.04
 */
class ThirdActivityViewModel(application: Application, savedStateHandle: SavedStateHandle)
  : RijselComposeViewModel(application, savedStateHandle)
{

  var throwError = false

  var throwInternetError = false

  val persons = MutableStateFlow(listOf<String>())

  var myString: String? = null

  val anotherString = MutableStateFlow<String?>(null)

  val resString = MutableStateFlow(R.string.app_name)

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

    myString = savedStateHandle.get<String?>(ThirdActivity.MY_EXTRA)
    anotherString.value = savedStateHandle.get(ThirdActivity.ANOTHER_EXTRA)
    persons.value = Array((0..50).random()) { "Person ${it + 1}" }.toList()
  }

  fun updateStateFlow()
  {
    viewModelScope.launch(dispatcher) {
      anotherString.value = UUID.randomUUID().toString()
    }
  }

}
