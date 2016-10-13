package fmrsabino.moviesdb.ui.search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import fmrsabino.moviesdb.R;
import fmrsabino.moviesdb.data.model.search.Result;
import fmrsabino.moviesdb.ui.detail.DetailActivity;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private Context context;
    private List<Result> results = new ArrayList<>();
    private String baseUrl;

    @Inject
    public SearchAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Result result = results.get(position);
        holder.textView.setText(results.get(position).title());
        Picasso.with(context).load(baseUrl + result.posterPath())
                .fit()
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.search_item_poster) ImageView imageView;
        @BindView(R.id.search_item_value) TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Result result = results.get(getAdapterPosition());
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(DetailActivity.MOVIE_ID_EXTRA, result.id());
            context.startActivity(intent);
        }
    }

    public void setPosterBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setResults(List<Result> results) {
        this.results.clear();
        if (results != null) {
            this.results.addAll(results);
        }
        notifyDataSetChanged();
    }
}
