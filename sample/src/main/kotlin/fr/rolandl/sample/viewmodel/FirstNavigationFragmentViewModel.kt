package fr.rolandl.sample.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import fr.rolandl.rijsel.content.LocalSharedFlowManager
import fr.rolandl.rijsel.lifecycle.RijselViewModel
import timber.log.Timber
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * @author Ludovic Roland
 * @since 2022/03/22
 */
class FirstNavigationFragmentViewModel(application: Application, savedStateHandle: SavedStateHandle)
  : RijselViewModel(application, savedStateHandle)
{

  var fragmentHostIdentityHashcode = ""

  private val scheduler = Executors.newScheduledThreadPool(1)

  val counter = MutableLiveData(0)

  override suspend fun computeViewModel()
  {
    scheduler.scheduleWithFixedDelay({
      Timber.d("Emitted by $fragmentHostIdentityHashcode")
      LocalSharedFlowManager.emit(viewModelScope, Intent("ACTION").apply {
        addCategory(fragmentHostIdentityHashcode)
      })
    }, 0, 1_000, TimeUnit.MILLISECONDS)
  }

  fun incrementCounter()
  {
    counter.value = counter.value?.plus(1)
  }
}