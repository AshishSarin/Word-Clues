package com.android.sareen.a2z;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

/**
 * Created by Ashish on 02-Aug-15.
 */
public class SettingsDialogFragment extends DialogFragment
    implements CompoundButton.OnCheckedChangeListener, SoundPool.OnLoadCompleteListener
{

    private CheckBox sound_checkbox;
    private CheckBox music_checkbox;

    private Boolean playSound = false;

    private SoundPool sp;

    private static final String SOUND_PREF = "sound_pref";
    private static final String NOTIF_PREF = "notif_pref";

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if(prefs.getBoolean(SOUND_PREF, true) && playSound && sp != null)
        {
            sp.load(getActivity(), R.raw.sound_level, 100);
        }
        SharedPreferences.Editor editor = prefs.edit();
        int id = compoundButton.getId();
        switch (id)
        {
            case R.id.checkbox_sound:
                editor.putBoolean(SOUND_PREF, compoundButton.isChecked());
                break;
            case R.id.checkbox_music:
                editor.putBoolean(NOTIF_PREF, compoundButton.isChecked());
                break;
        }
        editor.commit();
    }



    @Override
    public void onResume()
    {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sound_checkbox.setChecked(prefs.getBoolean(SOUND_PREF, true));
        music_checkbox.setChecked(prefs.getBoolean(NOTIF_PREF, true));

        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        sp.setOnLoadCompleteListener(this);
        playSound = true;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean(SOUND_PREF, sound_checkbox.isChecked());

        editor.putBoolean(NOTIF_PREF, music_checkbox.isChecked());
        playSound = false;
        sp.release();
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_settings, null, false);

        sound_checkbox = (CheckBox)v.findViewById(R.id.checkbox_sound);
        sound_checkbox.setOnCheckedChangeListener(this);
        music_checkbox = (CheckBox)v.findViewById(R.id.checkbox_music);
        music_checkbox.setOnCheckedChangeListener(this);

        Button rate_us = (Button)v.findViewById(R.id.btn_rate_us);
        rate_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                sp.load(getActivity(), R.raw.sound_level, 100);
//                Toast.makeText(getActivity(), "Coming Soon....", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.squarelabs.wordclues"));
                startActivity(Intent.createChooser(intent, "Complete action using..."));
            }
        });

        builder.setView(v);

        return builder.create();
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int i, int i1) {
        soundPool.play(i, 1.0f, 1.0f, 100, 0, 1);
    }
}
