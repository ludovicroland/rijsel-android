package fr.rolandl.rijsel.content

/**
 * States that the Android [androidx.appcompat.app.AppCompatActivity] or [androidx.fragment.app.Fragment] entity which implements this interface is able to provide a single
 * [RijselSharedFlowListener].
 * <p>
 * As soon as a [fr.rolandl.rijsel.app.Rijselable] entity implements this interface, it is able to register a wrapped [android.content.BroadcastReceiver]
 * instance through the concept of [RijselSharedFlowListener]: this is handy, because it enables to integrate an independent reusable
 * [RijselSharedFlowListener] at the same time, and because the framework takes care of unregistering it when the embedding entity is destroyed.
 * </p>
 *
 * @see fr.rolandl.rijsel.app.Rijselable.registerRijselSharedFlowListener
 * @see RijselSharedFlowListener
 * @see RijselSharedFlowListenersProvider
 *
 * @author Ludovic Roland
 * @since 2018.11.06
 */
fun interface RijselSharedFlowListenerProvider
{

  /**
   * This method will be invoked by the framework for registering a [RijselSharedFlowListener].
   *
   * @return the broadcast listener that this provider exposes
   */
  fun getRijselSharedFlowListener(): RijselSharedFlowListener

}
