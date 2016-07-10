package contacts.services;


import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import contacts.activities.R;

public class EditTextService {

    private FileService fileService = new FileService();
    public ArrayList userNames = new ArrayList<>();

    public AppCompatEditText EditTextStyler(AppCompatEditText appCompatEditText, Toolbar Actionbar) {
        appCompatEditText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI); // prevents full screen edit text in landscape.
        appCompatEditText.setHint("Search ...");
        appCompatEditText.setHeight(Actionbar.getHeight());
        appCompatEditText.setHintTextColor(0xFFFFFFFF);

        //for setting color of underline in edittext
        Drawable drawable = appCompatEditText.getBackground();
        drawable.setColorFilter(0xFFFFFFFF, PorterDuff.Mode.SRC_ATOP);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            appCompatEditText.setBackground(drawable);
        } else {
            appCompatEditText.setCompoundDrawables(null, null, drawable, null);
        }
        //end of color set code
        appCompatEditText.setWidth(Actionbar.getWidth()); // sets search box width 100% all the time.
        return appCompatEditText;
    }

    public void TextChange(final AppCompatEditText editText, final Context ctx) {
        //final keyword makes sure a variable is set only once
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                StringBuilder text = fileService.readFile(ctx);
//                AppCompatAutoCompleteTextView autoComplete = new AppCompatAutoCompleteTextView(ctx);
                try {
                    JSONArray jArray = new JSONArray(text.toString().toLowerCase());
                    String name = "";

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject objJson = jArray.getJSONObject(i);
                        userNames.add(objJson.getString("name"));
                        if ((objJson.getString("name").contains(s))) {
                            name = objJson.getString("name");
                            break;
                        }
                    }
//                    System.out.println("found username : " + name.substring(0, 1).toUpperCase() + name.substring(1));
//                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ctx, android.R.layout.select_dialog_singlechoice, userNames);
//                    autoComplete.setThreshold(1);
//                    autoComplete.setAdapter(adapter);


                } catch (JSONException ex) {
                    System.out.println("This is json exception" + ex);
                }
                System.out.println("text changed.....--------------->>>>>>>>>>>>" + s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

//    public void autoCompleteMenu() {
//
//        autoComplete.setText("whatever");
//    }
}
