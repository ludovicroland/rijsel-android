package fr.rolandl.rijsel.appcompat.app

import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar

/**
 * Interface used on a [RijselAppCompatActivity], in order to configure the [Toolbar]/[ActionBar] behavior.
 * <p>
 * The associated [RijselActivityAggregate] will configure the behavior of the [Toolbar]/[ActionBar].
 * </p>
 *
 * @author Ludovic Roland
 * @see RijselActivityAggregate
 * @since 2020.12.18
 */
interface RijselActionBarConfigurable
{

  /**
   * Defines the available [Toolbar]/[ActionBar] "home" button action behaviors handled by the [RijselActivityAggregate].
   */
  enum class ActionBarBehavior
  {

    /**
     * The [Toolbar]/[ActionBar] will display an "up" arrow icon
     */
    Up,

    /**
     * The [Toolbar]/[ActionBar] will display a hamburger icon
     */
    Drawer,

    /**
     * The [Toolbar]/[ActionBar] will display nothing
     */
    None
  }

  /**
   * @return the behavior to be applied to the [Toolbar]/[ActionBar].
   */
  fun actionBarBehavior(): ActionBarBehavior? =
      null

  /**
   * @return the [Toolbar] widget to be used as 'ActionBar'
   */
  fun toolbar(): Toolbar? =
      null

}
