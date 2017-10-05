package com.example.android.movieapp;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.movieapp.Adapters.MovieAdapter;
import com.example.android.movieapp.BroadCastRecevier.CheckOnlineReceiver;
import com.example.android.movieapp.contentprovider.MovieContract;
import com.example.android.movieapp.utilities.ApiRetrofitClient;
import com.example.android.movieapp.utilities.MovieResponse;
import com.example.android.movieapp.utilities.MovieShape;
import com.example.android.movieapp.utilities.RequestMovieInterface;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements CheckOnlineReceiver.MyInterFace, MovieAdapter.OnItemClicked, LoaderManager.LoaderCallbacks<Cursor> {


    public final static String API_KEY = "f8f76039dc303e0c702a8e6423b987f3";
    private static final String LIST_STATE_KEY = "layout";
    private static final int TASK_LOADER_ID = 0;
    public static String MOVIE_ID = "movie_id";
    int finalId_ = 0;
    ArrayList<MovieShape> movies2 = new ArrayList<>();
    private MovieAdapter movieAdapter;
    private RecyclerView mRecyclerView;
    private ArrayList<MovieShape> movies;
    private ProgressBar progressBar;
    private boolean isRunning = false;
    private boolean isFinished = false;
    private RecyclerView.LayoutManager layoutManager;
    private CheckOnlineReceiver checkOnlineReceiver;
    private Parcelable mListState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);

        progressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);


        //======================= when get connected====================

        checkOnlineReceiver = new CheckOnlineReceiver(this);


        if (mListState != null) {
            Toast.makeText(this, " resotr", Toast.LENGTH_SHORT).show();
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mListState);
        }


        // loadPopularMovies();
        // getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);


    }


    @Override
    protected void onResume() {
        super.onResume();
        // getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, MainActivity.this);

        registerReceiver(
                checkOnlineReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        );

    }


    @Override
    protected void onPause() {
        mListState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(checkOnlineReceiver);
    }


    public void loadPopularMovies() {
        progressBar.setVisibility(View.VISIBLE);
        if (movies != null) {
            movies.clear();
            movieAdapter = new MovieAdapter(movies, MainActivity.this, MainActivity.this);
            mRecyclerView.setAdapter(movieAdapter);
        }


        RequestMovieInterface requestMovieInterface = ApiRetrofitClient.getApiRetrofitClient().create(RequestMovieInterface.class);
        Call<MovieResponse> call = requestMovieInterface.getPopularMovies(API_KEY);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {

                progressBar.setVisibility(View.INVISIBLE);

                try {

                    movies = new ArrayList<>(Arrays.asList(response.body().getResults()));
                    movies2 = movies;
                    movieAdapter = new MovieAdapter(movies, MainActivity.this, MainActivity.this);
                    mRecyclerView.setAdapter(movieAdapter);
                    isRunning = true;
                } catch (Exception e) {
                    Log.d(" Error", e.getMessage().toString());
                }


            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

                Toast.makeText(MainActivity.this, " error " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        //  Log.i("movies2",movies2.get(1).getTitle());
        // return movies2;
    }


    public void loadTopRatedMovies() {
        progressBar.setVisibility(View.VISIBLE);
        if (movies != null) {
            movies.clear();
            movieAdapter = new MovieAdapter(movies, MainActivity.this, this);
            mRecyclerView.setAdapter(movieAdapter);
        }

        RequestMovieInterface requestMovieInterface = ApiRetrofitClient.getApiRetrofitClient().create(RequestMovieInterface.class);
        Call<MovieResponse> call = requestMovieInterface.getTopRatedMovies(API_KEY);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                progressBar.setVisibility(View.INVISIBLE);


                try {
                    movies = new ArrayList<>(Arrays.asList(response.body().getResults()));
                    movieAdapter = new MovieAdapter(movies, MainActivity.this, MainActivity.this);
                    mRecyclerView.setAdapter(movieAdapter);
                } catch (Exception e) {
                    Log.d(" Error", e.getMessage().toString());
                }


            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

                Toast.makeText(MainActivity.this, " error " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        SharedPreferences preferences = getSharedPreferences("MyMovies", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();


        if (item.getItemId() == R.id.top_rated) {
            editor.putString("movie", "top_rated");
            editor.commit();
            loadTopRatedMovies();

        } else if (item.getItemId() == R.id.most_popular) {
            editor.putString("movie", "most_popular");
            editor.commit();
            loadPopularMovies();
        } else if (item.getItemId() == R.id.favorite) {
            editor.putString("movie", "favorite");
            editor.commit();
            getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, MainActivity.this);

        }
        return true;
    }

    private void fetchMovieById(final ArrayList<Integer> ides) {
        //Toast.makeText(this, " hi from fetch ", Toast.LENGTH_SHORT).show();
        movies = new ArrayList<>();
/*        progressBar.setVisibility(View.VISIBLE);
        if(movies !=null) {
            movies.clear();
            movieAdapter = new ThrailerAdapter(movies, MainActivity.this,MainActivity.this);
            mRecyclerView.setAdapter(movieAdapter);
        }*/

        for (int id_1 = 0; id_1 < ides.size(); id_1++) {

/*            if(id_1==ides.size()-1){
                isFinished=true;
               // Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                Log.d(" id fini ===> ",Integer.toString(id_1));
            }else{
                isFinished=false;
                Log.d(" id ===> ",Integer.toString(id_1));
            }*/
            RequestMovieInterface requestMovieInterface = ApiRetrofitClient.getApiRetrofitClient().create(RequestMovieInterface.class);
            Call<MovieShape> call = requestMovieInterface.getMovieById(ides.get(id_1), API_KEY);
            if (id_1 == ides.size() - 1) {
                finalId_ = id_1;
            }
            call.enqueue(new Callback<MovieShape>() {
                @Override
                public void onResponse(Call<MovieShape> call, Response<MovieShape> response) {


                    MovieShape m = response.body();
                    movies.add(m);


                    // Toast.makeText(this, " hi from fetch "+movies.get(2).getId(), Toast.LENGTH_SHORT).show();
/*                   if(isFinished==false) {
                       Log.d("title:===>", m.getTitle());
                   }*/
                 /*  if(finalId_==ides.size()-1){
                       progressBar.setVisibility(View.INVISIBLE);*/
                    Log.d("title:===>", m.getTitle());
//                       Log.d("title:===> by movies", movies.get(2).getTitle());
                    movieAdapter = new MovieAdapter(movies, MainActivity.this, MainActivity.this);
                    mRecyclerView.setAdapter(movieAdapter);
                   movieAdapter.notifyDataSetChanged();
                    //  }
                    // isRunning=true;

                }

                @Override
                public void onFailure(Call<MovieShape> call, Throwable t) {
                    Toast.makeText(MainActivity.this, " Error->" + t.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        }

//        Toast.makeText(MainActivity.this, "title  "+movies.get(2).getTitle(), Toast.LENGTH_SHORT).show();


    }

    @Override
    protected void onStart() {
        super.onStart();



    }

    @Override
    protected void onRestart() {
        super.onRestart();


    }

    @Override
    public void onChanged(boolean changed) {

    if(changed){
            if(!isRunning){
                SharedPreferences preferences = getSharedPreferences("MyMovies", Context.MODE_PRIVATE);
                // SharedPreferences.Editor editor=preferences.edit();
                String s = preferences.getString("movie", "");


                if (s.equals("top_rated")) {
                    loadTopRatedMovies();
                } else if (s.equals("most_popular")) {
                    loadPopularMovies();
                } else if (s.equals("favorite")) {
                    getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, MainActivity.this);
                }
            }
        }

    }

    @Override
    public void onItemClickedLisener(int id) {
        Intent intent = new Intent(MainActivity.this, MovieDetail.class);
        intent.putExtra(MOVIE_ID, id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the task data
            Cursor mTaskData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
/*               if (mTaskData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mTaskData);
                } else {*/
                // Force a new load
/*                   if(movies!=null) {
                       movies.clear();
                       movieAdapter = new ThrailerAdapter(movies, MainActivity.this, MainActivity.this);
                       mRecyclerView.setAdapter(movieAdapter);
                       movieAdapter.notifyDataSetChanged();
                   }*/
                forceLoad();

                // }
/*                progressBar.setVisibility(View.VISIBLE);
                if(movies!=null) {
                    movies.clear();
                    movieAdapter = new ThrailerAdapter(movies, MainActivity.this, MainActivity.this);
                    mRecyclerView.setAdapter(movieAdapter);
                    movieAdapter.notifyDataSetChanged();
                }

                    forceLoad();*/

            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data
                // TODO (5) Query and load all task data in the background; sort by priority
                // [Hint] use a try/catch block to catch any errors in loading data
                try {
                    return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null,
                            null, null, MovieContract.MovieEntry.COLUMN_MOVIES_IDES);
                } catch (Exception e) {
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
/*           public void deliverResult(Cursor data) {
               mTaskData = data;
                super.deliverResult(data);
            }*/
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
/*
        Cursor cursor= getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,null,
                null,null, MovieContract.MovieEntry._ID);*/
        if (data != null && data.getCount() > 0) {
            progressBar.setVisibility(View.INVISIBLE);
            ArrayList<Integer> ides = new ArrayList<>();
            while (data.moveToNext()) {
                int id = data.getInt(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIES_IDES));

                ides.add(id);
                Log.e(" id == ", Integer.toString(id));

            }
            if (data != null) {
                fetchMovieById(ides);
            }

        } else {
            if (movies != null) {
                movies.clear();
                movieAdapter = new MovieAdapter(movies, MainActivity.this, MainActivity.this);
                mRecyclerView.setAdapter(movieAdapter);
                movieAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
/*       movies.clear();
         movieAdapter=new ThrailerAdapter(movies,MainActivity.this,MainActivity.this);
        mRecyclerView.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();*/
    }
}


