package com.android.sareen.a2z;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.android.sareen.a2z.database.GameContracts.GameEntry;
import com.android.sareen.a2z.database.GameDbHelper;

public class GameActivity extends FragmentActivity
        implements GameOverDialogFragment.GameOverDialogListener, LoaderManager.LoaderCallbacks<Cursor>,
        SoundPool.OnLoadCompleteListener, DialogInterface.OnDismissListener
{


    private boolean mBlinkAnim = false;

    private boolean mSound;

    private static final String SOUND_PREF = "sound_pref";

    private GameDbHelper db = new GameDbHelper(this);

    private static final String DIFFICULTY_EXTRA = "difficulty";

    private static final int DIFFICULTY_BEGINNER = 1;
    private static final int DIFFICULTY_AMATEUR = 2;
    private static final int DIFFICULTY_EXPERT = 3;
    private static final int DIFFICULTY_PROFESSIONAL = 4;

    private static final String LEVEL_EXTRA = "level";

    private GameObject gameObject;
    
    int right_sound_id = R.raw.sound_character;
    int level_sound_id = R.raw.sound_level;
    int wrong_sound_id  =R.raw.sound_wrong;
    private SoundPool soundPool;

    private TextView[] wrong_characters_textview = new TextView[4];
    private TextView meaning_textview;
    private TextView[] right_characters_textview = new TextView[8];
    private TextView[] input_characters_textview = new TextView[16];

    private int[] wrong_character_ids = new int[]
            {
                    R.id.wrong_character_1,
                    R.id.wrong_character_2,
                    R.id.wrong_character_3,
                    R.id.wrong_character_4
            };


    private int[] right_characters_ids = new int[]
            {
                    R.id.right_character_1,
                    R.id.right_character_2,
                    R.id.right_character_3,
                    R.id.right_character_4,
                    R.id.right_character_5,
                    R.id.right_character_6,
                    R.id.right_character_7,
                    R.id.right_character_8,
            };
    private int[] input_characters_ids = new int[]
            {
                    R.id.character_textview_1,
                    R.id.character_textview_2,
                    R.id.character_textview_3,
                    R.id.character_textview_4,
                    R.id.character_textview_5,
                    R.id.character_textview_6,
                    R.id.character_textview_7,
                    R.id.character_textview_8,
                    R.id.character_textview_9,
                    R.id.character_textview_10,
                    R.id.character_textview_11,
                    R.id.character_textview_12,
                    R.id.character_textview_13,
                    R.id.character_textview_14,
                    R.id.character_textview_15,
                    R.id.character_textview_16,
            };



    private static final int GAME_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        // intialize sound on/off variable
        mSound = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(SOUND_PREF, true);

        gameObject = new GameObject();
        gameObject.difficulty = getIntent().getIntExtra(DIFFICULTY_EXTRA, 1);
        gameObject.level = getIntent().getIntExtra(LEVEL_EXTRA, 1);

        getSupportLoaderManager().initLoader(GAME_LOADER, null, this);


        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(this);
        if(mSound)
        {
            soundPool.load(this, level_sound_id, 1);
        }
        // play
    }



    @Override
    public void onResume()
    {
        super.onResume();

    }


    private void initializeViews()
    {
        // Initializtion
        for(int i=0; i<4;i++)
        {
            wrong_characters_textview[i] = (TextView)findViewById(wrong_character_ids[i]);
            if(gameObject.wrong_characters[i] == '0')
            {
                wrong_characters_textview[i].setText(null);
            }
            else
            {
                wrong_characters_textview[i].setText(Character.toString(gameObject.wrong_characters[i]));
            }
        }


        meaning_textview = (TextView)findViewById(R.id.meaning_textview);
        meaning_textview.setText(gameObject.meaning);


        for(int i=0; i<gameObject.word.length(); i++)
        {
            right_characters_textview[i] = (TextView)findViewById(right_characters_ids[i]);
            if(gameObject.right_characters[i] == '0')
            {
                right_characters_textview[i].setBackgroundResource(R.drawable.rounded_right_characters_1);
                right_characters_textview[i].setText(null);
            }
            else
            {
                right_characters_textview[i].setBackgroundResource(R.drawable.rounded_right_characters_2);
                right_characters_textview[i].setText(Character.toString(gameObject.right_characters[i]));
            }
            right_characters_textview[i].setVisibility(View.VISIBLE);
        }
        for(int i=gameObject.word.length(); i<8;i++)
        {
            right_characters_textview[i] = (TextView)findViewById(right_characters_ids[i]);
            right_characters_textview[i].setVisibility(View.GONE);
        }

        for(int i=0; i<16; i++)
        {
            input_characters_textview[i] = (TextView)findViewById(input_characters_ids[i]);
            input_characters_textview[i].setText(Character.toString(gameObject.input_characters[i]));
            input_characters_textview[i].setVisibility(View.VISIBLE);
        }

        if(gameObject.level == 1 && gameObject.difficulty == 1)
        {
            playTutorial();
            input_characters_textview[12].startAnimation(AnimationUtils.loadAnimation(this, R.anim.blink));
            mBlinkAnim = true;
        }

    }





    private static final String GAME_ACTIVITY = GameActivity.class.getSimpleName();

    public void onCharacter(View view)
    {
        TextView textView = (TextView) view;
        char c = textView.getText().charAt(0);
        textView.setVisibility(View.INVISIBLE);
        if(mBlinkAnim && c == 'I')
        {
            view.clearAnimation();
            mBlinkAnim = false;
        }
        playCharacter(c);

    }

    private void playCharacter(char c) {
        boolean isFound = false;
        for(int i=0; i<gameObject.word.length(); i++)
        {

            if(c == gameObject.word.charAt(i))
            {
                // character found in thw word

                //play the right character sound
                if(mSound && !isFound && gameObject.right < (gameObject.word.length() - 1))      // check to see if sound is on/off
                {
                    soundPool.load(this, right_sound_id, 2);
                }

                isFound = true;
                gameObject.right_characters[i] = c;
                gameObject.right++;
                right_characters_textview[i].setText(Character.toString(c));
                right_characters_textview[i].setBackgroundResource(R.drawable.rounded_right_characters_2);
                if(gameObject.right >= gameObject.word.length())
                {
                    // Game Won
                    String pref_key = getResources().getString(R.string.pref_beg_level_unlock);
                    switch (gameObject.difficulty)
                    {
                        case DIFFICULTY_BEGINNER:
                            pref_key = getResources().getString(R.string.pref_beg_level_unlock);
                            break;

                        case DIFFICULTY_AMATEUR:
                            pref_key = getResources().getString(R.string.pref_ama_level_unlock);
                            break;

                        case DIFFICULTY_EXPERT:
                            pref_key = getResources().getString(R.string.pref_exp_level_unlock);
                            break;

                        case DIFFICULTY_PROFESSIONAL:
                            pref_key = getResources().getString(R.string.pref_pro_level_unlock);
                            break;
                    }
                    // Check to see if level need to be unlocked
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                    int levels_unlocked = prefs.getInt(pref_key, getResources().getInteger(R.integer.pref_default_level_unlock));
                    if(levels_unlocked == gameObject.level && gameObject.level != 26)
                    {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt(pref_key, gameObject.level + 1);
                        editor.commit();
                    }


                    GameOverDialogFragment dialogFragment = GameOverDialogFragment.getInstance(true, gameObject.level, gameObject.word);
                    dialogFragment.show(getSupportFragmentManager(), "game_over");
                }
            }
        }
        if(!isFound)
        {

            gameObject.wrong_characters[gameObject.wrong] = c;
            wrong_characters_textview[gameObject.wrong].setText(Character.toString(c));
            gameObject.wrong++;


            if(gameObject.wrong <= 3)
            {
                // Shake Animation
                ViewGroup v = (ViewGroup) findViewById(R.id.wrong_layout);
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.shake);
                v.startAnimation(animation);
            }

            // character not found
            if(mSound && gameObject.wrong < 4)
            {
                soundPool.load(this, wrong_sound_id, 2);
            }

            if(gameObject.wrong >= 4)
            {
                // Game Lost
                GameOverDialogFragment dialogFragment = GameOverDialogFragment.getInstance(false, gameObject.level, gameObject.word);
                dialogFragment.show(getSupportFragmentManager(), "game_over");
            }

        }
    }


    @Override
    public void onPause()
    {
        super.onPause();
        soundPool.release();
    }

    @Override
    public void onDialogContinue(boolean w)
    {
        int prevLevel = gameObject.level;
        if(!w)
        {
            // Not win and retry
            gameObject = new GameObject();
            gameObject.level = prevLevel;
            gameObject.difficulty = getIntent().getIntExtra(DIFFICULTY_EXTRA, DIFFICULTY_BEGINNER);
            getSupportLoaderManager().restartLoader(GAME_LOADER, null, this);
        }
        else
        {
            if(prevLevel <= 25)
            {
                gameObject = new GameObject();
                gameObject.level = prevLevel + 1;
                gameObject.difficulty = getIntent().getIntExtra(DIFFICULTY_EXTRA, DIFFICULTY_BEGINNER);
                getSupportLoaderManager().restartLoader(GAME_LOADER, null, this);
            }
            else
            {
                finish();
                Intent intent  = new Intent(this, DifficultyActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onDialogQuit()
    {
        finish();
        Intent intent = new Intent(this, LevelActivity.class);
        intent.putExtra(DIFFICULTY_EXTRA, gameObject.difficulty);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        return new android.support.v4.content.CursorLoader(this)
        {
            @Override
            public Cursor loadInBackground()
            {

                String level = Integer.toString(gameObject.level);
                String difficulty = Integer.toString(gameObject.difficulty);
                String[] projection = new String[]
                        {
                                GameEntry.COLUMN_WORD,
                                GameEntry.COLUMN_MEANING,
                                GameEntry.COLUMN_INPUT_CHARACTERS
                        };

                String selection = GameEntry.COLUMN_DIFFICULTY + " = ?" +
                        " AND " + GameEntry.COLUMN_LEVEL + " = ? ";
                String[] selectionArgs = new String[]
                        {
                                difficulty, level
                        };
                String orderBy = GameEntry._ID + " ASC";

                Cursor cursor = db.getWritableDatabase().query(GameEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs, null, null, orderBy);

                return cursor;
            }
        };


    }



    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        if(data.moveToFirst())
        {

            gameObject.word = data.getString(data.getColumnIndex(GameEntry.COLUMN_WORD)).toUpperCase();
            String inputString = data.getString(data.getColumnIndex(GameEntry.COLUMN_INPUT_CHARACTERS));

            // converting string of input characters into array of characters
            for(int i=0;i<16;i++)
            {
                gameObject.input_characters[i] = inputString.charAt(i);
            }

            gameObject.meaning = data.getString(data.getColumnIndex(GameEntry.COLUMN_MEANING));
            initializeViews();



        }
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int i, int i1)
    {
        if(i1 == 0)
        {
            soundPool.play(i,1.0f, 1.0f, 2,0, 1);
        }

    }

    @Override
    public void onPlaySound(boolean w)
    {
        if(PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(SOUND_PREF, true))
        {
            int soundId;
            if (w)
            {
                soundId = R.raw.sound_win;
            }
            else
            {
                soundId = R.raw.sound_loose;
            }


            soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
            soundPool.setOnLoadCompleteListener(this);
            soundPool.load(this, soundId, 2);
        }

    }

    @Override
    public void onDismiss(DialogInterface dialogInterface)
    {
        onDialogQuit();
    }

    private void playTutorial()
    {

        mSound = false;
        playCharacter('R');
        playCharacter('E');
        playCharacter('P');
        playCharacter('A');
        mSound = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(SOUND_PREF, true);


    }
}




class GameObject
{
    public int difficulty;
    public int level;

    public String word;                                     // Original Word
    public String meaning;                                  // the phrase shown
    public char[] wrong_characters = new char[4];           // array of wrong characters
    int right = 0;                                          // no of right characters
    int wrong = 0;                                          // no of wrong characters

    char[] input_characters = new char[16];                 // character to display on input textview
    char[] right_characters = new char[8];

    public GameObject()
    {
        word = "HABIT";
        meaning = "something that a person does very often or in a repeated way";
        wrong_characters = new char[]
                {
                        '0', '0', '0', '0'
                };
        right = 0;
        wrong = 0;
        input_characters = new char[]
                {
                        'Z', 'C', 'A', 'K', 'L', 'T', 'P', 'D',
                        'H', 'R', 'W', 'B', 'G', 'M', 'I', 'S'
                };
        right_characters = new char[]
                {
                        '0', '0', '0', '0', '0', '0', '0', '0'
                };
    }
}

