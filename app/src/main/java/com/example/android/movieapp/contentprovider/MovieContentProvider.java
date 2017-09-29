/*
* Copyright (C) 2016 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.android.movieapp.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

// TODO (1) Verify that TaskContentProvider extends from ContentProvider and implements required methods
public class MovieContentProvider extends ContentProvider {


    // TODO (3) Define final integer constants for the directory of tasks and a single item.
    // It's convention to use 100, 200, 300, etc for directories,
    // and related ints (101, 102, ..) for items in that directory.

    //for directory
    public static final int MOVIES=100;
    public static final int SINGLE_MOVIE=101;


    // TODO (4) Declare a static variable for the Uri matcher that you construct
    private static final UriMatcher sUriMatcher=buildUriMatcher();

    // TODO (5) Define a static buildUriMatcher method that associates URI's with their int match
    public static UriMatcher buildUriMatcher(){

        UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.MOVIES_PATH,MOVIES);
        uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.MOVIES_PATH+"/"+ MovieContract.MovieEntry.COLUMN_MOVIES_IDES+"/#",SINGLE_MOVIE);

        return uriMatcher;

    }






    /* onCreate() is where you should initialize anything you’ll need to setup
    your underlying data source.
    In this case, you’re working with a SQLite database, so you’ll need to
    initialize a DbHelper to gain access to it.
    */

    private MovieDbHelper mTaskDbHelper;
    @Override
    public boolean onCreate() {
        // TODO (2) Complete onCreate() and initialize a TaskDbhelper on startup
        // [Hint] Declare the DbHelper as a global variable
        Context context=getContext();
        mTaskDbHelper=new MovieDbHelper(context);

        return true;
    }


    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {


        // TODO (6) Get access to the task database (to write new data to)
      final SQLiteDatabase db= mTaskDbHelper.getWritableDatabase();

        // TODO (7) Write URI matching code to identify the match for the tasks directory
        int uriMatcher=sUriMatcher.match(uri);


        // TODO (8) Insert new values into the database
        // TODO (9) Set the value for the returnedUri and write the default case for unknown URI's

        Uri returnUri=null;

        switch (uriMatcher){
            case MOVIES:
                long id=db.insert(MovieContract.MovieEntry.TABLE_NAME,null,values);

                if(id>0){
                    Toast.makeText(getContext(), " inserted success ", Toast.LENGTH_SHORT).show();
                    returnUri= ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI,id);
                }else
                {
                    throw new SQLException("Fialed to insert into row "+uri);
                }


                  break;

                default:
                    throw new UnsupportedOperationException("unknown URI's"+uri);
        }

        // TODO (10) Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri,null);

           return returnUri;


    }


    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // TODO (1) Get access to underlying database (read-only for query)
         final SQLiteDatabase db=mTaskDbHelper.getReadableDatabase();
        // TODO (2) Write URI match code and set a variable to return a Cursor
        int match=sUriMatcher.match(uri);

        Cursor rectCurrsor;
      // TODO (3) Query for the tasks directory and write a default case
        switch (match){
            case MOVIES:

                rectCurrsor=db.query(MovieContract.MovieEntry.TABLE_NAME,projection
                ,selection,selectionArgs,null,null,sortOrder
                );


                break;


                //for one item query

            case SINGLE_MOVIE:

                //<scheme>://<authority>/path/#

                String id=uri.getPathSegments().get(2);

                String mSelection="ides=?";
                String []mSelectionArgs=new String[]{id};

                rectCurrsor=db.query(MovieContract.MovieEntry.TABLE_NAME,projection
                        ,mSelection,mSelectionArgs,null,null,sortOrder
                );


                break;
                default:
                    throw new UnsupportedOperationException(" unknown uri "+uri);

        }



        // TODO (4) Set a notification URI on the Cursor and return that Cursor
        //to notify any change to the cursor
      rectCurrsor.setNotificationUri(getContext().getContentResolver(),uri);

      return rectCurrsor;
    }


    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        // TODO (1) Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db=mTaskDbHelper.getWritableDatabase();
        // TODO (2) Write the code to delete a single row of data
        // [Hint] Use selections to delete an item by its row ID

        int mach=sUriMatcher.match(uri);

        String id=uri.getPathSegments().get(2);
        String mSelection="ides=?";
        String []mSelectionArgs=new String[]{id};
       int num;

        switch (mach){
            case SINGLE_MOVIE:

               num= db.delete(MovieContract.MovieEntry.TABLE_NAME,mSelection,mSelectionArgs);

                break;
                default:
                    throw new UnsupportedOperationException(" unknown uri "+uri);
        }
        // TODO (3) Notify the resolver of a change and return the number of items deleted

        getContext().getContentResolver().notifyChange(uri,null);
        return num;
    }


    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {


        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public String getType(@NonNull Uri uri) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

}
