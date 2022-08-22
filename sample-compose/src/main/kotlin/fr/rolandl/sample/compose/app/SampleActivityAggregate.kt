package fr.rolandl.sample.compose.app

import androidx.activity.ComponentActivity
import fr.rolandl.rijsel.activity.RijselComposeActivityAggregate
import fr.rolandl.rijsel.activity.RijselComposeActivityConfigurable

/**
 * @author Ludovic Roland
 * @since 2022.04.12
 */
class SampleActivityAggregate(activity: ComponentActivity, activityConfigurable: RijselComposeActivityConfigurable?)
  : RijselComposeActivityAggregate(activity, activityConfigurable)