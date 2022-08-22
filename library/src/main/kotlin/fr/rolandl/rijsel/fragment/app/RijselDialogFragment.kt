package fr.rolandl.rijsel.fragment.app

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.OnRebindCallback
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import fr.rolandl.rijsel.app.Rijselable
import fr.rolandl.rijsel.app.Rijselizer
import fr.rolandl.rijsel.content.RijselSharedFlowListener
import fr.rolandl.rijsel.lifecycle.RijselViewModel
import fr.rolandl.rijsel.lifecycle.RijselViewModelFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

/**
 * A basis class for designing an Android compatibility library [Fragment] compatible with the framework, i.e.
 * Rijsel ready.
 * <p>
 * This implementation use a [RijselViewModel] in order to implement the databinding and the MVVM architecture
 * </p>
 *
 * @param AggregateClass the aggregate class accessible though the [setAggregate] and [getAggregate] methods
 * @param BindingClass the binding class in order to implement the databinding
 *
 * @author Ludovic Roland
 * @since 2018.11.07
 */
abstract class RijselDialogFragment<AggregateClass : RijselFragmentAggregate, BindingClass : ViewDataBinding, ViewModelClass : RijselViewModel>
  : DialogFragment(),
    Rijselable<AggregateClass>, RijselFragmentConfigurable
{

  private var rijselizer: Rijselizer<AggregateClass, RijselDialogFragment<AggregateClass, BindingClass, ViewModelClass>>? = null

  protected var viewDatabinding: BindingClass? = null

  protected var viewModel: ViewModelClass? = null

  private val onRebindCallback = object : OnRebindCallback<BindingClass>()
  {

    override fun onPreBind(binding: BindingClass): Boolean
    {
      return false
    }

  }

  protected abstract fun getViewModelClass(): Class<ViewModelClass>

  protected abstract fun getBindingVariable(): Int?

  protected open fun getDispatcher(): CoroutineDispatcher = Dispatchers.IO

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
      super@RijselDialogFragment.onCreate(savedInstanceState)
    }, savedInstanceState)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
  {
    viewDatabinding = DataBindingUtil.inflate(inflater, layoutId() ?: android.R.layout.activity_list_item, container, false)

    viewDatabinding?.lifecycleOwner = viewLifecycleOwner

    if (preBind() == false)
    {
      viewDatabinding?.addOnRebindCallback(onRebindCallback)
    }

    return viewDatabinding?.root
  }

  @CallSuper
  override fun onViewCreated(view: View, savedInstanceState: Bundle?)
  {
    super.onViewCreated(view, savedInstanceState)
    rijselizer?.onViewCreated()

    createViewModel()
    observeStates()
    computeViewModel()
  }

  protected open fun createViewModel()
  {
    val viewModelClass = getViewModelClass()
    val viewModelOwner: ViewModelStoreOwner = if (viewModelContext() == RijselFragmentConfigurable.ViewModelContext.Fragment) this@RijselDialogFragment else requireActivity()

    viewModel = ViewModelProvider(viewModelOwner, RijselViewModelFactory(getDispatcher(), requireActivity().application, this, arguments))[viewModelClass]
  }

  protected open fun observeStates()
  {
    viewModel?.stateManager?.state?.observe(viewLifecycleOwner) {
      Timber.d("The state ${it.javaClass.simpleName} has been observed into the fragment ${this@RijselDialogFragment.javaClass.simpleName}")

      when (it)
      {
        is RijselViewModel.StateManager.State.LoadingState -> onLoadingState()
        is RijselViewModel.StateManager.State.LoadedState  -> onLoadedState()
        is RijselViewModel.StateManager.State.ErrorState   -> onErrorState()
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

  override fun registerRijselSharedFlowListener(rijselSharedFlowListeners: RijselSharedFlowListener)
  {
    rijselizer?.registerRijselSharedFlowListener(rijselSharedFlowListeners)
  }

  protected open fun onErrorState()
  {

  }

  @CallSuper
  protected open fun onLoadedState()
  {
    viewModel?.let {
      viewDatabinding?.apply {
        removeOnRebindCallback(onRebindCallback)

        getBindingVariable()?.let { bindingVariable ->
          setVariable(bindingVariable, it)
        }
      }
    }
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
