package fr.rolandl.sample.compose

import android.content.Intent
import fr.rolandl.sample.compose.app.SampleConnectivityListener
import fr.rolandl.sample.compose.app.SampleInterceptor
import fr.rolandl.rijsel.activity.RijselComposeSplashscreenActivity
import fr.rolandl.rijsel.app.RijselActivityController
import fr.rolandl.rijsel.app.RijselApplication
import fr.rolandl.rijsel.app.RijselConnectivityListener
import fr.rolandl.rijsel.exception.ExceptionHandlers
import fr.rolandl.rijsel.exception.RijselExceptionHandler
import fr.rolandl.rijsel.exception.RijselIssueAnalyzer
import timber.log.Timber

/**
 * @author Ludovic Roland
 * @since 2018.11.08
 */
class SampleApplication
  : RijselApplication<RijselApplication.ApplicationConstants>()
{

  override fun setupTimber()
  {
    if (BuildConfig.DEBUG == true)
    {
      Timber.plant(Timber.DebugTree())
    }
  }

  override fun getI18N(): RijselApplication.I18N =
      RijselApplication.I18N(R.string.businessProblem, R.string.connectivityProblem, R.string.unavailableService)

  override fun retrieveConnectivityListener(): RijselConnectivityListener =
      SampleConnectivityListener(this)

  override fun onSetupExceptionHandlers()
  {
    //Init Crashlytics here
  }

  override fun getActivityRedirector(): RijselActivityController.Redirector
  {
    return RijselActivityController.Redirector { activity ->
      if (RijselComposeSplashscreenActivity.isInitialized(SampleSplashscreenActivity::class) == null && activity is SampleSplashscreenActivity == false)
      {
        Intent(activity, SampleSplashscreenActivity::class.java)
      }
      else
      {
        null
      }
    }
  }

  override fun getInterceptor(): RijselActivityController.Interceptor
  {
    val applicationInterceptor = SampleInterceptor()

    return RijselActivityController.Interceptor { activity, fragment, event ->
      applicationInterceptor.onLifeCycleEvent(activity, fragment, event)
      connectivityListener?.onLifeCycleEvent(activity, fragment, event)
    }

  }

  override fun getExceptionHandler(): RijselExceptionHandler
  {

    return object : ExceptionHandlers.DefaultExceptionHandler(getI18N(), RijselIssueAnalyzer.DefaultIssueAnalyzer(applicationContext))
    {

      override fun reportIssueIfNecessary(isRecoverable: Boolean, throwable: Throwable)
      {
        if (isRecoverable == false)
        {
          return
        }

        //TODO : log exception in Crashlytics
      }

    }
  }

}

