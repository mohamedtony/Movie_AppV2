package com.example.android.movieapp;

import android.content.ContentValues;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movieapp.Adapters.ThrailerAdapter;
import com.example.android.movieapp.BroadCastRecevier.CheckOnlineReceiver;
import com.example.android.movieapp.contentprovider.MovieContract;
import com.example.android.movieapp.utilities.ApiRetrofitClient;
import com.example.android.movieapp.utilities.BackdropImages;
import com.example.android.movieapp.utilities.MovieImagesResponse;
import com.example.android.movieapp.utilities.MovieReview;
import com.example.android.movieapp.utilities.MovieReviewResponse;
import com.example.android.movieapp.utilities.MovieShape;
import com.example.android.movieapp.utilities.MovieTrailer;
import com.example.android.movieapp.utilities.MovieTrailerResponse;
import com.example.android.movieapp.utilities.PosterImages;
import com.example.android.movieapp.utilities.RequestMovieInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetail extends AppCompatActivity implements CheckOnlineReceiver.MyInterFace{

    private TextView movie_title,release_date,tv_duration,tv_rat,tv_overview;
    private ImageView poster,poster_back;
    private MovieShape movieShape;
    private int id;
    private ProgressBar progressBar;
    private boolean isRunning=false;
    private CheckOnlineReceiver checkOnlineReceiver;
    private ImageView imageView;
    private ArrayList<MovieTrailer> movieTrailer;
    private ArrayList<BackdropImages>backImages;
    private ArrayList<PosterImages>posterImages;
    private ArrayList<MovieReview> movieReviews;

    private RecyclerView mRecyclerView;
private ThrailerAdapter thrailerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);


        initViews();




    }

    private void initViews() {
       // movie_title,release_date,tv_duration,tv_rat

        movie_title=(TextView)findViewById(R.id.tv_movie_title);
        release_date=(TextView)findViewById(R.id.tv_release_date);
        tv_duration=(TextView)findViewById(R.id.tv_duration);
        tv_rat=(TextView)findViewById(R.id.tv_rate);
        tv_overview=(TextView)findViewById(R.id.tv_overview);
        poster=(ImageView)findViewById(R.id.poster);
        poster_back=(ImageView)findViewById(R.id.bacckground_poster);
        progressBar=(ProgressBar)findViewById(R.id.pb2_loading_indicator);
        imageView=(ImageView)findViewById(R.id.imageView);

        id=getIntent().getIntExtra(MainActivity.MOVIE_ID,0);

        // fetchMovieById();

        checkOnlineReceiver=new CheckOnlineReceiver(this);



        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerTrailer);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getQuery()){

                    Uri uri = MovieContract.MovieEntry.CONTENT_URI_SINGLE;
                    uri=uri.buildUpon().appendPath(MovieContract.MovieEntry.COLUMN_MOVIES_IDES).build();
                    uri=uri.buildUpon().appendPath(Integer.toString(movieShape.getId())).build();
                    imageView.setImageResource(R.drawable.fav_no);
                   int n= getContentResolver().delete(uri,null,null);
                    if(n>0){
                        Toast.makeText(MovieDetail.this, " id deleted success "+movieShape.getId(), Toast.LENGTH_SHORT).show();
                    }


                }else{
                    imageView.setImageResource(R.drawable.fav_yes);
                    ContentValues values=new ContentValues();
                    values.put(MovieContract.MovieEntry.COLUMN_MOVIES_IDES,movieShape.getId());


                    // TODO (12) Insert new task data via a ContentResolver

                    Uri uri=getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,values);

                    // TODO (13) Display the URI that's returned with a Toast
                    // [Hint] Don't forget to call finish() to return to MainActivity after this insert is complete
                    if (uri!=null){
                        Toast.makeText(MovieDetail.this, " success "+uri.toString(), Toast.LENGTH_SHORT).show();
                    }
                }




            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(checkOnlineReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(checkOnlineReceiver);
    }

    public void fetchMovieById() {
        progressBar.setVisibility(View.VISIBLE);


        RequestMovieInterface requestMovieInterface= ApiRetrofitClient.getApiRetrofitClient().create(RequestMovieInterface.class);
        Call<MovieShape> call=requestMovieInterface.getMovieById(id,MainActivity.API_KEY);

        call.enqueue(new Callback<MovieShape>() {
            @Override
            public void onResponse(Call<MovieShape> call, Response<MovieShape> response) {
                progressBar.setVisibility(View.INVISIBLE);

                 movieShape=response.body();

                 setViews();
                if(getQuery()){
                    imageView.setImageResource(R.drawable.fav_yes);
                }else{
                    imageView.setImageResource(R.drawable.fav_no);
                }



                isRunning=true;

            }

            @Override
            public void onFailure(Call<MovieShape> call, Throwable t) {
                Toast.makeText(MovieDetail.this, " Error->"+t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

    private boolean getQuery() {
        boolean b=false;

        Uri uri = MovieContract.MovieEntry.CONTENT_URI_SINGLE;
        uri=uri.buildUpon().appendPath(MovieContract.MovieEntry.COLUMN_MOVIES_IDES).build();
        uri=uri.buildUpon().appendPath(Integer.toString(movieShape.getId())).build();

        //  uri.withAppendedPath(uri,"22");

        Log.e(" uri ",uri.toString());

        Cursor cursor = getContentResolver().query(uri, null,
                null, null, null);

        while (cursor.moveToNext()) {
            if (cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIES_IDES)) == movieShape.getId()) {
                Toast.makeText(this, " founded " + movieShape.getId(), Toast.LENGTH_SHORT).show();
                b = true;
            }
        }
        return b;



      /*  while (cursor.moveToNext()) {
            int d = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIES_IDES);

            int i_d = cursor.getInt(d);

            Log.e(" id =",Integer.toString(i_d));


            if (i_d == 22) {
                Toast.makeText(this, " founded " + i_d, Toast.LENGTH_SHORT).show();
            }

        }*/

    }

    private void setViews() {
        // movie_title,release_date,tv_duration,tv_rat
        //poster,poster_back;
        Log.i(" Moive Shape ",movieShape.getTitle());
        movie_title.setText(movieShape.getTitle());
        release_date.setText(movieShape.getReleaseDate());
        tv_overview.setText(movieShape.getOverview());

        String string=Float.toString(movieShape.getVoteAverage());

        tv_rat.setText(string+"/10");

        String in=Integer.toString(movieShape.getRuntime());

        tv_duration.setText(in+" mins ");


        Picasso.with(this)
                .load("http://image.tmdb.org/t/p/w185//"+movieShape.getPosterPath())
                .into(poster);

        Picasso.with(this)
                .load("http://image.tmdb.org/t/p/w342//"+movieShape.getBackdropPath())
                .into(poster_back);
//        tv_duration.setText((int) movieShape.getRuntime());

    }


    @Override
    public void onChanged(boolean changed) {

        if(changed){
            if(!isRunning){
                fetchMovieById();
                fetchMovieVideos();
                fetchMovieReview();

            }

        }else {
            Toast.makeText(this, getString(R.string.connectivity), Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchMovieReview() {

        RequestMovieInterface requestMovieInterface=ApiRetrofitClient.getApiRetrofitClient().create(RequestMovieInterface.class);
        Call<MovieReviewResponse> call=requestMovieInterface.getMovieReviews(id,MainActivity.API_KEY);
        call.enqueue(new Callback<MovieReviewResponse>() {
            @Override
            public void onResponse(Call<MovieReviewResponse> call, Response<MovieReviewResponse> response) {

                movieReviews=new ArrayList<MovieReview>(response.body().getResults());
                if(movieReviews!=null) {
               //     Log.e(" author ",movieReviews.get(0).getAuthor()+"\n"+movieReviews.get(0).getContent());
                }

            }

            @Override
            public void onFailure(Call<MovieReviewResponse> call, Throwable t) {

            }
        });


    }

    private void fetchMovieVideos() {
        RequestMovieInterface requestMovieInterface= ApiRetrofitClient.getApiRetrofitClient().create(RequestMovieInterface.class);
        Call<MovieTrailerResponse> call=requestMovieInterface.getMovieVideo(id,MainActivity.API_KEY);

        call.enqueue(new Callback<MovieTrailerResponse>() {
            @Override
            public void onResponse(Call<MovieTrailerResponse> call, Response<MovieTrailerResponse> response) {
                 movieTrailer=new ArrayList<MovieTrailer>(response.body().getResults());
                Log.e(" trailer : ",movieTrailer.get(0).getKey());
                fetchMovieImage();
            }

            @Override
            public void onFailure(Call<MovieTrailerResponse> call, Throwable t) {

            }
        });



    }

    private void fetchMovieImage() {
      //  RequestMovieInterface requestMovieInterface= ApiRetrofitClient.getApiRetrofitClient().create(RequestMovieInterface.class);
      //  Call<MovieTrailerResponse> call=requestMovieInterface.getMovieVideo(id,MainActivity.API_KEY);

            RequestMovieInterface requestMovieInterface = ApiRetrofitClient.getApiRetrofitClient().create(RequestMovieInterface.class);
            Call<MovieImagesResponse> call=requestMovieInterface.getMovieImages(id,MainActivity.API_KEY);

            call.enqueue(new Callback<MovieImagesResponse>() {
                @Override
                public void onResponse(Call<MovieImagesResponse> call, Response<MovieImagesResponse> response) {

                    backImages=new ArrayList<BackdropImages>(response.body().getBackdrops());

                    posterImages=new ArrayList<PosterImages>(response.body().getPosters());



                    Log.e("traile"," hi from response "+backImages.get(0).getFilePath());
                    Log.e("traile"," hi from response "+posterImages.get(0).getFilePath());

                    thrailerAdapter=new ThrailerAdapter(movieTrailer,backImages,MovieDetail.this);
                    mRecyclerView.setAdapter(thrailerAdapter);


                }

                @Override
                public void onFailure(Call<MovieImagesResponse> call, Throwable t) {

                    Toast.makeText(MovieDetail.this, " error "+t.getMessage(), Toast.LENGTH_LONG).show();

                }
            });

        }


}
