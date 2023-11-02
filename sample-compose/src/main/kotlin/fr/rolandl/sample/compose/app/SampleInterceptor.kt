package fr.rolandl.sample.compose.app

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import fr.rolandl.rijsel.activity.RijselComposeActivityConfigurable
import fr.rolandl.rijsel.app.RijselActivityInterceptor
import fr.rolandl.rijsel.fragment.app.RijselComposeFragmentConfigurable

/**
 * @author Ludovic Roland
 * @since 2018.11.08
 */
class SampleInterceptor
  : RijselActivityInterceptor()
{

  override fun instantiateComposeActivityAggregate(activity: ComponentActivity, activityConfigurable: RijselComposeActivityConfigurable?): SampleActivityAggregate =
      SampleActivityAggregate(activity, activityConfigurable)

  override fun instantiateComposeFragmentAggregate(fragment: Fragment, fragmentConfigurable: RijselComposeFragmentConfigurable?): SampleFragmentAggregate =
      SampleFragmentAggregate(fragment, fragmentConfigurable)

}
