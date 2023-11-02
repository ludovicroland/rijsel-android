package fr.rolandl.rijsel.app

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import fr.rolandl.rijsel.activity.RijselComposeActivityAggregate
import fr.rolandl.rijsel.activity.RijselComposeActivityConfigurable
import fr.rolandl.rijsel.fragment.app.*
import fr.rolandl.rijsel.lifecycle.RijselLifeCycle

/**
 * An interceptor which is responsible for handling the [RijselFragmentConfigurable] interfaces declarations on [AppCompatActivity] and [Fragment].
 *
 * @author Ludovic Roland
 * @since 2018.11.07
 */
abstract class RijselActivityInterceptor
  : RijselActivityController.Interceptor
{

  protected open fun instantiateComposeActivityAggregate(activity: ComponentActivity, activityConfigurable: RijselComposeActivityConfigurable?): RijselComposeActivityAggregate? =
    null

  protected open fun instantiateComposeFragmentAggregate(fragment: Fragment, fragmentConfigurable: RijselComposeFragmentConfigurable?): RijselComposeFragmentAggregate? =
    null

  override fun onLifeCycleEvent(activity: ComponentActivity, fragment: Fragment?, event: RijselLifeCycle.Event)
  {
    if (event == RijselLifeCycle.Event.ON_CREATE)
    {
      //it's a fragment
      if(fragment is Rijselable<*>)
      {
        if (fragment is RijselComposeFragment<*, *>)
        {
          instantiateComposeFragmentAggregate(fragment, (fragment as? RijselComposeFragmentConfigurable))?.let { aggregate ->
            (fragment as Rijselable<RijselComposeFragmentAggregate>).apply {
              this.setAggregate(aggregate)
            }
          }
        }
      }
      else
      {
        if (activity is fr.rolandl.rijsel.activity.RijselComposeActivity<*, *>)
        {
          instantiateComposeActivityAggregate(activity, (activity as? RijselComposeActivityConfigurable))?.let { aggregate ->
            (activity as Rijselable<RijselComposeActivityAggregate>).apply {
              this.setAggregate(aggregate)
              this.getAggregate()?.onCreate()
            }
          }
        }
      }
    }
  }

}
