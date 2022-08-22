package fr.rolandl.rijsel.lifecycle

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import fr.rolandl.rijsel.R
import fr.rolandl.rijsel.app.RijselActivityController
import fr.rolandl.rijsel.fragment.app.RijselFragmentConfigurable
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.Serializable

/**
 * The basis class for all [ViewModel] available in the framework.
 *
 * @author Ludovic Roland
 * @since 2018.12.05
 */
abstract class RijselViewModel(application: Application, val savedStateHandle: SavedStateHandle, val dispatcher: CoroutineDispatcher = Dispatchers.IO)
  : AndroidViewModel(application)
{

  class StateManager
    : Serializable
  {

    sealed class State
    {

      object LoadingState : State()

      object LoadedState : State()

      class ErrorState(val throwable: Throwable) : State()

    }

    val state = MutableLiveData<State>()

    val errorMessage = MutableLiveData(RijselActivityController.exceptionHandler?.getGenericErrorMessage() ?: R.string.rijsel_defaultErrorMessage)

    val errorAndLoadingViewVisibility = state.map {
      if (it is State.LoadedState) View.INVISIBLE else View.VISIBLE
    }

    val loadingViewVisibility = state.map {
      if (it is State.ErrorState) View.INVISIBLE else View.VISIBLE
    }

  }

  companion object
  {

    /**
     * Key used in order to add the extra that defined the [fr.rolandl.rijsel.fragment.app.RijselFragmentConfigurable.ViewModelBehavior] of the ViewModel
     */
    const val RIJSEL_VIEWMODEL_BEHAVIOR_EXTRA = "RijselViewModelBehaviorExtra"

  }

  val stateManager = StateManager()

  private var dataAlreadyLoaded = false

  abstract suspend fun computeViewModel()

  open fun computeViewModelInternal(displayLoadingState: Boolean = true, runnable: Runnable? = null)
  {
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
      if (dataAlreadyLoaded == false || savedStateHandle.get<RijselFragmentConfigurable.ViewModelBehavior>(RijselViewModel.RIJSEL_VIEWMODEL_BEHAVIOR_EXTRA) == RijselFragmentConfigurable.ViewModelBehavior.Reload)
      {
        if (displayLoadingState == true)
        {
          stateManager.state.postValue(StateManager.State.LoadingState)
        }

        computeViewModel()

        dataAlreadyLoaded = true
        stateManager.state.postValue(StateManager.State.LoadedState)
      }

      withContext(Dispatchers.Main) {
        runnable?.run()
      }
    }
  }

  /**
   * Force the refresh of the ViewModel using the [computeViewModel] method
   */
  open fun refreshViewModel(displayLoadingState: Boolean = true, runnable: Runnable? = null)
  {
    dataAlreadyLoaded = false
    computeViewModelInternal(displayLoadingState, runnable)
  }

}
