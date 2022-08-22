package fr.rolandl.sample.compose.app

import androidx.fragment.app.Fragment
import fr.rolandl.rijsel.fragment.app.RijselComposeFragmentAggregate
import fr.rolandl.rijsel.fragment.app.RijselComposeFragmentConfigurable

/**
 * @author Ludovic Roland
 * @since 2022.04.13
 */
class SampleFragmentAggregate(fragment: Fragment, fragmentConfigurable: RijselComposeFragmentConfigurable?)
  : RijselComposeFragmentAggregate(fragment, fragmentConfigurable)