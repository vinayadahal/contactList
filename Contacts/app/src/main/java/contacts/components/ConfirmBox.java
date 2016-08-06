package contacts.components;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import contacts.activities.ContactList;
import contacts.activities.R;

public class ConfirmBox {

    public void showConfirmBox(final Context ctx) {
        AlertDialog.Builder objAlertDialog = new AlertDialog.Builder(ctx);
        objAlertDialog.setTitle("Confirm Action");
        objAlertDialog.setMessage("Do you really want to exit?");
        objAlertDialog.setIcon(R.drawable.ic_report_problem_black_24dp);
        objAlertDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ((ContactList) ctx).finish();
            }
        });
        objAlertDialog.setNegativeButton(android.R.string.no, null);
        objAlertDialog.show();
    }

}
