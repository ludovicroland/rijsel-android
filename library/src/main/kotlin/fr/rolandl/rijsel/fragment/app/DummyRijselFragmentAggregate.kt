package fr.rolandl.rijsel.fragment.app

import androidx.annotation.RestrictTo

/**
 * An internal default [RijselFragmentAggregate] class used by the [DummyRijselFragment] only.
 *
 * @author Ludovic Roland
 * @since 2018.11.12
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class DummyRijselFragmentAggregate(fragment: RijselFragment<*, *, *>, fragmentConfigurable: RijselFragmentConfigurable?)
  : RijselFragmentAggregate(fragment, fragmentConfigurable)
