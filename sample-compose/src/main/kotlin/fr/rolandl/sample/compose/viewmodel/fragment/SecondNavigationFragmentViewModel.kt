package fr.rolandl.sample.compose.viewmodel.fragment

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import fr.rolandl.sample.compose.bo.ListItem
import fr.rolandl.rijsel.lifecycle.RijselComposeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * @author Ludovic Roland
 * @since 2022.04.14
 */
class SecondNavigationFragmentViewModel(application: Application, savedStateHandle: SavedStateHandle)
  : RijselComposeViewModel(application, savedStateHandle)
{

  var fragmentHostIdentityHashcode = ""

  val counter = MutableStateFlow(0)

  val items = MutableStateFlow<List<ListItem>>(listOf())

  override suspend fun computeViewModel()
  {
    Timber.d(fragmentHostIdentityHashcode)

    populateList()
  }

  fun incrementCounter()
  {
    counter.value = counter.value.plus(1)
  }

  private fun populateList()
  {
    viewModelScope.launch(dispatcher) {
      delay(1_000)

      val items = mutableListOf<ListItem>()

      for (i in 0..10)
      {
        items.add(ListItem("$i", fragmentHostIdentityHashcode))
      }

      this@SecondNavigationFragmentViewModel.items.value = items
    }
  }

}
