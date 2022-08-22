package fr.rolandl.rijsel.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import fr.rolandl.rijsel.content.RijselSharedFlowListener
import fr.rolandl.rijsel.lifecycle.RijselLifeCycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * TODO: class documentation
 *
 * @author Ludovic Roland
 * @since 2018.11.06
 */
class Rijselizer<AggregateClass : Any, ComponentClass : Any>(val activity: ComponentActivity,
                                                             val rijselable: Rijselable<AggregateClass>,
                                                             val component: ComponentClass,
                                                             val fragment: Fragment?,
                                                             val coroutineScope: LifecycleCoroutineScope)
  : Rijselable<AggregateClass>
{

  init
  {
    Timber.d("Creating the Rijselizer for Activity belonging to class '${activity.javaClass.name}' and with Fragment belonging to class '${fragment?.javaClass?.name}'")
  }

  private val stateContainer by lazy { RijselStateContainer<AggregateClass, ComponentClass>(activity, component) }

  override fun onRetrieveDisplayObjects()
  {
    rijselable.onRetrieveDisplayObjects()
  }

  override suspend fun onRetrieveModel()
  {
    rijselable.onRetrieveModel()
  }

  override fun onBindModel()
  {
    rijselable.onBindModel()
  }

  override fun getAggregate(): AggregateClass? =
      stateContainer.aggregate

  override fun setAggregate(aggregate: AggregateClass?)
  {
    stateContainer.aggregate = aggregate
  }

  override fun registerRijselSharedFlowListener(rijselSharedFlowListener: RijselSharedFlowListener)
  {
    stateContainer.registerRijselSharedFlowListener(coroutineScope, rijselSharedFlowListener)
  }

  override fun refreshModelAndBind(retrieveModel: Boolean, onOver: Runnable?, immediately: Boolean)
  {
    if (stateContainer.isAliveAsWellAsHostingActivity() == false)
    {
      // In that case, we skip the processing
      return
    }

    if (stateContainer.shouldDelayRefreshModelAndBind(retrieveModel, onOver, immediately) == true)
    {
      return
    }

    if (stateContainer.isAliveAsWellAsHostingActivity() == false)
    {
      // In that case, we skip the processing
      return
    }

    stateContainer.onRefreshingModelAndBindingStart()

    coroutineScope.launch(Dispatchers.IO) {
      if (onRetrieveModelInternal(retrieveModel) == true)
      {
        withContext(Dispatchers.Main) {
          if (stateContainer.isAliveAsWellAsHostingActivity() == true)
          {
            onBindModelInternal(onOver)
          }
        }
      }
    }
  }

  override fun isRefreshingModelAndBinding(): Boolean =
      stateContainer.isRefreshingModelAndBinding()

  override fun isFirstLifeCycle(): Boolean =
      stateContainer.firstLifeCycle

  override fun isInteracting(): Boolean =
      stateContainer.isInteracting

  override fun isAlive(): Boolean =
      stateContainer.isAlive

  override fun shouldKeepOn(): Boolean =
      stateContainer.shouldKeepOn()

  fun refreshModelAndBind()
  {
    refreshModelAndBind(true, null, false)
  }

  fun onCreate(superMethod: Runnable, savedInstanceState: Bundle?)
  {
    Timber.d("Rijselizer::onCreate ${activity.javaClass.simpleName} - ${fragment?.javaClass?.simpleName}")

    superMethod.run()

    if (isFragment() == false && RijselActivityController.needsRedirection(activity) == true)
    {
      // We stop here if a redirection is needed
      stateContainer.beingRedirected()

      return
    }
    else
    {
      RijselActivityController.onLifeCycleEvent(activity, fragment, RijselLifeCycle.Event.ON_CREATE)
    }

    stateContainer.firstLifeCycle = RijselStateContainer.isFirstCycle(savedInstanceState) != true

    stateContainer.registerRijselSharedFlowListeners(coroutineScope)

    if (isFragment() == false)
    {
      try
      {
        onRetrieveDisplayObjects()
      }
      catch (exception: Exception)
      {
        stateContainer.stopHandling()
        Timber.w(exception, "Cannot retrieve display objects references")
        return
      }
    }
  }

  fun onNewIntent()
  {
    Timber.d("Rijselizer::onNewIntent")

    if (isFragment() == false && RijselActivityController.needsRedirection(activity) == true)
    {
      // We stop here if a redirection is needed
      stateContainer.beingRedirected()
    }
  }

  fun onResume()
  {
    Timber.d("Rijselizer::onResume")

    if (shouldKeepOn() == false)
    {
      return
    }

    RijselActivityController.onLifeCycleEvent(activity, fragment, RijselLifeCycle.Event.ON_RESUME)

    stateContainer.onResume()

    refreshModelAndBindInternal()
  }

  fun onViewCreated()
  {
    Timber.d("Rijselizer::onViewCreated")

    RijselActivityController.onLifeCycleEvent(activity, fragment, RijselLifeCycle.Event.ON_VIEW_CREATED)
  }

  fun onSaveInstanceState(outState: Bundle)
  {
    Timber.d("Rijselizer::onSaveInstanceState")

    stateContainer.onSaveInstanceState(outState)
  }

  fun onStart()
  {
    Timber.d("Rijselizer::onStart")

    RijselActivityController.onLifeCycleEvent(activity, fragment, RijselLifeCycle.Event.ON_START)
  }

  fun onPause()
  {
    Timber.d("Rijselizer::onPause")

    if (shouldKeepOn() == false)
    {
      // We stop here if a redirection is needed or is something went wrong
      return
    }

    RijselActivityController.onLifeCycleEvent(activity, fragment, RijselLifeCycle.Event.ON_PAUSE)
    stateContainer.onPause()
  }

  fun onStop()
  {
    Timber.d("Rijselizer::onStop")

    RijselActivityController.onLifeCycleEvent(activity, fragment, RijselLifeCycle.Event.ON_STOP)
  }

  fun onDestroy()
  {
    Timber.d("Rijselizer::onDestroy")

    stateContainer.onDestroy()

    if (shouldKeepOn() == false)
    {
      // We stop here if a redirection is needed or is something went wrong
      return
    }

    RijselActivityController.onLifeCycleEvent(activity, fragment, RijselLifeCycle.Event.ON_DESTROY)
  }

  private suspend fun onRetrieveModelInternal(retrieveModel: Boolean): Boolean
  {
    try
    {
      if (retrieveModel == true)
      {
        if (stateContainer.isAliveAsWellAsHostingActivity() == false)
        {
          // If the entity is no more alive, we give up the process
          return false
        }

        onRetrieveModel()

        // We notify the entity that the business objects have actually been loaded
        if (stateContainer.isAliveAsWellAsHostingActivity() == false)
        {
          // If the entity is no more alive, we give up the process
          return false
        }
      }

      stateContainer.modelRetrieved()
      return true
    }
    catch (throwable: Throwable)
    {
      stateContainer.onRefreshingModelAndBindingStop(this@Rijselizer)

      // We check whether the issue does not come from a non-alive entity
      return if (stateContainer.isAliveAsWellAsHostingActivity() == false)
      {
        // In that case, we just ignore the exception: it is very likely that the entity or the hosting Activity have turned as non-alive
        // during the "onRetrieveBusinessObjects()" method!
        false
      }
      else
      {
        // Otherwise, we report the exception
        onInternalModelAvailableException(throwable)

        false
      }
    }
  }

  private fun onBindModelInternal(onOver: Runnable?)
  {
    if (stateContainer.resumedForTheFirstTime == true)
    {
      try
      {
        onBindModel()
      }
      catch (throwable: Throwable)
      {
        stateContainer.onRefreshingModelAndBindingStop(this)
        Timber.w(throwable, "Cannot bind model")

        return
      }
    }

    stateContainer.markNotResumedForTheFirstTime()

    if (onOver != null)
    {
      try
      {
        onOver.run()
      }
      catch (throwable: Throwable)
      {
        Timber.e(throwable, "An exception occurred while executing the 'refreshModelAndBind()' runnable!")
      }

    }

    stateContainer.onRefreshingModelAndBindingStop(this)
  }

  private fun refreshModelAndBindInternal()
  {
    rijselable.refreshModelAndBind(stateContainer.isRetrievingModel(), stateContainer.getRetrieveModelOver(), true)
  }

  private fun onInternalModelAvailableException(throwable: Throwable)
  {
    Timber.e(throwable, "Cannot retrieve the view model")

    if (stateContainer.onInternalModelAvailableExceptionWorkAround(throwable) == true)
    {
      return
    }

    // We need to indicate to the method that it may have been triggered from another thread than the GUI's
    Timber.w(throwable, "An internal error occurs")
  }

  private fun isFragment(): Boolean =
      activity != rijselable

}
