package fr.rolandl.sample.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import fr.rolandl.sample.fragment.SecondFragment
import fr.rolandl.rijsel.lifecycle.ModelUnavailableException
import fr.rolandl.rijsel.lifecycle.RijselViewModel
import kotlinx.coroutines.delay
import java.net.UnknownHostException

/**
 * @author Ludovic Roland
 * @since 2018.11.09
 */
class SecondFragmentViewModel(application: Application, savedStateHandle: SavedStateHandle)
  : RijselViewModel(application, savedStateHandle)
{

  val myString = MutableLiveData<String>()

  val anotherString = MutableLiveData<String>()

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

    myString.postValue(savedStateHandle.get(SecondFragment.MY_EXTRA))
    anotherString.postValue(savedStateHandle.get(SecondFragment.ANOTHER_EXTRA))
  }

}
