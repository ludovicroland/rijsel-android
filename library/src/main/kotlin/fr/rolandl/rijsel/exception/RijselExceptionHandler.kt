package fr.rolandl.rijsel.exception

import android.content.Context
import androidx.annotation.StringRes
import fr.rolandl.rijsel.lifecycle.ModelUnavailableException

/**
 * Defines and splits the handling of various exceptions in a single place. This handler will be invoked once it has been
 * [fr.rolandl.rijsel.app.RijselActivityController.registerExceptionHandler] registered.
 * <p>
 * The exception handler will be invoked at runtime when an exception is thrown and is not handled. You do not need to log the exception, because
 * the [fr.rolandl.rijsel.app.RijselActivityController] already takes care of logging it, before invoking the current interface methods.
 * </p>
 *
 * @see fr.rolandl.rijsel.app.RijselActivityController.registerExceptionHandler
 *
 * @author Ludovic Roland
 * @since 2018.11.05
 */
interface RijselExceptionHandler
{

  /**
   * Is invoked whenever the [fr.rolandl.rijsel.lifecycle.RijselLifeCycle.onRetrieveModel] throws an exception.
   * <p>
   * Warning, it is not ensured that this method will be invoked from the UI thread!
   * </p>
   *
   * @param exception the exception that has been thrown
   *
   * @return the most appropriate string resource id
   */
  @StringRes
  fun onModelUnavailableException(exception: ModelUnavailableException): Int

  /**
   * Is invoked whenever a handled exception is thrown outside from an available [Context].
   * <p>
   * This method serves as a fallback on the framework, in order to handle gracefully exceptions and prevent the application from crashing.
   * </p>
   * <p>
   * Warning, it is not ensured that this method will be invoked from the UI thread!
   * </p>
   *
   * @param isRecoverable indicates whether the application is about to crash when the exception has been triggered
   * @param throwable     the exception that has been triggered
   *
   * @return the most appropriate string resource id
   */
  @StringRes
  fun onException(isRecoverable: Boolean, throwable: Throwable): Int

  /**
   * Is invoked whenever an issue occurs while trying to analyse a [Throwable] and extracting the most appropriate string resource.
   * <p>
   * This method serves as a fallback on the framework, in order to handle gracefully exceptions and prevent the application from crashing.
   * </p>
   * <p>
   * Warning, it is not ensured that this method will be invoked from the UI thread!
   * </p>
   *
   * @return the most appropriate string resource id
   */
  @StringRes
  fun getGenericErrorMessage(): Int

  fun reportIssueIfNecessary(isRecoverable: Boolean, throwable: Throwable)

}
