package fr.rolandl.sample.app

import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import fr.rolandl.rijsel.app.RijselActivityInterceptor
import fr.rolandl.rijsel.appcompat.app.RijselActionBarConfigurable
import fr.rolandl.rijsel.appcompat.app.RijselActivityConfigurable
import fr.rolandl.rijsel.fragment.app.RijselFragmentConfigurable

/**
 * @author Ludovic Roland
 * @since 2018.11.08
 */
class SampleInterceptor
  : RijselActivityInterceptor()
{

  override fun instantiateAppCompatActivityAggregate(activity: ComponentActivity, activityConfigurable: RijselActivityConfigurable?, actionBarConfigurable: RijselActionBarConfigurable?): SampleActivityAggregate =
      SampleActivityAggregate(activity as AppCompatActivity, activityConfigurable, actionBarConfigurable)

  override fun instantiateFragmentAggregate(fragment: Fragment, fragmentConfigurable: RijselFragmentConfigurable?): SampleFragmentAggregate =
      SampleFragmentAggregate(fragment, fragmentConfigurable)

}
