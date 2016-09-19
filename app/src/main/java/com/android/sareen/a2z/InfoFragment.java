package com.android.sareen.a2z;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

/**
 * Created by Ashish on 06-Aug-15.
 */
public class InfoFragment extends DialogFragment
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_info, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);
        return builder.create();
    }

}
