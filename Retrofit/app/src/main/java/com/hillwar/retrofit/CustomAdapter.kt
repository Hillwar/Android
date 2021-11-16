package com.hillwar.retrofit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.hillwar.retrofit.MyApp.Companion.instance
import com.hillwar.retrofit.databinding.ItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CustomAdapter(private val dataSet: ArrayList<Post>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var binding: ItemBinding = ItemBinding.bind(view)
        val title = binding.itemHead
        val body = binding.itemText
        val button = binding.itemButton
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item, viewGroup, false)
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val current = dataSet[position]
        with(viewHolder) {
            title.text = current.title
            body.text = current.body
            button.setOnClickListener {
                MainActivity.instance.lifecycle.coroutineScope.launch {
                    val response =
                        withContext(Dispatchers.Default) {
                            try {
                                instance.service.deleteGist(current.id.toString())
                            } catch (e: Exception) {
                                error(title.rootView.rootView.context)
                                null
                            }
                        }
                    if (response != null) Snackbar.make(
                        title.rootView.rootView,
                        (if (response.isSuccessful) "Delete is successful: "
                        else "Delete failed: ") + response.code(),
                        Snackbar.LENGTH_LONG
                    ).show()
                    if (response != null && response.isSuccessful) {
                        delete(position)
                    }
                }
            }
        }
    }

    fun add(post: Post) {
        dataSet.add(0, post)
        notifyDataSetChanged()
    }

    private fun delete(position: Int) {
        dataSet.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, dataSet.size)
    }

    override fun getItemCount() = dataSet.size

}