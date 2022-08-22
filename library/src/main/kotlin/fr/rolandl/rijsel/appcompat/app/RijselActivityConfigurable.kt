package fr.rolandl.rijsel.appcompat.app

import androidx.annotation.IdRes
import fr.rolandl.rijsel.fragment.app.DummyRijselFragment
import fr.rolandl.rijsel.fragment.app.RijselFragment
import kotlin.reflect.KClass

/**
 * Interface which should be used on a [RijselAppCompatActivity], in order to configure it.
 *
 * @author Ludovic Roland
 * @since 2020.12.18
 */
interface RijselActivityConfigurable
{

  /**
   * @return the view identifier to be used to place the [RijselFragment] defined by the
   * [fr.rolandl.rijsel.appcompat.app.RijselActivityConfigurable.fragmentClass] method.
   */
  @IdRes
  fun fragmentPlaceholderId(): Int? =
      null

  /**
   * @return the [RijselFragment] class to be created and displayed in the [fr.rolandl.rijsel.appcompat.app.RijselActivityConfigurable.fragmentPlaceholderId] view holder.
   */
  fun fragmentClass(): KClass<out RijselFragment<*, *, *>> =
      DummyRijselFragment::class

  /**
   * @return if the fragment referred into the [fr.rolandl.rijsel.appcompat.app.RijselActivityConfigurable.fragmentClass] should be added to the backStack or not.
   */
  fun addFragmentToBackStack(): Boolean =
      false

  /**
   * @return the name of the fragment if it is add to the backStack.
   */
  fun fragmentBackStackName(): String? =
      null

  /**
   * @return true if the activity can rotate.
   */
  fun canRotate(): Boolean =
      false

}
