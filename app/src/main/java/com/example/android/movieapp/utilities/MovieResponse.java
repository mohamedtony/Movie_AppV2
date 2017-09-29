package com.example.android.movieapp.utilities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mohamed tony  on 03-Sep-17.
 */

public class MovieResponse {
    @SerializedName("results")
    @Expose
    public MovieShape[] results;

    public MovieShape[] getResults() {
        return results;
    }

}
