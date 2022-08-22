package fr.rolandl.rijsel.fragment.app

import androidx.fragment.app.Fragment

/**
 * The basis class for all Compose Fragment Aggregate available in the framework.
 *
 * @author Ludovic Roland
 * @since 2022.04.12
 */
abstract class RijselComposeFragmentAggregate(val fragment: Fragment, private val fragmentConfigurable: RijselComposeFragmentConfigurable?)