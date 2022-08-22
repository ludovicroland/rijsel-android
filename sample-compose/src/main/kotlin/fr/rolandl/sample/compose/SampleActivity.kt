package fr.rolandl.sample.compose

import android.content.Intent
import android.content.IntentFilter
import fr.rolandl.rijsel.activity.RijselComposeActivity
import fr.rolandl.sample.compose.app.SampleActivityAggregate
import fr.rolandl.rijsel.content.RijselSharedFlowListener
import fr.rolandl.rijsel.content.RijselSharedFlowListenerProvider
import fr.rolandl.rijsel.lifecycle.RijselComposeViewModel
import timber.log.Timber

/**
 * @author Ludovic Roland
 * @since 2022.04.13
 */
abstract class SampleActivity<ViewModelClass : RijselComposeViewModel>
  : RijselComposeActivity<SampleActivityAggregate, ViewModelClass>(),
    RijselSharedFlowListenerProvider
{

  companion object
  {

    const val SAMPLE_ACTION = "sampleAction"

  }

  override fun getRijselSharedFlowListener(): RijselSharedFlowListener
  {
    return object: RijselSharedFlowListener
    {

      override fun getIntentFilter(): IntentFilter =
          IntentFilter(SampleActivity.SAMPLE_ACTION)

      override fun onCollect(intent: Intent)
      {
        if(intent.action == SampleActivity.SAMPLE_ACTION)
        {
          Timber.d("SAMPLE ACTION RECEIVED")
        }
      }

    }
  }

}
