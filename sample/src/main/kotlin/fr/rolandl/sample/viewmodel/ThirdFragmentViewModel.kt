package fr.rolandl.sample.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import fr.rolandl.sample.R
import fr.rolandl.sample.fragment.ThirdFragment
import fr.rolandl.rijsel.lifecycle.ModelUnavailableException
import fr.rolandl.rijsel.lifecycle.RijselViewModel
import kotlinx.coroutines.delay
import java.net.UnknownHostException

/**
 * @author Ludovic Roland
 * @since 2019.03.21
 */
class ThirdFragmentViewModel(application: Application, savedStateHandle: SavedStateHandle)
  : RijselViewModel(application, savedStateHandle)
{

  var throwError = false

  var throwInternetError = false

  val persons = mutableListOf<String>()

  var myString: String? = null

  val anotherString = MutableLiveData<String>()

  val resString = MutableLiveData<Int>().apply {
    value = R.string.app_name
  }

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

    myString = savedStateHandle.get<String?>(ThirdFragment.MY_EXTRA)
    anotherString.postValue(savedStateHandle.get(ThirdFragment.ANOTHER_EXTRA))
    persons.addAll(Array(15) { "Person ${it + 1}" })
  }

}