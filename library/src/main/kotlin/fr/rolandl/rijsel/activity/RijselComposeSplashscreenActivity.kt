package fr.rolandl.rijsel.activity

import android.content.Intent
import android.content.IntentFilter
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import fr.rolandl.rijsel.app.RijselActivityController
import fr.rolandl.rijsel.content.LocalSharedFlowManager
import fr.rolandl.rijsel.content.RijselSharedFlowListener
import fr.rolandl.rijsel.content.RijselSharedFlowListenerProvider
import fr.rolandl.rijsel.lifecycle.DummyRijselComposeViewModel
import fr.rolandl.rijsel.lifecycle.ModelUnavailableException
import timber.log.Timber
import java.util.*
import kotlin.reflect.KClass

/**
 * A basis activity class which is displayed while the application is loading.
 *
 * @param AggregateClass the aggregate class accessible though the [setAggregate] and [getAggregate] methods
 *
 * @author Ludovic Roland
 * @since 2022.04.11
 */
abstract class RijselComposeSplashscreenActivity<AggregateClass : RijselComposeActivityAggregate>
  : RijselComposeActivity<AggregateClass, DummyRijselComposeViewModel>(),
    RijselSharedFlowListenerProvider
{

  companion object
  {

    private const val MODEL_LOADED_ACTION = "modelLoadedAction"

    private val initialized = mutableMapOf<String, Date>()

    private var onRetrieveModelCustomStarted = false

    private var onRetrieveModelCustomOver = false

    private var onRetrieveModelCustomOverInvoked = false

    fun isInitialized(activityClass: KClass<out ComponentActivity>): Date? =
        initialized[activityClass.java.name]

    fun markAsInitialized(activityClass: KClass<out ComponentActivity>, isInitialized: Boolean)
    {
      if (isInitialized == false)
      {
        initialized.remove(activityClass.java.name)
        onRetrieveModelCustomStarted = false
        onRetrieveModelCustomOverInvoked = false
      }
      else
      {
        initialized[activityClass.java.name] = Date()
      }
    }

  }

  private var hasStopped: Boolean = false

  private var onStartRunnable: Runnable? = null

  override fun getRijselSharedFlowListener(): RijselSharedFlowListener
  {
    return object : RijselSharedFlowListener
    {
      override fun getIntentFilter(): IntentFilter
      {
        return IntentFilter(MODEL_LOADED_ACTION).apply {
          addCategory(packageName)
        }
      }

      override fun onCollect(intent: Intent)
      {
        if (MODEL_LOADED_ACTION == intent.action)
        {
          markAsInitialized()

          if (isFinishing == false)
          {
            // We do not take into account the event on the activity instance which is over
            if (onRetrieveModelCustomOverInvoked == false)
            {
              onRetrieveModelCustomOver {
                onRetrieveModelCustomOverInvoked = true
                finishActivity()
              }
            }
          }
        }
      }
    }
  }

  @Throws(ModelUnavailableException::class)
  protected abstract fun onRetrieveModelCustom()

  override fun onStart()
  {
    super.onStart()

    Timber.d("Marking the splash screen as un-stopped")

    hasStopped = false

    onStartRunnable?.let {
      Timber.d("Starting the delayed activity which follows the splash screen, because the splash screen is restarted")

      it.run()
    }
  }

  @Throws(ModelUnavailableException::class)
  final override suspend fun onRetrieveModel()
  {
    // We check whether another activity instance is already running the business objects retrieval
    if (onRetrieveModelCustomStarted == false)
    {
      onRetrieveModelCustomStarted = true
      var onRetrieveModelCustomSuccess = false

      try
      {
        onRetrieveModelCustom()
        onRetrieveModelCustomSuccess = true
      }
      finally
      {
        // If the retrieval of the business objects is a failure, we assume as if it had not been started
        if (onRetrieveModelCustomSuccess == false)
        {
          onRetrieveModelCustomStarted = false
        }
      }

      onRetrieveModelCustomOver = true
      LocalSharedFlowManager.emit(lifecycleScope, Intent(MODEL_LOADED_ACTION).addCategory(packageName))
    }
    else if (onRetrieveModelCustomOver == true)
    {
      // A previous activity instance has already completed the business objects retrieval, but the current instance was not active at this time
      LocalSharedFlowManager.emit(lifecycleScope, Intent(MODEL_LOADED_ACTION).addCategory(packageName))
    }
  }

  override fun onBindModel()
  {
  }

  override fun onStop()
  {
    try
    {
      Timber.d("Marking the splash screen as stopped")
      hasStopped = true
    }
    finally
    {
      super.onStop()
    }
  }

  protected open fun onRetrieveModelCustomOver(finishRunnable: Runnable)
  {
    finishRunnable.run()
  }

  protected open fun getNextActivity(): KClass<out ComponentActivity>? =
      null

  protected open fun startCallingIntent()
  {
    val callingIntent = RijselActivityController.extractCallingIntent(this)

    Timber.d("Redirecting to the initial activity for the component with class '${callingIntent?.component?.className}'")

    // This is essential, in order for the activity to be displayed
    callingIntent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

    try
    {
      startActivity(callingIntent)
    }
    catch (throwable: Throwable)
    {
      Timber.e(throwable, "Cannot start the Activity with Intent '$callingIntent'")
    }

  }

  protected open fun computeNextIntent(): Intent?
  {
    val nextActivity = getNextActivity()

    return if (nextActivity != null)
    {
      Intent(applicationContext, nextActivity.java)
    }
    else
    {
      null
    }
  }

  private fun markAsInitialized()
  {
    markAsInitialized(this@RijselComposeSplashscreenActivity::class, true)
  }

  protected open fun finishActivity()
  {
    if (isFinishing == false)
    {
      val runnable = Runnable {
        Timber.d("Starting the activity which follows the splash screen")

        if (intent.hasExtra(RijselActivityController.CALLING_INTENT_EXTRA) == true)
        {
          // We only resume the previous activity if the splash screen has not been dismissed
          startCallingIntent()

          Timber.d("Finishing the splash screen")

          finish()
        }
        else
        {
          // We only resume the previous activity if the splash screen has not been dismissed
          val nextIntent = computeNextIntent()

          if (nextIntent != null)
          {
            startActivity(nextIntent)

            Timber.d("Finishing the splash screen")

            finish()
          }
          else
          {
            Timber.d("The splash screen cannot be finished since there is no activity to launch next")
          }
        }
      }
      if (hasStopped == false)
      {
        runnable.run()
      }
      else
      {
        onStartRunnable = runnable

        Timber.d("Delays the starting the activity which follows the splash screen, because the splash screen has been stopped")
      }
    }
    else
    {
      Timber.d("Gives up the starting the activity which follows the splash screen, because the splash screen is finishing or has been stopped")
    }
  }

}
