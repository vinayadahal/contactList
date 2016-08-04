package contacts.components;


import android.content.Context;
import android.os.Build;
import android.view.MenuItem;

public class progressBar {

    MenuItem menuItem;

    public void showProgressBar(Context ctx, MenuItem item) {
        android.widget.ProgressBar progressBar = new android.widget.ProgressBar(ctx, null, android.R.attr.progressBarStyleSmall);
        menuItem = item;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            menuItem.setActionView(progressBar);
        }
    }

    public void hideProgressBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            menuItem.setActionView(null);
        }
    }

}
