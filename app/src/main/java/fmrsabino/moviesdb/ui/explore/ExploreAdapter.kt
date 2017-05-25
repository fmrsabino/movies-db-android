package fmrsabino.moviesdb.ui.explore

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fmrsabino.moviesdb.R
import fmrsabino.moviesdb.data.remote.Network
import fmrsabino.moviesdb.injection.scope.ForView
import kotlinx.android.synthetic.main.explore_item.view.*
import javax.inject.Inject

@ForView
class ExploreAdapter @Inject constructor() : RecyclerView.Adapter<ExploreAdapter.ViewHolder>() {
    val items: MutableList<Network.Movie> = mutableListOf()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
            ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.explore_item, parent, false))

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Network.Movie) {
            itemView.movie_title.text = item.title
        }
    }

    fun onNewItems(newItems: List<Network.Movie>) {
        //TODO: Diff Util
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
