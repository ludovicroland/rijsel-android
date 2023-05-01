package fr.rolandl.rijsel.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.CallSuper
import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import fr.rolandl.rijsel.app.Rijselable
import fr.rolandl.rijsel.app.Rijselizer
import fr.rolandl.rijsel.content.RijselSharedFlowListener
import fr.rolandl.rijsel.lifecycle.RijselComposeViewModel
import fr.rolandl.rijsel.lifecycle.RijselViewModelFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * @author Ludovic Roland
 * @since 2022.04.11
 */
abstract class RijselComposeActivity<AggregateClass : RijselComposeActivityAggregate, ViewModelClass : RijselComposeViewModel>
  : ComponentActivity(),
  Rijselable<AggregateClass>, RijselComposeActivityConfigurable
{

  protected var viewModel: ViewModelClass? = null

  private val rijselizer by lazy { Rijselizer(this, this, this, null, lifecycleScope) }

  @Composable
  abstract fun Body()

  protected open fun getViewModelClass(): Class<ViewModelClass>? =
    null

  protected open fun getDispatcher(): CoroutineDispatcher =
    Dispatchers.IO

  @CallSuper
  override fun onCreate(savedInstanceState: Bundle?)
  {
    rijselizer.onCreate({
      super@RijselComposeActivity.onCreate(savedInstanceState)

      createViewModel()
      observeStates()

      setContent { Body() }

      computeViewModel()
    }, savedInstanceState)
  }

  protected open fun createViewModel()
  {
    getViewModelClass()?.let {
      intent.putExtra(RijselComposeViewModel.RIJSEL_VIEWMODEL_BEHAVIOR_EXTRA, viewModelBehavior())
      viewModel = ViewModelProvider(this@RijselComposeActivity, createViewModelFactory())[it]
    }
  }

  protected open fun createViewModelFactory(): ViewModelProvider.Factory =
    RijselViewModelFactory(getDispatcher(), application, this, intent.extras)

  @CallSuper
  protected open fun computeViewModel()
  {
    viewModel?.computeViewModelInternal()
  }

  protected open fun observeStates()
  {
    lifecycleScope.launch {
      viewModel?.stateManager?.state?.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)?.collect {
        Timber.d("The state ${it.javaClass.simpleName} has been observed into the activity ${this@RijselComposeActivity.javaClass.simpleName}")

        when (it) {
          is RijselComposeViewModel.StateManager.State.LoadingState -> onLoadingState()
          is RijselComposeViewModel.StateManager.State.LoadedState  -> onLoadedState()
          is RijselComposeViewModel.StateManager.State.ErrorState   -> onErrorState()
        }
      }
    }
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

  @CallSuper
  final override fun onRetrieveDisplayObjects()
  {
  }

  @CallSuper
  override fun onStart()
  {
    super.onStart()
    rijselizer.onStart()
  }

  @CallSuper
  override fun onResume()
  {
    super.onResume()
    rijselizer.onResume()
  }

  @CallSuper
  override fun onPause()
  {
    try
    {
      rijselizer.onPause()
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
      rijselizer.onStop()
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
      rijselizer.onDestroy()
    }
    finally
    {
      super.onDestroy()
    }
  }

  override fun getAggregate(): AggregateClass? =
    rijselizer.getAggregate()

  override fun setAggregate(aggregate: AggregateClass?)
  {
    rijselizer.setAggregate(aggregate)
  }

  override fun registerRijselSharedFlowListener(rijselSharedFlowListener: RijselSharedFlowListener)
  {
    rijselizer.registerRijselSharedFlowListener(rijselSharedFlowListener)
  }

  @CallSuper
  override fun onNewIntent(intent: Intent)
  {
    super.onNewIntent(intent)
    rijselizer.onNewIntent()
  }

  override fun isRefreshingModelAndBinding(): Boolean =
    rijselizer.isRefreshingModelAndBinding()

  override fun isFirstLifeCycle(): Boolean =
    rijselizer.isFirstLifeCycle()

  override fun isInteracting(): Boolean =
    rijselizer.isInteracting()

  override fun isAlive(): Boolean =
    rijselizer.isAlive()

  override suspend fun onRetrieveModel()
  {

  }

  override fun refreshModelAndBind(retrieveModel: Boolean, onOver: Runnable?, immediately: Boolean)
  {
    rijselizer.refreshModelAndBind(retrieveModel, onOver, immediately)
  }

  override fun shouldKeepOn(): Boolean =
    rijselizer.shouldKeepOn()

  override fun onSaveInstanceState(outState: Bundle)
  {
    super.onSaveInstanceState(outState)
    rijselizer.onSaveInstanceState(outState)
  }

  fun refreshModelAndBind()
  {
    refreshModelAndBind(true, null, false)
  }

  override fun onBindModel()
  {

  }

}
