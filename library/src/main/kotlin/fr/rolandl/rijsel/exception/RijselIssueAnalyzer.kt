package fr.rolandl.rijsel.exception

import android.content.Context

/**
 * Responsible for analyzing issues resulting from [Throwable] entities.
 *
 * @author Ludovic Roland
 * @since 2018.11.06
 */
abstract class RijselIssueAnalyzer(val context: Context)
{

  /**
   * A default implementation.
   */
  class DefaultIssueAnalyzer(context: Context)
    : RijselIssueAnalyzer(context)
  {

    override fun handleIssue(throwable: Throwable?): Boolean
    {
      return false
    }

  }

  /**
   * Is responsible for analyzing the provided exception, and indicates whether it has been handled.
   *
   * @param throwable the issue to analyze
   *
   * @return `true` if and only if the issue has actually been handled by the implementation
   */
  abstract fun handleIssue(throwable: Throwable?): Boolean

}
