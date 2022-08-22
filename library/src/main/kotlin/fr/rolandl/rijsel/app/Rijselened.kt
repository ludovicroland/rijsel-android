package fr.rolandl.rijsel.app

import fr.rolandl.rijsel.content.RijselSharedFlowListener

/**
 * Defines some common methods for all [AppCompatActivity] and [Fragment] entities defined in the framework.
 *
 * @param AggregateClass the aggregate class accessible though the [setAggregate] and [getAggregate] methods
 *
 * @author Ludovic Roland
 * @since 2018.11.06
 */
interface Rijselened<AggregateClass>
{

  /**
   * Gives access to the entity underlying "aggregate" object.
   * <p>
   * This "aggregate" is especially useful to provide data to the entity, and is typically used by the [RijselActivityController.Interceptor] through
   * the entity life cycle events.
   * </p>
   *
   * @return an object that may be used along the [AppCompatActivity]/[Fragment] entity life
   *
   * @see setAggregate
   */
  fun getAggregate(): AggregateClass?

  /**
   * Enables to set an aggregate hat may be used along the [AppCompatActivity]/[Fragment] entity life.
   *
   * @param aggregate the object to use as an aggregate
   * @see getAggregate
   */
  fun setAggregate(aggregate: AggregateClass?)

  /**
   * Explicitly registers some wrapped broadcast receivers for the [AppCompatActivity]/[Fragment] entity. This method is
   * especially useful to declare and consume at the same place broadcast intents.
   * <p>
   * Those receivers will finally be unregistered by the[AppCompatActivity.onDestroy]/[Fragment.onDestroy] method.
   * </p>
   * <p>
   * When invoking that method, all previously registered listeners via the [fr.rolandl.rijsel.content.RijselSharedFlowListenerProvider] or
   * [fr.rolandl.rijsel.content.RijselSharedFlowListenersProvider] are kept, and the new provided ones are added.
   * </p>
   *
   * @param rijselSharedFlowListener the wrapped broadcast receivers to registers
   *
   * @see RijselSharedFlowListener
   * @see fr.rolandl.rijsel.content.RijselSharedFlowListenerProvider
   */
  fun registerRijselSharedFlowListener(rijselSharedFlowListener: RijselSharedFlowListener)

}
