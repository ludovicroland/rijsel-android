package fr.rolandl.rijsel.app

import fr.rolandl.rijsel.lifecycle.RijselLifeCycle

/**
 * All [androidx.appcompat.app.AppCompatActivity] and [androidx.fragment.app.Fragment] entities of the framework must at least implement this composite interface.
 * <p>
 * Any entity implementing this interface is considered as Rijsel ready (or Rijsel compliant) and benefit from all the framework features.
 * </p>
 * <p>
 * If the implementing entity also implements the [fr.rolandl.rijsel.content.RijselSharedFlowListener], or the [fr.rolandl.rijsel.content.RijselSharedFlowListenerProvider] or the
 * [fr.rolandl.rijsel.content.RijselSharedFlowListenersProvider] interface, the framework will register one or several [android.content.BroadcastReceiver],
 * as explained in the [fr.rolandl.rijsel.content.RijselSharedFlowListener].
 * </p>
 * <p>
 * When it is required to have an existing [androidx.appcompat.app.AppCompatActivity] or [androidx.fragment.app.Fragment] implement this interface, you may use the
 * [Rijselizer] on that purpose.
 * </p>
 *
 * @param AggregateClass the aggregate class accessible through the [Rijselened.setAggregate] and [Rijselened.getAggregate] methods
 *
 * @see [Rijselizer]
 *
 * @author Ludovic Roland
 * @since 2018.11.06
 */
interface Rijselable<AggregateClass>
  : Rijselened<AggregateClass>, RijselLifeCycle
