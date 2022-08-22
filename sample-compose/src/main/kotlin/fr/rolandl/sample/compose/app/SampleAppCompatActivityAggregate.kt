package fr.rolandl.sample.compose.app

import androidx.appcompat.app.AppCompatActivity
import fr.rolandl.rijsel.appcompat.app.RijselActionBarConfigurable
import fr.rolandl.rijsel.appcompat.app.RijselActivityAggregate
import fr.rolandl.rijsel.appcompat.app.RijselActivityConfigurable

/**
 * @author Ludovic Roland
 * @since 2022.04.2022
 */
class SampleAppCompatActivityAggregate(activity: AppCompatActivity, activityConfigurable: RijselActivityConfigurable?, actionBarConfigurable: RijselActionBarConfigurable?)
  : RijselActivityAggregate(activity, activityConfigurable, actionBarConfigurable)