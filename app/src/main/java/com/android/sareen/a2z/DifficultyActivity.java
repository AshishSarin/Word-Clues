package com.android.sareen.a2z;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


public class DifficultyActivity extends Activity
    implements SoundPool.OnLoadCompleteListener
{

    TextView tv_diff_unlock;
    TextView tv_level;
    TextView tv_diff_lock;


    private static final String SOUND_PREF = "sound_pref";

    private static final String DIFFICULTY_EXTRA = "difficulty";
    private static final int DIFFICULTY_BEGINNER = 1;
    private static final int DIFFICULTY_AMATEUR = 2;
    private static final int DIFFICULTY_EXPERT = 3;
    private static final int DIFFICULTY_PROFESSIONAL = 4;

    private SoundPool mp;
    private int beg_levels_unlocked;
    private int pro_levels_unlocked;
    private int exp_levels_unlocked;
    private int ama_levels_unlocked;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);


        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        ViewGroup beg_view = (ViewGroup)findViewById(R.id.beginner_layout);
        ViewGroup ama_view = (ViewGroup)findViewById(R.id.amateur_layout);
        ViewGroup exp_view = (ViewGroup)findViewById(R.id.expert_layout);
        ViewGroup pro_view = (ViewGroup)findViewById(R.id.professional_layout);

        beg_levels_unlocked = PreferenceManager.getDefaultSharedPreferences(this)
                .getInt(getResources().getString(R.string.pref_beg_level_unlock),
                        getResources().getInteger(R.integer.pref_default_level_unlock));

        ama_levels_unlocked = PreferenceManager.getDefaultSharedPreferences(this)
                .getInt(getResources().getString(R.string.pref_ama_level_unlock),
                        getResources().getInteger(R.integer.pref_default_level_unlock));

        exp_levels_unlocked = PreferenceManager.getDefaultSharedPreferences(this)
                .getInt(getResources().getString(R.string.pref_exp_level_unlock),
                        getResources().getInteger(R.integer.pref_default_level_unlock));

        pro_levels_unlocked = PreferenceManager.getDefaultSharedPreferences(this)
                .getInt(getResources().getString(R.string.pref_pro_level_unlock),
                        getResources().getInteger(R.integer.pref_default_level_unlock));

        beg_view.addView(getLayoutInflater().inflate(R.layout.unlock_layout, null, false));
        tv_diff_unlock = (TextView)beg_view.findViewById(R.id.difficulty_text_unlock);
        tv_diff_unlock.setText("Beginner");
        tv_level = (TextView)beg_view.findViewById(R.id.level_difficulty);
        tv_level.setText(Integer.toString(beg_levels_unlocked -1) + "/26");




        if(beg_levels_unlocked >= 16)
        {
            ama_view.addView(getLayoutInflater().inflate(R.layout.unlock_layout, null, false));
            tv_diff_unlock = (TextView)ama_view.findViewById(R.id.difficulty_text_unlock);
            tv_diff_unlock.setText("Amateur");
            tv_level = (TextView)ama_view.findViewById(R.id.level_difficulty);
            tv_level.setText(Integer.toString(ama_levels_unlocked-1) + "/26");
        }
        else
        {
            ama_view.addView(getLayoutInflater().inflate(R.layout.lock_layout, null, false));
            tv_diff_lock = (TextView)ama_view.findViewById(R.id.difficulty_text_lock);
            tv_diff_lock.setText("Amateur");
        }

        if(ama_levels_unlocked >= 16)
        {
            exp_view.addView(getLayoutInflater().inflate(R.layout.unlock_layout, null, false));
            tv_diff_unlock = (TextView)exp_view.findViewById(R.id.difficulty_text_unlock);
            tv_diff_unlock.setText("Expert");
            tv_level = (TextView)exp_view.findViewById(R.id.level_difficulty);
            tv_level.setText(Integer.toString(exp_levels_unlocked-1) + "/26");
        }
        else
        {
            exp_view.addView(getLayoutInflater().inflate(R.layout.lock_layout, null, false));
            tv_diff_lock = (TextView)exp_view.findViewById(R.id.difficulty_text_lock);
            tv_diff_lock.setText("Expert");
        }


        if(exp_levels_unlocked >= 16)
        {
            pro_view.addView(getLayoutInflater().inflate(R.layout.unlock_layout, null, false));
            tv_diff_unlock = (TextView)pro_view.findViewById(R.id.difficulty_text_unlock);
            tv_diff_unlock.setText("Professional");
            tv_level = (TextView)pro_view.findViewById(R.id.level_difficulty);
            tv_level.setText(Integer.toString(pro_levels_unlocked-1) + "/26");
        }
        else
        {
            pro_view.addView(getLayoutInflater().inflate(R.layout.lock_layout, null, false));
            tv_diff_lock = (TextView)pro_view.findViewById(R.id.difficulty_text_lock);
            tv_diff_lock.setText("Professional");
        }

    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int i, int i1) {
        soundPool.play(i, 1.0f, 1.0f, 100, 0, 1);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        mp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mp.setOnLoadCompleteListener(this);
        if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SOUND_PREF, true))

        {mp.load(this, R.raw.sound_level, 100);}


//        // scroll down
//        ScrollView v= (ScrollView)findViewById(R.id.scrollView_difficulty);
//        v.pageScroll(View.FOCUS_DOWN);



    }

    public void onBeginner(View view)
    {
        Intent intent = new Intent(this, LevelActivity.class);
        intent.putExtra(DIFFICULTY_EXTRA, DIFFICULTY_BEGINNER);
        startActivity(intent);
    }


    public void onAmateur(View view)
    {
        if(beg_levels_unlocked >= 16)
        {
            if(toast != null)
            {
                toast.cancel();
            }
            Intent intent = new Intent(this, LevelActivity.class);
            intent.putExtra(DIFFICULTY_EXTRA, DIFFICULTY_AMATEUR);
            startActivity(intent);
        }
        else
        {
            toast = Toast.makeText(this, "Complete 15 levels of Beginner to unlock Amateur", Toast.LENGTH_LONG);
            toast.show();
        }
    }



    public void onExpert(View view)
    {
        if(ama_levels_unlocked >= 16)
        {
            if (toast != null)
            {
                toast.cancel();
            }
            Intent intent = new Intent(this, LevelActivity.class);
            intent.putExtra(DIFFICULTY_EXTRA, DIFFICULTY_EXPERT);
            startActivity(intent);
        }
        else
        {
            toast = Toast.makeText(this, "Complete 15 levels of Amateur to unlock Expert", Toast.LENGTH_LONG);
            toast.show();
        }
    }


    public void onProfessional(View view)
    {
        if (ama_levels_unlocked >= 16)
        {

            if (toast != null)
            {
                toast.cancel();
            }
            Intent intent = new Intent(this, LevelActivity.class);
            intent.putExtra(DIFFICULTY_EXTRA, DIFFICULTY_PROFESSIONAL);
            startActivity(intent);
        }
        else
        {
            toast = Toast.makeText(this, "Complete 15 levels of Expert to unlock Professional", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mp.release();
    }

}
