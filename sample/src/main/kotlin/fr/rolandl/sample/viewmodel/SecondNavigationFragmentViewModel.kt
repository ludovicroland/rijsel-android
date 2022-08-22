package fr.rolandl.sample.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import fr.rolandl.sample.item.ListItem
import fr.rolandl.rijsel.lifecycle.RijselViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.Executors

/**
 * @author Ludovic Roland
 * @since 2022/03/22
 */
class SecondNavigationFragmentViewModel(application: Application, savedStateHandle: SavedStateHandle)
  : RijselViewModel(application, savedStateHandle)
{

  var fragmentHostIdentityHashcode = ""

  private val scheduler = Executors.newScheduledThreadPool(1)

  val counter = MutableLiveData(0)

  val items = MutableLiveData<List<ListItem>>()

  override suspend fun computeViewModel()
  {
    Timber.d(fragmentHostIdentityHashcode)

    populateList()
/*    scheduler.scheduleWithFixedDelay({
      LocalSharedFlowManager.emit(viewModelScope, Intent("ACTION").apply {
        addCategory(fragmentHostIdentityHashcode)
      })
    }, 0, 1_000, TimeUnit.MILLISECONDS)*/
  }

  fun incrementCounter()
  {
    counter.value = counter.value?.plus(1)
  }

  private fun populateList()
  {
    viewModelScope.launch(dispatcher) {
      delay(1_000)

      val items = mutableListOf<ListItem>()

      for (i in 0..10)
      {
        items.add(ListItem(ListItemModel("$i", fragmentHostIdentityHashcode)))
      }

      this@SecondNavigationFragmentViewModel.items.postValue(items)
    }
  }
}