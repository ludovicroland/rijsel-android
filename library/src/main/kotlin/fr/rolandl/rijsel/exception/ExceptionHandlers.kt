package fr.rolandl.rijsel.exception

import fr.rolandl.rijsel.app.RijselApplication
import fr.rolandl.rijsel.lifecycle.ModelUnavailableException

/**
 * A wrapper class which holds exception handlers implementations.
 *
 * @author Ludovic Roland
 * @since 2018.11.06
 */
sealed class ExceptionHandlers
{

  /**
   * A simple implementation of [DefaultExceptionHandler] which pops-up error dialog boxes and toasts.
   * <p>
   * The labels of the dialogs and the toasts are i18ned though the provided [RijselApplication.I18N] provided instance.
   * </p>
   **/
  //TODO : rework the I18N and the "showdialog" part
  open class AbstractExceptionHandler(protected val i18n: RijselApplication.I18N, protected val issueAnalyzer: RijselIssueAnalyzer)
    : RijselExceptionHandler
  {

    override fun onModelUnavailableException(exception: ModelUnavailableException): Int
    {
      return if (exception.isAConnectivityProblem() == true)
      {
        i18n.connectivityProblemHint
      }
      else
      {
        i18n.businessObjectAvailabilityProblemHint
      }
    }

    override fun onException(isRecoverable: Boolean, throwable: Throwable): Int =
        i18n.otherProblemHint

    override fun getGenericErrorMessage(): Int =
        i18n.otherProblemHint

    override fun reportIssueIfNecessary(isRecoverable: Boolean, throwable: Throwable)
    {

    }

  }

  /**
   * A default implementation.
   */
  open class DefaultExceptionHandler(i18n: RijselApplication.I18N, issueAnalyzer: RijselIssueAnalyzer)
    : AbstractExceptionHandler(i18n, issueAnalyzer)

}
