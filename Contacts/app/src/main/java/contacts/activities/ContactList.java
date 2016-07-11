package contacts.activities;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import contacts.services.AutoCompleteService;
import contacts.services.FileService;

public class ContactList extends AppCompatActivity {

    private Toolbar Actionbar;
    private AppCompatTextView appCompatTextView;
    private MenuItem searchIcon;
    private MenuItem closeIcon;
    private FileService fileService = new FileService();
    private AutoCompleteService autoCompleteService = new AutoCompleteService();
    private AppCompatAutoCompleteTextView autoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.click_layout);
        Actionbar = (Toolbar) findViewById(R.id.action_bar);
        setSupportActionBar(Actionbar);
        getSupportActionBar().setTitle(null);
        View backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        StringBuilder contactData = fileService.readFile(this);
        if (contactData == null) {
            return;
        }
        createContactList(contactData);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar_btn, menu);
        closeIcon = menu.findItem(R.id.action_bar_close);
        closeIcon.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_bar_search:
                appCompatTextView = (AppCompatTextView) findViewById(R.id.txtView);
                Actionbar.removeView(appCompatTextView);
                autoComplete = new AppCompatAutoCompleteTextView(this);
                autoComplete = autoCompleteService.EditTextStyler(autoComplete, Actionbar); // calls styler from autoCompleteService
                autoCompleteService.TextChange(this, autoComplete); // calls textChange action from another class
                Actionbar.addView(autoComplete);
                searchIcon = item;
                searchIcon.setVisible(false);
                closeIcon.setVisible(true);
                return true;
            case R.id.action_bar_close:
                Actionbar.removeView(autoComplete);
                Actionbar.addView(appCompatTextView);
                searchIcon.setVisible(true);
                closeIcon.setVisible(false);
                return true;
            case R.id.action_bar_download:
                StringBuilder contactList = fileService.getContactList(this);
                if (contactList == null) {
                    return false;
                }
                createContactList(contactList);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createContactList(StringBuilder text) {
        try {
            JSONArray jArray = new JSONArray(text.toString());
            LinearLayout layout = (LinearLayout) findViewById(R.id.lnrLayout);
            ArrayList user_id = new ArrayList<>();
            final ArrayList names = new ArrayList<>();
            final ArrayList phone = new ArrayList<>();
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject objJson = jArray.getJSONObject(i);
                user_id.add(objJson.getString("id"));
                names.add(objJson.getString("name"));
                phone.add(objJson.getString("phone"));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_list, android.R.id.text1, names);
            final ListView listView = new ListView(this);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int itemPosition = position;
                    Toast.makeText(getApplicationContext(), "Calling: " + names.get(itemPosition), Toast.LENGTH_SHORT).show();
                    callHotline(phone.get(itemPosition).toString());
                    showPopUp();
                }
            });
            layout.removeAllViews();
            layout.addView(listView);
        } catch (JSONException ex) {
            System.out.println(ex);
        }
    }

    public void showPopUp() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.choice_pop_up, null, false), 100, 100, true);
        pw.showAtLocation(this.findViewById(R.id.lnrLayout), Gravity.CENTER, 0, 0);
    }

    public void callHotline(String phone) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phone));
        try {
            startActivity(callIntent);
        } catch (SecurityException ex) {
            System.out.println(ex);
        }
    }
}