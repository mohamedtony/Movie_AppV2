package com.example.android.movieapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movieapp.R;
import com.example.android.movieapp.utilities.MovieShape;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by medo on 03-Sep-17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    final private OnItemClicked onItemClicked;
    List<MovieShape> mMovies;
    Context mContex;

    public MovieAdapter(List<MovieShape> mMovies, Context mContex, OnItemClicked onItemClicked) {
        this.mMovies = mMovies;
        this.mContex = mContex;
        this.onItemClicked = onItemClicked;

    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_row, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        holder.title.setText(mMovies.get(position).getTitle());
/*        Picasso.with(mContex)
                .load("http://image.tmdb.org/t/p/w185//" + mMovies.get(position).getPosterPath())
                .into(holder.poster);*/
        Picasso.with(mContex)
                .load("http://image.tmdb.org/t/p/w185//" + mMovies.get(position).getPosterPath())
                .into(holder.poster);

    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public interface OnItemClicked {
        public void onItemClickedLisener(int id);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView poster;
        TextView title;

        public MovieViewHolder(View itemView) {
            super(itemView);

            poster = (ImageView) itemView.findViewById(R.id.thumbnail);
            title = (TextView) itemView.findViewById(R.id.title);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            int pos = getAdapterPosition();
            int id = mMovies.get(pos).getId();

            onItemClicked.onItemClickedLisener(id);
        }

    }

}
