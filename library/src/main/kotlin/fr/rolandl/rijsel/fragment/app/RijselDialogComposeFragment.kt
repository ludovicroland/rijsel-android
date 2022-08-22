package fr.rolandl.rijsel.fragment.app

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.*
import fr.rolandl.rijsel.app.Rijselable
import fr.rolandl.rijsel.app.Rijselizer
import fr.rolandl.rijsel.content.RijselSharedFlowListener
import fr.rolandl.rijsel.lifecycle.RijselComposeViewModel
import fr.rolandl.rijsel.lifecycle.RijselViewModelFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * A basis class for designing an Android compatibility library [DialogFragment] compatible with the framework, i.e.
 * Rijsel ready.
 * <p>
 * This implementation use a [RijselComposeViewModel] in order to implement the databinding and the MVVM architecture
 * </p>
 *
 * @param AggregateClass the aggregate class accessible though the [setAggregate] and [getAggregate] methods
 *
 * @author Ludovic Roland
 * @since 2022.04.12
 */
abstract class RijselDialogComposeFragment<AggregateClass : RijselComposeFragmentAggregate, ViewModelClass : RijselComposeViewModel>
  : DialogFragment(),
    Rijselable<AggregateClass>, RijselComposeFragmentConfigurable
{

  private var rijselizer: Rijselizer<AggregateClass, RijselDialogComposeFragment<AggregateClass, ViewModelClass>>? = null

  protected var viewModel: ViewModelClass? = null

  @Composable
  protected abstract fun Body()

  protected open fun getViewModelClass(): Class<ViewModelClass>? =
      null

  protected open fun getDispatcher(): CoroutineDispatcher =
      Dispatchers.IO

  @CallSuper
  override fun onAttach(context: Context)
  {
    super.onAttach(context)

    activity?.let {
      rijselizer = Rijselizer(it, this, this, this, lifecycleScope)
    }
  }

  @CallSuper
  override fun onCreate(savedInstanceState: Bundle?)
  {
    rijselizer?.onCreate({
      super@RijselDialogComposeFragment.onCreate(savedInstanceState)
    }, savedInstanceState)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
  {
    createViewModel()
    observeStates()
    computeViewModel()

    return ComposeView(requireContext()).apply {
      setContent {
        Body()
      }
    }
  }

  @CallSuper
  override fun onViewCreated(view: View, savedInstanceState: Bundle?)
  {
    super.onViewCreated(view, savedInstanceState)
    rijselizer?.onViewCreated()
  }

  protected open fun createViewModel()
  {
    val viewModelOwner: ViewModelStoreOwner = if (viewModelContext() == RijselComposeFragmentConfigurable.ViewModelContext.Fragment) this@RijselDialogComposeFragment else requireActivity()

    getViewModelClass()?.let {
      if(arguments == null)
      {
        arguments = bundleOf()
      }

      arguments?.putSerializable(RijselComposeViewModel.RIJSEL_VIEWMODEL_BEHAVIOR_EXTRA, viewModelBehavior())
      viewModel = ViewModelProvider(viewModelOwner, createViewModelFactory())[it]
    }
  }

  protected open fun createViewModelFactory(): ViewModelProvider.Factory =
      RijselViewModelFactory(getDispatcher(), requireActivity().application, this, arguments)

  protected open fun observeStates()
  {
    viewLifecycleOwner.lifecycleScope.launch {
      viewModel?.stateManager?.state?.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)?.collect {
        Timber.d("The state ${it.javaClass.simpleName} has been observed into the fragment ${this@RijselDialogComposeFragment.javaClass.simpleName}")

        when (it) {
          is RijselComposeViewModel.StateManager.State.LoadingState -> onLoadingState()
          is RijselComposeViewModel.StateManager.State.LoadedState  -> onLoadedState()
          is RijselComposeViewModel.StateManager.State.ErrorState   -> onErrorState()
        }
      }
    }
  }

  @CallSuper
  protected open fun computeViewModel()
  {
    viewModel?.computeViewModelInternal()
  }

  @CallSuper
  override fun onStart()
  {
    super.onStart()
    rijselizer?.onStart()
  }

  @CallSuper
  override fun onResume()
  {
    super.onResume()
    rijselizer?.onResume()
  }

  @CallSuper
  override fun onPause()
  {
    try
    {
      rijselizer?.onPause()
    }
    finally
    {
      super.onPause()
    }
  }

  @CallSuper
  override fun onStop()
  {
    try
    {
      rijselizer?.onStop()
    }
    finally
    {
      super.onStop()
    }
  }

  @CallSuper
  override fun onDestroy()
  {
    try
    {
      rijselizer?.onDestroy()
    }
    finally
    {
      super.onDestroy()
    }
  }

  override fun getAggregate(): AggregateClass? =
      rijselizer?.getAggregate()

  override fun setAggregate(aggregate: AggregateClass?)
  {
    rijselizer?.setAggregate(aggregate)
  }

  override fun registerRijselSharedFlowListener(rijselSharedFlowListener: RijselSharedFlowListener)
  {
    rijselizer?.registerRijselSharedFlowListener(rijselSharedFlowListener)
  }

  protected open fun onErrorState()
  {

  }

  @CallSuper
  protected open fun onLoadedState()
  {

  }

  protected open fun onLoadingState()
  {

  }

  /*Life cycle part. Not used in Fragments*/
  final override fun onRetrieveDisplayObjects()
  {
  }

  final override suspend fun onRetrieveModel()
  {
  }

  final override fun onBindModel()
  {
  }

  final override fun refreshModelAndBind(retrieveModel: Boolean, onOver: Runnable?, immediately: Boolean)
  {
  }

  override fun shouldKeepOn(): Boolean =
      rijselizer?.shouldKeepOn() ?: true

  override fun isFirstLifeCycle(): Boolean =
      rijselizer?.isFirstLifeCycle() ?: true

  override fun isInteracting(): Boolean =
      rijselizer?.isInteracting() ?: true

  override fun isAlive(): Boolean =
      rijselizer?.isAlive() ?: true

  override fun isRefreshingModelAndBinding(): Boolean =
      rijselizer?.isRefreshingModelAndBinding() ?: false

}
