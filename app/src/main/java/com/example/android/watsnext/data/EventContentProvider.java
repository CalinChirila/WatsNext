package com.example.android.watsnext.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.example.android.watsnext.data.EventContract.EventsEntry;

/**
 * Created by Astraeus on 2/26/2018.
 */

public class EventContentProvider extends ContentProvider {

    private EventDbHelper mDbHelper;
    private Context mContext;
    private ContentResolver mContentResolver = mContext.getContentResolver();

    // Codes for the URI matcher
    private static final int EVENTS = 100;
    private static final int EVENT = 101;

    private static UriMatcher sUriMatcher = buildUriMatcher();

    // Helper method to build the uri matcher
    private static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // Add uri for all events
        uriMatcher.addURI(EventContract.AUTHORITY, EventContract.PATH_EVENTS, EVENTS);

        // Add uri for a specific event, based on its ID
        uriMatcher.addURI(EventContract.AUTHORITY, EventContract.PATH_EVENTS + "/#", EVENT);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        mContext = getContext();
        mDbHelper = new EventDbHelper(mContext);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);
        Cursor cursor;

        switch(match){
            case EVENTS:
                cursor = db.query(
                        EventsEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                );
                break;
            case EVENT:
                String newSelection = EventsEntry._ID + "=?";
                // Get the event's ID
                String[] newSelectionArgs = {uri.getPathSegments().get(1)};

                // Only read the event with a specific ID
                cursor = db.query(
                        EventsEntry.TABLE_NAME,
                        null,
                        newSelection,
                        newSelectionArgs,
                        null,
                        null,
                        null
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(mContentResolver, uri);

        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Uri returnUri;

        switch(match){
            // No need to add an event to a specific row, so only general case will be treated.
            case EVENTS:
                long id = db.insert(EventsEntry.TABLE_NAME, null, contentValues);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(EventsEntry.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert row into uri: " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Error occurred while inserting item with uri: " + uri);
        }

        mContentResolver.notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int deletedRows;

        switch(match){
            case EVENTS:
                // Delete all events
                deletedRows = db.delete(EventsEntry.TABLE_NAME, null, null);
                break;
            case EVENT:
                // Delete event with specified ID.
                String newSelection = EventsEntry._ID + "=?";
                String[] newSelectionArgs = {uri.getPathSegments().get(1)};
                deletedRows = db.delete(EventsEntry.TABLE_NAME, newSelection, newSelectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Error while deleting item with uri: " + uri);
        }

        if(deletedRows > 0){
            mContentResolver.notifyChange(uri, null);
        }
        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int updatedRows;

        switch(match){
            case EVENTS:
                // Update all events
                updatedRows = db.update(EventsEntry.TABLE_NAME, contentValues, null, null);
                break;
            case EVENT:
                // Update event with specified ID
                String newSelection = EventsEntry._ID + "=?";
                String[] newSelectionArgs = {uri.getPathSegments().get(1)};
                updatedRows = db.update(EventsEntry.TABLE_NAME, contentValues, newSelection, newSelectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Error updating item with uri: " + uri);
        }

        if(updatedRows > 0){
            mContentResolver.notifyChange(uri, null);
        }
        return updatedRows;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
