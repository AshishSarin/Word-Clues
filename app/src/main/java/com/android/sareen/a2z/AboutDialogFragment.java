package com.android.sareen.a2z;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class AboutDialogFragment extends DialogFragment
{
    
    public interface AboutDialogListener
    {
        public void onAbout();
        public void onFb();
        public void onTwitter();
    }

    private AboutDialogListener mListener;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        // Verify that host activity implement callback interface
        try
        {
            mListener = (AboutDialogListener)activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " mus implement GameOverDialogListener");
        }
    }

    Button abt, fb, twt;

    TextView ver_txt;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_about, null, false);
        builder.setView(v);

        abt = (Button)v.findViewById(R.id.btn_about);
        fb = (Button)v.findViewById(R.id.btn_fb);
        twt = (Button)v.findViewById(R.id.btn_twitter);

        abt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onAbout();
            }
        });


        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFb();
            }
        });


        twt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onTwitter();
            }
        });


        ver_txt = (TextView)v.findViewById(R.id.version_text);
        try
        {
            ver_txt.setText("Version    " + BuildConfig.VERSION_NAME);
        }
        catch (Exception e)
        {
            ver_txt.setText("Version    0.0.0");
        }
        return builder.create();
    }


}
