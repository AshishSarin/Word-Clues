package com.android.sareen.a2z;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Ashish on 27-Jul-15.
 */
public class GameOverDialogFragment extends DialogFragment
{
    private boolean win;
    private int current_level;
    private String current_word;


    private static final String SOUND_PREF = "sound_pref";




    public interface GameOverDialogListener
    {
        public void onPlaySound(boolean w);
        public void onDialogContinue(boolean w);
        public void onDialogQuit();
    }

    private GameOverDialogListener mListener;

    public static GameOverDialogFragment getInstance(boolean w, int level, String word)
    {
        GameOverDialogFragment dialogFragment = new GameOverDialogFragment();
        dialogFragment.current_word  = word;
        dialogFragment.current_level = level;
        dialogFragment.win = w;
        return dialogFragment;

    }

    @Override
    public void onPause()
    {
        super.onPause();
    }
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        // Verify that host activity implement callback interface
        try
        {
            mListener = (GameOverDialogListener)activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " mus implement GameOverDialogListener");
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mListener.onPlaySound(win);
        getDialog().setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                mListener.onDialogQuit();
            }
        });

    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_game_over, null);
        TextView finalMessage = (TextView)view.findViewById(R.id.textview_final_message);
        Button btn_continue = (Button)view.findViewById(R.id.btn_continue);
        TextView level_gameOver = (TextView)view.findViewById(R.id.level_gameover);
        TextView word_gameOver = (TextView)view.findViewById(R.id.word_gameover_textview);
        level_gameOver.setText(current_level + " / 26");
        word_gameOver.setText(current_word);
        if(win)
        {
            finalMessage.setText("YOU GOT IT!");
            btn_continue.setText("Continue");
            word_gameOver.setVisibility(View.VISIBLE);
        }
        else
        {
            word_gameOver.setVisibility(View.GONE);
            finalMessage.setText("TRY AGAIN...");
            btn_continue.setText("Retry");
        }
        btn_continue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dismiss();
                mListener.onDialogContinue(win);
            }
        });
        Button btn_quit = (Button)view.findViewById(R.id.btn_quit);
        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mListener.onDialogQuit();
            }
        });



        builder.setView(view);
        return builder.create();
    }
}
