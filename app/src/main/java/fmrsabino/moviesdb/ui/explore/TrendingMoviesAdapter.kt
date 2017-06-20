package fmrsabino.moviesdb.ui.explore

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import fmrsabino.moviesdb.R
import fmrsabino.moviesdb.data.model.ImageConfiguration
import fmrsabino.moviesdb.data.remote.Network
import fmrsabino.moviesdb.injection.scope.ActivityContext
import fmrsabino.moviesdb.injection.scope.ForView
import fmrsabino.moviesdb.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.explore_item.view.*
import timber.log.Timber
import javax.inject.Inject

@ForView
class TrendingMoviesAdapter @Inject constructor(val picasso: Picasso, @ActivityContext val context: Context) : RecyclerView.Adapter<TrendingMoviesAdapter.ViewHolder>() {
    val items: MutableList<Network.Movie> = mutableListOf()
    var imageConfiguration: ImageConfiguration? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
            ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.explore_item, parent, false))

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        fun bind(item: Network.Movie) {
            itemView.movie_title.text = item.title
            imageConfiguration?.let {
                //TODO: Make util to get proper size
                picasso.load(it.secureBaseUrl + it.posterSizes?.get(4) + item.posterPath).into(itemView.movie_cover)
            }
        }

        override fun onClick(v: View?) {
            val movie = items[adapterPosition]
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.MOVIE_ID_KEY, movie.id)
            context.startActivity(intent)
        }
    }

    fun onNewConfiguration(imageConfiguration: ImageConfiguration?) {
        this.imageConfiguration = imageConfiguration
    }

    fun onNewItems(newItems: List<Network.Movie>) {
        //TODO: Diff Util
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
