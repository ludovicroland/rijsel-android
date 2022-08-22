package fr.rolandl.sample.item

import android.content.Intent
import android.view.View
import fr.rolandl.sample.R
import fr.rolandl.sample.databinding.ItemListBinding
import fr.rolandl.sample.viewmodel.ListItemModel
import fr.rolandl.rijsel.content.LocalSharedFlowManager
import com.xwray.groupie.viewbinding.BindableItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber

/**
 * @author Ludovic Roland
 * @since 2022/03/22
 */
class ListItem(val model: ListItemModel)
  : BindableItem<ItemListBinding>()
{
  private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

  override fun bind(viewBinding: ItemListBinding, position: Int)
  {
    viewBinding.model = model
    viewBinding.executePendingBindings()

    viewBinding.root.setOnClickListener {
      Timber.d(model.fragmentIdentityHashCode)

      LocalSharedFlowManager.emit(scope, Intent("ACTION").apply {
        addCategory(model.fragmentIdentityHashCode)
      })
    }
  }

  override fun getLayout(): Int =
      R.layout.item_list

  override fun initializeViewBinding(view: View): ItemListBinding =
      ItemListBinding.bind(view)
}