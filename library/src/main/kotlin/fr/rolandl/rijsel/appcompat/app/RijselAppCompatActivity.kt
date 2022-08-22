package fr.rolandl.rijsel.appcompat.app

import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import fr.rolandl.rijsel.app.Rijselable
import fr.rolandl.rijsel.app.Rijselizer
import fr.rolandl.rijsel.content.RijselSharedFlowListener

/**
 * The basis class for all activities available in the framework.
 *
 * @param AggregateClass the aggregate class accessible through the [setAggregate] and [getAggregate] methods
 * @param ViewBindingClass the [ViewBinding] class associate with the activity
 *
 * @see Rijselable
 * @see RijselActivityAggregate
 *
 * @author Ludovic Roland
 * @since 2018.11.06
 */
abstract class RijselAppCompatActivity<AggregateClass : RijselActivityAggregate, ViewBindingClass : ViewBinding>
  : AppCompatActivity(),
    Rijselable<AggregateClass>, RijselActionBarConfigurable, RijselActivityConfigurable
{

  var viewBinding: ViewBindingClass? = null
    private set

  private val rijselizer by lazy { Rijselizer(this, this, this, null, lifecycleScope) }

  /**
   * TODO : function doc
   */
  abstract fun inflateViewBinding(): ViewBindingClass

  @CallSuper
  override fun onCreate(savedInstanceState: Bundle?)
  {
    rijselizer.onCreate({
      super@RijselAppCompatActivity.onCreate(savedInstanceState)
      viewBinding = inflateViewBinding()
      setContentView(viewBinding?.root)
    }, savedInstanceState)
  }

  @CallSuper
  override fun onRetrieveDisplayObjects()
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
