
package com.example.android.movieapp.contentprovider;

import android.net.Uri;
import android.provider.BaseColumns;


public class MovieContract {
            /* TODO (1) Add content provider constants to the Contract
     Clients need to know how to access the task data, and it's your job to provide
     these content URI's for the path to that data:
        1) Content authority,
        2) Base content URI,
        3) Path(s) to the tasks directory
        4) Content URI for data in the TaskEntry class
      */

    public static final String AUTHORITY="com.example.android.movieapp";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+AUTHORITY);

    public static final String MOVIES_PATH="movies_ids";

    /* TaskEntry is an inner class that defines the contents of the task table */
    public static final class MovieEntry implements BaseColumns {


        //TODO (2) build the complete content uri
        public static final String COLUMN_MOVIES_IDES= "ides";

        public static Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().path(MOVIES_PATH).build();
        public static Uri CONTENT_URI_SINGLE=BASE_CONTENT_URI.buildUpon().path(MOVIES_PATH).build();

        // Task table and column names
        public static final String TABLE_NAME = "movies_ids";

        // Since TaskEntry implements the interface "BaseColumns", it has an automatically produced
        // "_ID" column in addition to the two below
       // public static final String COLUMN_DESCRIPTION = "description";



        /*
        The above table structure looks something like the sample table below.
        With the name of the table and columns on top, and potential contents in rows

        Note: Because this implements BaseColumns, the _id column is generated automatically

        movies_ids
         - - - - - - - - - - - - - - - - - - - - - -
        | _id      |    ides   |
         - - - - - - - - - - - - - - - - - - - - - -
        |  1      |       1       |
         - - - - - - - - - - - - - - - - - - - - - -
        |  2     |       3       |
         - - - - - - - - - - - - - - - - - - - - - -
        .
        .
        .
         - - - - - - - - - - - - - - - - - - - - - -
        | 43   |     2       |
         - - - - - - - - - - - - - - - - - - - - - -

         */

    }
}
