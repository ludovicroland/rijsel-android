package fr.rolandl.rijsel.content

/**
 * States that the Android [androidx.appcompat.app.AppCompatActivity] or [androidx.fragment.app.Fragment] entity which implements this interface is able to provide several
 * [RijselSharedFlowListener].
 * <p>
 * As soon as a [fr.rolandl.rijsel.app.Rijselable] entity implements this interface, it is able to register several wrapped  [android.content.BroadcastReceiver]
 * instances through the concept of [RijselSharedFlowListener]: this is handy, because it enables to aggregate several independent reusable
 * [RijselSharedFlowListener] at the same time, and because the framework takes care of unregistering them when the embedding entity is destroyed.
 * </p>
 * <p>
 * This interface has been split into two distinct methods, one for determining how many broadcast listeners the entity exposes,
 * one for getting each individual [RijselSharedFlowListener]. This split is mostly due to performance issues.
 * </p>
 *
 * @see fr.rolandl.rijsel.app.Rijselable.registerBroadcastListeners
 * @see RijselSharedFlowListener
 *
 * @author Ludovic Roland
 * @since 2018.11.06
 */
interface RijselSharedFlowListenersProvider
{

  /**
   * This method will be invoked by the framework, so that it knows how many [RijselSharedFlowListener] it exposes.
   *
   * @return the number of [RijselSharedFlowListener] which are supported
   *
   * @see getSharedFlowListener
   */
  fun getSharedFlowListenersCount(): Int

  /**
   * This method is bound to be invoked successfully by the framework with a `index` argument ranging from `0` to
   * `getBroadcastListenersCount() - 1`. The method implementation is responsible for returning all the [RijselSharedFlowListener]
   * that this entity is supposed to expose.
   *
   * @param index of the [RijselSharedFlowListener] to return
   *
   * @return the [RijselSharedFlowListener] for the provided `index` parameter
   *
   * @see getSharedFlowListenersCount
   */
  fun getSharedFlowListener(index: Int): RijselSharedFlowListener

}
