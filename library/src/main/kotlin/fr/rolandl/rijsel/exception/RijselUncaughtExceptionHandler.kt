package fr.rolandl.rijsel.exception

import fr.rolandl.rijsel.app.RijselActivityController

/**
 * Defined as a wrapper over the built-in [Thread.UncaughtExceptionHandler].
 *
 * @author Ludovic Roland
 * @since 2018.11.07
 */
class RijselUncaughtExceptionHandler(private val builtinUncaughtExceptionHandler: Thread.UncaughtExceptionHandler?)
  : Thread.UncaughtExceptionHandler
{

  override fun uncaughtException(thread: Thread?, throwable: Throwable)
  {
    try
    {
      RijselActivityController.handleException(false, throwable)
    }
    finally
    {
      builtinUncaughtExceptionHandler?.uncaughtException(thread, throwable)
    }
  }

}
