package fr.rolandl.rijsel.app

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import fr.rolandl.rijsel.R
import fr.rolandl.rijsel.annotation.EscapeToRedirectorAnnotation
import fr.rolandl.rijsel.exception.RijselExceptionHandler
import fr.rolandl.rijsel.lifecycle.ModelUnavailableException
import fr.rolandl.rijsel.lifecycle.RijselLifeCycle
import timber.log.Timber

/**
 * Is responsible for intercepting an activity starting and redirect it to a prerequisite one if necessary, and for handling globally exceptions.
 * <p>
 * Everything described here which involves the [AppCompatActivity], is applicable provided the activity is a [Rijselable].
 * </p>
 * <p>
 * It is also a container for multiple interfaces relative to its architecture.
 * </p>
 *
 * @author Ludovic Roland
 * @since 2018.11.05
 */
object RijselActivityController
{

  /**
   * An interface which is requested when a new [AppCompatActivity] is bound to be started.
   * <p>
   * The redirector acts as a controller over the activities starting phase: if an activity should be started before another one is really
   * active, this is the right place to handle this at runtime.
   * </p>
   * <p>
   * This component is especially useful when ones need to make sure that an [AppCompatActivity] has actually been submitted to the end-user before
   * resuming a workflow. The common cases are the traditional application splash screen, or a signin/signup process.
   * </p>
   *
   * @see registerRedirector
   */
  fun interface Redirector
  {

    /**
     * Will be invoked by the framework, in order to know whether an [AppCompatActivity] should be
     * started instead of the provided one, which is supposed to have just started, or when the
     * [AppCompatActivity.onNewIntent] method is invoked. However, the method will be not been invoked when those methods are invoked due to a
     * configuration change.
     * <p>
     * Caution: if an [Exception] is thrown during the method execution, the application will crash!
     * </p>
     *
     * @param activity which is bound to be displayed
     *
     * @return `null` if and only if nothing is to be done, i.e. no activity should be started instead. Otherwise, the given intent will be
     * executed: in that case, the provided activity finishes
     *
     * @see needsRedirection
     */
    fun getRedirection(activity: ComponentActivity): Intent?

  }

  /**
   * An interface which is queried during the various life cycle events of a [fr.rolandl.rijsel.lifecycle.RijselLifeCycle].
   * <p>
   * An interceptor is the ideal place for centralizing in one place many of the [AppCompatActivity]/[Fragment] entity life cycle
   * events.
   * </p>
   *
   * @see registerRedirector
   */
  fun interface Interceptor
  {

    /**
     * Invoked every time a new event occurs on the provided [AppCompatActivity]/[Fragment]. For instance, this is an ideal for logging
     * application usage analytics.
     * <p>
     * The framework ensures that this method will be invoked from the UI thread, hence the method implementation should last a very short time!
     * </p>
     * <p>
     * Caution: if an exception is thrown during the method execution, the application will crash!
     * </p>
     *
     * @param activity  the [ComponentActivity] on which a life cycle event occurs
     * @param fragment  the [Fragment] on which the life cycle event occurs
     * @param event     the [RijselLifeCycle.Event] that has just happened
     */
    fun onLifeCycleEvent(activity: ComponentActivity, fragment: Fragment?, event: RijselLifeCycle.Event)

  }

  /**
   * When a new activity is started because of a redirection, the newly started activity will receive the
   * initial activity [Intent] through this key.
   *
   * @see needsRedirection
   * @see registerRedirector
   */
  const val CALLING_INTENT_EXTRA = "callingIntentExtra"

  private var redirector: RijselActivityController.Redirector? = null

  private var interceptor: RijselActivityController.Interceptor? = null

  var exceptionHandler: RijselExceptionHandler? = null
    private set

  /**
   * Attempts to decode from the provided [ComponentActivity] the original [Intent] that was
   *
   * @param activity the Activity whose Intent will be analyzed
   *
   * @return an [Intent] that may be started if the provided [ComponentActivity] actually contains a reference to
   * another [ComponentActivity] ; `null` otherwise
   *
   * @see CALLING_INTENT_EXTRA
   * @see needsRedirection
   * @see registerInterceptor
   */
  fun extractCallingIntent(activity: ComponentActivity): Intent? =
      activity.intent.getParcelableExtra(RijselActivityController.CALLING_INTENT_EXTRA)

  /**
   * Remembers the activity redirector that will be used by the framework, before starting a new [AppCompatActivity]
   *
   * @param redirector the redirector that will be requested at runtime, when a new activity is being started; if `null`, no redirection mechanism will be set up
   */
  fun registerRedirector(redirector: RijselActivityController.Redirector?)
  {
    this.redirector = redirector
  }

  /**
   * Remembers the activity interceptor that will be used by the framework, on every [Lifecycle.Event]
   * during the underlying [AppCompatActivity].
   *
   * @param interceptor the interceptor that will be invoked at runtime, on every event; if `null`, no interception mechanism will be used
   */
  fun registerInterceptor(interceptor: RijselActivityController.Interceptor?)
  {
    this.interceptor = interceptor
  }

