package com.example.android.movieapp.utilities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by medo on 29-Sep-17.
 */

public class MovieImagesResponse {


        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("backdrops")
        @Expose
        private List<BackdropImages> backdrops = null;
        @SerializedName("posters")
        @Expose
        private List<PosterImages> posters = null;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public List<BackdropImages> getBackdrops() {
            return backdrops;
        }

        public void setBackdrops(List<BackdropImages> backdrops) {
            this.backdrops = backdrops;
        }

        public List<PosterImages> getPosters() {
            return posters;
        }

        public void setPosters(List<PosterImages> posters) {
            this.posters = posters;
        }

    }
