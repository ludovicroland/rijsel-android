package fr.rolandl.sample

import android.content.Intent
import android.content.IntentFilter
import android.view.MenuItem
import androidx.viewbinding.ViewBinding
import fr.rolandl.sample.app.SampleActivityAggregate
import fr.rolandl.rijsel.appcompat.app.RijselActionBarConfigurable
import fr.rolandl.rijsel.appcompat.app.RijselActivityConfigurable
import fr.rolandl.rijsel.appcompat.app.RijselAppCompatActivity
import fr.rolandl.rijsel.content.RijselSharedFlowListener
import fr.rolandl.rijsel.content.RijselSharedFlowListenerProvider
import timber.log.Timber

/**
 * @author Ludovic Roland
 * @since 2018.11.08
 */
abstract class SampleActivity<ViewBindingClass : ViewBinding>
  : RijselAppCompatActivity<SampleActivityAggregate, ViewBindingClass>(),
  RijselActivityConfigurable, RijselActionBarConfigurable,
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
          IntentFilter(SAMPLE_ACTION)

      override fun onCollect(intent: Intent)
      {
        if(intent.action == SAMPLE_ACTION)
        {
          Timber.d("SAMPLE ACTION RECEIVED")
        }
      }

    }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean
  {
    return if (item.itemId == android.R.id.home)
    {
      onBackPressed()
      true
    }
    else
    {
      super.onOptionsItemSelected(item)
    }
  }

}