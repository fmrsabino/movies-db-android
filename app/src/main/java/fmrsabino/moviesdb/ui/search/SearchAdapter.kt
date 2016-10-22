package fmrsabino.moviesdb.ui.search

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import fmrsabino.moviesdb.R
import fmrsabino.moviesdb.data.model.search.Result
import fmrsabino.moviesdb.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.search_item.view.*
import java.util.*
import javax.inject.Inject

class SearchAdapter
@Inject
constructor(private val context: Context) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    private val results = ArrayList<Result>()
    var baseUrl: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(results[position])

    override fun getItemCount() = results.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val result = results[adapterPosition]
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.MOVIE_ID_EXTRA, result.id)
            context.startActivity(intent)
        }

        fun bind(result: Result) {
            itemView.search_item_value.text = result.title
            Picasso.with(context)
                    .load(baseUrl + result.posterPath)
                    .fit()
                    .centerCrop()
                    .into(itemView.search_item_poster)
        }
    }

    fun setResults(results: List<Result>?) {
        this.results.clear()
        results?.let { this.results.addAll(it) }
        notifyDataSetChanged()
    }
}
