package br.com.carlosti.components;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import br.com.carlosti.enterprise.R;

public class CustomAlertDialog {

    public static AlertDialog confirmDialogConstructor(Context context, String title, String message, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);

        if (positive != null)
            builder.setPositiveButton(R.string.ok, positive);
        else
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });

        if (negative != null)
            builder.setNegativeButton(R.string.cancel, negative);
        else
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });

        return builder.create();
    }
}
