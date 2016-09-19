package com.android.sareen.a2z;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;


public class LevelActivity extends Activity
    implements SoundPool.OnLoadCompleteListener
{

    private static final String DIFFICULTY_EXTRA = "difficulty";

    private static final String SOUND_PREF = "sound_pref";

    private SoundPool mp;

    private static final int DIFFICULTY_BEGINNER = 1;
    private static final int DIFFICULTY_AMATEUR = 2;
    private static final int DIFFICULTY_EXPERT = 3;
    private static final int DIFFICULTY_PROFESSIONAL = 4;

    private static final int NO_OF_LEVELS = 26;

    private static final String LEVEL_EXTRA = "level";

    private static int difficulty;

    private static int level;

    private static int level_unlocked;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);


        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        difficulty = getIntent().getIntExtra(DIFFICULTY_EXTRA, 1);


        GridView gridView = (GridView)findViewById(R.id.level_gridview);
        gridView.setAdapter(new LevelAdapter(this));



        checkUnlockedLevels();

    }

    @Override
    public void onPause()
    {
        super.onPause();
        mp.release();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mp.setOnLoadCompleteListener(this);

        if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SOUND_PREF, true))

        {mp.load(this, R.raw.sound_level, 100);}

    }

    private void checkUnlockedLevels()
    {
        String pref_key = getResources().getString(R.string.pref_beg_level_unlock);
        switch (difficulty)
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
        level_unlocked = PreferenceManager.getDefaultSharedPreferences(this)
                .getInt(pref_key, getResources().getInteger(R.integer.pref_default_level_unlock));
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int i, int i1) {
        soundPool.play(i, 1.0f, 1.0f, 100, 0, 1);
    }

    public static class LevelAdapter extends BaseAdapter
    {


        private Context mContext;

        public LevelAdapter(Context c)
        {
            mContext = c;
        }

        public int getCount()
        {
            return NO_OF_LEVELS;
        }

        public Object getItem(int position)
        {
            return null;
        }

        public long getItemId(int position)
        {
            return 0;
        }

        public View getView(final int position, View convertView, ViewGroup parent)
        {


            if(level_unlocked >= position + 1)
            {
                Button button;
                button = new Button(mContext);
                button.setLayoutParams(new GridView.LayoutParams(
                        (int) mContext.getResources().getDimension(R.dimen.button_height),
                        (int) mContext.getResources().getDimension(R.dimen.button_width)
                ));
                button.setGravity(Gravity.CENTER);

                // set the style of button
                button.setBackgroundResource(R.drawable.flat_selector_level);
                button.setTextColor(mContext.getResources().getColor(R.color.text_color_a2z));
                button.setTextSize(22);
                button.setText(mLevelName[position]);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        level = position + 1;
                        if (level <= level_unlocked) {
                            Intent intent = new Intent(mContext, GameActivity.class);
                            intent.putExtra(DIFFICULTY_EXTRA, difficulty);
                            intent.putExtra(LEVEL_EXTRA, level);
                            mContext.startActivity(intent);
                        } else {
                            Toast.makeText(mContext, "Level Locked", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                return button;
            }
            else
            {
                View v = View.inflate(mContext, R.layout.lock_view, null);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        level = position + 1;
                        if (level <= level_unlocked) {
                            Intent intent = new Intent(mContext, GameActivity.class);
                            intent.putExtra(DIFFICULTY_EXTRA, difficulty);
                            intent.putExtra(LEVEL_EXTRA, level);
                            mContext.startActivity(intent);
                        } else {
                            Toast.makeText(mContext, "Level Locked", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                return v;
            }





        }

        private String[] mLevelName =
                {
                        "1", "2", "3", "4", "5",
                        "6", "7", "8", "9", "10",
                        "11", "12", "13", "14", "15",
                        "16", "17", "18", "19", "20",
                        "21", "22", "23", "24", "25",
                        "26"
                };

    }


}