  /**
   * Remembers the exception handler that will be used by the framework.
   *
   * @param exceptionHandler the handler that will be invoked in case of exception; if `null`, no exception handler will be used
   */
  @Synchronized
  fun registerExceptionHandler(exceptionHandler: RijselExceptionHandler?)
  {
    this.exceptionHandler = exceptionHandler
  }

  /**
   * Is invoked by the framework every time a [Lifecycle.Event] occurs for the provided activity. You should not invoke that method yourself!
   * <p>
   * Note that the method is synchronized, which means that the previous call will block the next one, if no thread is spawn.
   * </p>
   *
   * @param activity  the [ComponentActivity] which is involved with the event
   * @param event     the [Lifecycle.Event] that has just happened for that activity
   */
  @Synchronized
  fun onLifeCycleEvent(activity: ComponentActivity, fragment: Fragment?, event: RijselLifeCycle.Event)
  {
    interceptor?.onLifeCycleEvent(activity, fragment, event)
  }

  /**
   * Indicates whether a redirection is required before letting the activity continue its life cycle. It launches the redirected [AppCompatActivity] if a
   * redirection is needed, and provide to its [Intent] the initial activity [Intent] trough the extra [CALLING_INTENT_EXTRA] key.
   * <p>
   * If the provided activity exposes the [EscapeToRedirectorAnnotation] annotation, the method returns `false`.
   * </p>
   * <p>
   * Note that this method does not need to be marked as `synchronized`, because it is supposed to be invoked systematically from the UI thread.
   * </p>
   *
   * @param activity the activity which is being proved against the [Redirector]
   *
   * @return `true` if and only if the given activity should be paused (or ended) and if another activity should be launched instead through the
   * [AppCompatActivity.startActivity] method
   *
   * @see extractCallingIntent
   * @see Redirector.getRedirection
   * @see EscapeToRedirectorAnnotation
   */
  fun needsRedirection(activity: ComponentActivity): Boolean
  {
    if (redirector == null)
    {
      return false
    }

    Timber.d("Check for annotation")

    if (activity::class.java.getAnnotation(EscapeToRedirectorAnnotation::class.java) != null)
    {
      Timber.d("The Activity with class '${activity::class.qualifiedName}' is escaped regarding the Redirector")

      return false
    }

    Timber.d("Check for redirection")

    val intent = redirector?.getRedirection(activity) ?: return false

    Timber.d("A redirection is needed")

    // We redirect to the right Activity
    // We consider the parent activity in case it is embedded (like in an ActivityGroup)
    val formerIntent = if (activity.parent != null) activity.parent.intent else activity.intent
    intent.putExtra(RijselActivityController.CALLING_INTENT_EXTRA, formerIntent)

    // Disables the fact that the new started activity should belong to the tasks history and from the recent tasks
    activity.startActivity(intent)

    // We now finish the redirected Activity
    activity.finish()

    return true
  }

  /**
   * Dispatches the exception to the [RijselExceptionHandler], and invokes the right method depending on its nature.
   * <p>
   * The framework is responsible for invoking that method every time an unhandled exception is thrown. If no
   * [RijselExceptionHandler]is registered, the exception will be only logged, and the method will return `false`.
   * </p>
   * <p>
   * Note that this method is `synchronized`, which prevents it from being invoking while it is already being executed, and which involves that
   * only one [Throwable] may be handled at the same time.
   * </p>
   *
   * @param isRecoverable indicates whether the application is about to crash when the exception has been triggered
   * @param throwable     the reported exception
   *
   * @return the string resource id of the most appropriate error message.
   *
   * @see registerExceptionHandler
   */
  @StringRes
  @Synchronized
  fun handleException(isRecoverable: Boolean, throwable: Throwable): Int
  {
    val theExceptionHandler = exceptionHandler

    return if (theExceptionHandler == null)
    {
      Timber.w(throwable, "Detected an exception which will not be handled")
      R.string.rijsel_defaultErrorMessage
    }
    else
    {
      try
      {
        theExceptionHandler.reportIssueIfNecessary(isRecoverable, throwable)

        if (throwable is ModelUnavailableException)
        {
          Timber.w(throwable, "Caught an exception during the retrieval of the business objects")
          theExceptionHandler.onModelUnavailableException(throwable)
        }
        else
        {
          Timber.w(throwable, "Caught an exception")
          theExceptionHandler.onException(isRecoverable, throwable)
        }
      }
      catch (otherThrowable: Throwable)
      {
        // Just to make sure that handled exceptions do not trigger un-handled exceptions on their turn ;(
        Timber.e(otherThrowable, "An error occurred while attempting to handle an exception")
        theExceptionHandler.getGenericErrorMessage()
      }
    }

  }

}
