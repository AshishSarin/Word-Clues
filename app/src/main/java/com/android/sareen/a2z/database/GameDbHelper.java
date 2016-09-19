package com.android.sareen.a2z.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.android.sareen.a2z.database.GameContracts.GameEntry;

/**
 * Created by Ashish on 29-Jul-15.
 */
public class GameDbHelper extends SQLiteOpenHelper
{
    private static final int DATABSE_VERSION = 1;

    private static final String DATABASE_NAME = "a2z";

    public GameDbHelper(Context c)
    {
        super(c, DATABASE_NAME, null, DATABSE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        final String CREATE_GAMES_TABLE = "CREATE TABLE IF NOT EXISTS " + GameEntry.TABLE_NAME + " (" +
                GameEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                GameEntry.COLUMN_WORD + " TEXT NOT NULL, " +
                GameEntry.COLUMN_MEANING + " TEXT NOT NULL, " +
                GameEntry.COLUMN_INPUT_CHARACTERS + " TEXT NOT NULL, " +
                GameEntry.COLUMN_DIFFICULTY + " INTEGER NOT NULL, " +
                GameEntry.COLUMN_LEVEL + " INTEGER NOT NULL);";

        db.execSQL(CREATE_GAMES_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + GameEntry.TABLE_NAME);
        onCreate(db);
    }

}
