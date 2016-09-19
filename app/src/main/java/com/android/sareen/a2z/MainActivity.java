package com.android.sareen.a2z;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class MainActivity extends FragmentActivity
    implements AboutDialogFragment.AboutDialogListener, SoundPool.OnLoadCompleteListener
{
    SoundPool sp;

    private static final String SOUND_PREF = "sound_pref";

    private static final String ABOUT_FRAGMENT = "about_fragment";
    private static final String SETTINGS_FRAGMENT = "settings_fragment";
    private static final String INFO_FRAGMENT = "info_fragment";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try
        {
            String destPath = "/data/data/" + getPackageName() +
                    "/databases";
            File f = new File(destPath);

            if (!f.exists())
            {
                f.mkdirs();
                f.createNewFile();

                //---copy the db from the assets folder into
                // the databases folder---
                CopyDB(getBaseContext().getAssets().open("a2z.db"),
                        new FileOutputStream(destPath + "/a2z"));
            }

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        sp.setOnLoadCompleteListener(this);
    }

   @Override
   public void onStop()
   {
       super.onStop();
   }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        sp.release();
    }

    public void CopyDB(InputStream inputStream, OutputStream outputStream) throws IOException {
        //---copy 1K bytes at a time---
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0)
        {
            outputStream.write(buffer, 0, length);
        }

        inputStream.close();
        outputStream.close();
    }



    //OnClick listener for Start Button
    public void onClickStart(View view)
    {


        // Start the difficult select activity
        Intent intent = new Intent(this, DifficultyActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    public void onClickSettings(View view)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getBoolean(SOUND_PREF, true))
        {
            sp.load(this, R.raw.sound_level, 100);
        }
        SettingsDialogFragment dialogFragment = new SettingsDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), SETTINGS_FRAGMENT);
    }

    public void onClickShare(View view)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getBoolean(SOUND_PREF, true))
        {
            sp.load(this, R.raw.sound_level,100);
        }
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name) + System.getProperty("line.separator")+ "Check out this cool word game"
                            + System.getProperty("line.separator") + "https://play.google.com/store/apps/details?id=com.squarelabs.wordclues");
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }


    public void onClickAbout(View view)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getBoolean(SOUND_PREF, true))
        {
            sp.load(this, R.raw.sound_level,100);
        }
        AboutDialogFragment dialogFragment = new AboutDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), ABOUT_FRAGMENT);
    }

    @Override
    public void onAbout()
    {
        InfoFragment infoFragment = new InfoFragment();
        infoFragment.show(getSupportFragmentManager(), INFO_FRAGMENT);
    }

    @Override
    public void onFb()
    {
        Intent fbIntent = new Intent(Intent.ACTION_VIEW);
        fbIntent.setData(Uri.parse("https://www.facebook.com/labs.square"));
        startActivity(Intent.createChooser(fbIntent, "Complete action using..."));
    }

    @Override
    public void onTwitter()
    {
        Intent twtIntent = new Intent(Intent.ACTION_VIEW);
        twtIntent.setData(Uri.parse("https://twitter.com/LabsSquare"));
        startActivity(Intent.createChooser(twtIntent, "Complete action using..."));
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int i, int i1)
    {
        soundPool.play(i, 1.0f, 1.0f, 100, 0, 1);
    }
}
