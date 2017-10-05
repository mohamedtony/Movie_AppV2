package com.example.android.movieapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movieapp.R;
import com.example.android.movieapp.utilities.BackdropImages;
import com.example.android.movieapp.utilities.MovieTrailer;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by medo on 05-Oct-17.
 */

public class ThrailerAdapter extends RecyclerView.Adapter<ThrailerAdapter.MovieViewHolder> {

    //final private OnItemClicked onItemClicked;
    List<MovieTrailer> mTrailer;
    List<BackdropImages> mTrailerImages;
    Context mContex;

    public ThrailerAdapter(List<MovieTrailer> mTrailer, List<BackdropImages> mTrailerImages, Context mContex) {
        this.mTrailer = mTrailer;
        this.mTrailerImages = mTrailerImages;
        this.mContex = mContex;
      //  this.onItemClicked = onItemClicked;

    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

       // holder.title.setText(mMovies.get(position).getTitle());
/*        Picasso.with(mContex)
                .load("http://image.tmdb.org/t/p/w185//" + mMovies.get(position).getPosterPath())
                .into(holder.poster);*/
        Picasso.with(mContex)
                .load("http://image.tmdb.org/t/p/w185//" + mTrailerImages.get(position).getFilePath())
                .into(holder.poster);

    }

    @Override
    public int getItemCount() {
        return mTrailer.size();
    }

/*    public interface OnItemClicked {
        public void onItemClickedLisener(int id);
    }*/

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView poster;
        TextView title;

        public MovieViewHolder(View itemView) {
            super(itemView);

            poster = (ImageView) itemView.findViewById(R.id.image_trailer);


            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            int pos = getAdapterPosition();

           mContex.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+mTrailer.get(pos).getKey())));

        }

    }
}