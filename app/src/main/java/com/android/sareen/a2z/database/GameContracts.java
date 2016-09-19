package com.android.sareen.a2z.database;

import android.provider.BaseColumns;

/**
 * Created by Ashish on 29-Jul-15.
 */
public class GameContracts
{
    public static final class GameEntry implements BaseColumns
    {

        public static final String TABLE_NAME = "games";

        public static final String COLUMN_WORD = "word";

        public static final String COLUMN_MEANING = "meaning";

        public static final String COLUMN_INPUT_CHARACTERS = "input_characters";

        public static final String COLUMN_DIFFICULTY = "difficulty";

        public static final String COLUMN_LEVEL = "level";
    }
}
