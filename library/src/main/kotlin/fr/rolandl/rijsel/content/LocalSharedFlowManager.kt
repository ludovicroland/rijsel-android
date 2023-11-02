package fr.rolandl.rijsel.content

import android.content.Intent
import fr.rolandl.rijsel.app.RijselStateContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Helper to register for and send flow of Intents to local objects within your process.
 *
 * @author Ludovic Roland
 * @since 2020.12.23
 */
object LocalSharedFlowManager
{

  private val sharedFlow = MutableSharedFlow<Intent>()

  /**
   * Emit the given [Intent] to all interested [RijselSharedFlowListener].
   * This given intent is emit asynchronously through the [Dispatchers.IO].
   *
   * @param intent The [Intent] to emit; all receivers matching this Intent will receive the broadcast.
   * @see [RijselStateContainer.registerRijselSharedFlowListener]
   */
  fun emit(coroutineScope: CoroutineScope, intent: Intent)
  {
    coroutineScope.launch(Dispatchers.IO) {
      sharedFlow.emit(intent)
    }
  }

  /**
   * Filter and dispatch [Intent] to [RijselSharedFlowListener] every time an emitted [Intent] matches.
   *
   * @param rijselSharedFlowListener to dispatch the [Intent] in case of matching.
   *
   * @see [RijselSharedFlowListener.onCollect]
   */
  suspend fun collect(rijselSharedFlowListener: RijselSharedFlowListener)
  {
    val intentFilter = rijselSharedFlowListener.getIntentFilter()
    sharedFlow.filter { intent ->
      intentFilter.hasAction(intent.action) == true && ((intentFilter.countCategories() == 0 && (intent.categories?.size ?: 0) == 0) || (intentFilter.countCategories() > 0 && intentFilter.categoriesIterator().asSequence().firstOrNull { intent.categories?.contains(it) == true } != null))
    }.flowOn(Dispatchers.IO)
        .collect { intent ->
          withContext(Dispatchers.Main) {
            rijselSharedFlowListener.onCollect(intent)
          }
        }
  }

}
