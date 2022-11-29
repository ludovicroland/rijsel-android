package fr.rolandl.rijsel.lifecycle

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import fr.rolandl.rijsel.R
import fr.rolandl.rijsel.app.RijselActivityController
import fr.rolandl.rijsel.fragment.app.RijselComposeFragmentConfigurable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.Serializable

/**
 * @author Ludovic Roland
 * @since 2021.12.21
 */
abstract class RijselComposeViewModel(application: Application, val savedStateHandle: SavedStateHandle, val dispatcher: CoroutineDispatcher = Dispatchers.IO)
  : AndroidViewModel(application)
{
  class StateManager(val coroutineScope: CoroutineScope)
    : Serializable
  {

    sealed class State
    {

      object LoadingState : State()

      object LoadedState : State()

      class ErrorState(val throwable: Throwable) : State()

    }

    val state = MutableStateFlow<State>(State.LoadingState)

    val errorMessage = MutableStateFlow(RijselActivityController.exceptionHandler?.getGenericErrorMessage() ?: R.string.rijsel_defaultErrorMessage)

    val isErrorAndLoadingViewVisible = state.map {
      if (it is State.LoadedState) false else true
    }.stateIn(coroutineScope, WhileSubscribed(5_000), true)

    val isLoadingViewVisible = state.map {
      if (it is State.ErrorState) false else true
    }.stateIn(coroutineScope, WhileSubscribed(5_000), true)

  }

  companion object
  {

    /**
     * Key used in order to add the extra that defined the [fr.rolandl.rijsel.fragment.app.RijselComposeFragmentConfigurable.ViewModelBehavior] of the ViewModel
     */
    const val RIJSEL_VIEWMODEL_BEHAVIOR_EXTRA = "RijselViewModelBehaviorExtra"

  }

  val stateManager = StateManager(viewModelScope)

  private var dataAlreadyLoaded = false

  abstract suspend fun computeViewModel()

  open fun computeViewModelInternal(displayLoadingState: Boolean = true, runnable: Runnable? = null)
  {
    Timber.d("computeViewModelInternal")

    viewModelScope.launch(dispatcher + CoroutineExceptionHandler { _, throwable ->
      viewModelScope.launch(Dispatchers.Main) {
        Timber.w(throwable, "An error occurred while computing the ViewModel")

        //TODO : log the error + update the message error

        val errorStringRes = RijselActivityController.handleException(true, throwable)

        stateManager.apply {
          state.value = StateManager.State.ErrorState(throwable)
          errorMessage.value = errorStringRes
        }
      }
    }) {
      if (dataAlreadyLoaded == false || savedStateHandle.get<RijselComposeFragmentConfigurable.ViewModelBehavior>(RijselComposeViewModel.RIJSEL_VIEWMODEL_BEHAVIOR_EXTRA) == RijselComposeFragmentConfigurable.ViewModelBehavior.Reload)
      {
        if (displayLoadingState == true)
        {
          stateManager.state.value = StateManager.State.LoadingState
        }

        computeViewModel()

        dataAlreadyLoaded = true
        stateManager.state.value = StateManager.State.LoadedState
      }

      withContext(Dispatchers.Main) {
        runnable?.run()
      }
    }
  }

  open fun refreshViewModel(displayLoadingState: Boolean = true, runnable: Runnable? = null)
  {
    dataAlreadyLoaded = false
    computeViewModelInternal(displayLoadingState, runnable)
  }

}