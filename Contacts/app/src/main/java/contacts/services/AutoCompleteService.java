package contacts.services;


import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import contacts.activities.R;

public class AutoCompleteService {

    private FileHandleService objFileHandle = new FileHandleService();
    public AppCompatAutoCompleteTextView autoComplete_public;
    private String filename = "Contacts";

    public AppCompatAutoCompleteTextView EditTextStyler(AppCompatAutoCompleteTextView autoComplete, Toolbar Actionbar) {
        autoComplete.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI); // prevents full screen edit text in landscape.
        autoComplete.setHint("Search ...");
        autoComplete.setHeight(Actionbar.getHeight());
        autoComplete.setHintTextColor(0xFFFFFFFF);
        autoComplete.setTextSize(16);
        autoComplete.setTextColor(0xFFFFFFFF);
        //for setting color of underline in edittext
        Drawable drawable = autoComplete.getBackground();
        drawable.setColorFilter(0xFFFFFFFF, PorterDuff.Mode.SRC_ATOP);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            autoComplete.setBackground(drawable);
        } else {
            autoComplete.setCompoundDrawables(null, null, drawable, null);
        }
        //end of color set code
        autoComplete.setWidth(Actionbar.getWidth()); // sets search box width 100% all the time.
        return autoComplete;
    }

    public void showAutoCompleteDropDown(final Context ctx, final AppCompatAutoCompleteTextView autoComplete) {
        StringBuilder text = objFileHandle.readFile(ctx, filename);
        if (text == null || text.equals("")) {
            return;
        }
        try {
            JSONArray jArray = new JSONArray(text.toString().toLowerCase());
            final ArrayList userNames = new ArrayList<>();
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject objJson = jArray.getJSONObject(i);
                userNames.add(WordUtils.capitalize(objJson.getString("name")));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(ctx, R.layout.drop_down_item, userNames);
            autoComplete.setThreshold(1);
            autoComplete.setAdapter(adapter);
            autoComplete.showDropDown();
            autoComplete_public = autoComplete;

        } catch (JSONException ex) {
            System.out.println("This is json exception" + ex);
        }
    }
}
