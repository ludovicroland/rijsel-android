package fr.rolandl.sample.compose.viewmodel.fragment

import android.app.Application
import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import fr.rolandl.rijsel.content.LocalSharedFlowManager
import fr.rolandl.rijsel.lifecycle.RijselComposeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * @author Ludovic Roland
 * @since 2022.04.14
 */
class FirstNavigationFragmentViewModel(application: Application, savedStateHandle: SavedStateHandle)
  : RijselComposeViewModel(application, savedStateHandle)
{

  var fragmentHostIdentityHashcode = ""

  private val scheduler = Executors.newScheduledThreadPool(1)

  val counter = MutableStateFlow(0)

  override suspend fun computeViewModel()
  {
    scheduler.scheduleWithFixedDelay({
      LocalSharedFlowManager.emit(viewModelScope, Intent("ACTION").apply {
        addCategory(fragmentHostIdentityHashcode)
      })
    }, 0, 1_000, TimeUnit.MILLISECONDS)
  }

  fun incrementCounter()
  {
    counter.value = counter.value.plus(1)
  }

}
