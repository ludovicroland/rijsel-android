package fr.rolandl.sample.app

import androidx.appcompat.app.AppCompatActivity
import fr.rolandl.rijsel.appcompat.app.RijselActionBarConfigurable
import fr.rolandl.rijsel.appcompat.app.RijselActivityAggregate
import fr.rolandl.rijsel.appcompat.app.RijselActivityConfigurable

/**
 * @author Ludovic Roland
 * @since 2018.11.08
 */
class SampleActivityAggregate(activity: AppCompatActivity, activityConfigurable: RijselActivityConfigurable?, actionBarConfigurable: RijselActionBarConfigurable?)
  : RijselActivityAggregate(activity, activityConfigurable, actionBarConfigurable)