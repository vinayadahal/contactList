package contacts.activities;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import contacts.config.AppConfig;
import contacts.services.AutoCompleteService;
import contacts.services.FileService;
import contacts.services.UrlConnectionService;

public class ContactList extends AppCompatActivity {

    private Toolbar Actionbar;
    private AppCompatTextView appCompatTextView;
    private MenuItem searchIcon;
    private MenuItem closeIcon;
    private MenuItem forwardIcon;
    private FileService fileService = new FileService();
    private AutoCompleteService autoCompleteService = new AutoCompleteService();
    private AppCompatAutoCompleteTextView autoComplete;
    private LinearLayout.LayoutParams llp;
    private PopupWindow pw;
    AppConfig objAppConfig = new AppConfig();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list_layout);

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
        forwardIcon = menu.findItem(R.id.action_go_btn);
        forwardIcon.setVisible(false);
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
                forwardIcon.setVisible(true);
                closeIcon.setVisible(true);
                return true;
            case R.id.action_bar_close:
                Actionbar.removeView(autoComplete);
                Actionbar.addView(appCompatTextView);
                searchIcon.setVisible(true);
                forwardIcon.setVisible(false);
                closeIcon.setVisible(false);
                StringBuilder contactData = fileService.readFile(this);
                if (contactData == null) {
                    return true;
                }
                createContactList(contactData);
                return true;
            case R.id.action_bar_download:
                ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLargeInverse);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    item.setActionView(progressBar);
                }
                StringBuilder contactList = fileService.getContactList(this);
                Map connectionDetails = new HashMap<>();
                connectionDetails.put("url", objAppConfig.remoteServer);
                connectionDetails.put("method", "GET");
                connectionDetails.put("context", this);
                connectionDetails.put("filename", "Contacts");
                UrlConnectionService objUrlService = new UrlConnectionService();
                objUrlService.execute(connectionDetails);
                if (contactList == null) {
                    return false;
                }
                createContactList(contactList);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    item.setActionView(null);
                }
                return true;

            case R.id.action_go_btn:
                searchContact(autoComplete.getText().toString());
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
                    showPopUp(names.get(itemPosition).toString(), phone.get(itemPosition).toString());
                }
            });
            layout.removeAllViews();
            layout.addView(listView);
        } catch (JSONException ex) {
            System.out.println(ex);
        }
    }

    public void searchContact(String name) {
        StringBuilder text = fileService.readFile(this);
        if (text == null || text.equals("")) {
            return;
        }
        try {
            JSONArray jArray = new JSONArray(text.toString());
            final ArrayList userNames = new ArrayList<>();
            final ArrayList phone = new ArrayList<>();
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject objJson = jArray.getJSONObject(i);
                if ((objJson.getString("name").contains(name))) {
                    userNames.add(objJson.getString("name"));
                    phone.add(objJson.getString("phone"));
                    break;
                }
            }
            LinearLayout layout = (LinearLayout) findViewById(R.id.lnrLayout);
            final ListView listView = new ListView(this);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_list, userNames);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int itemPosition = position;
                    showPopUp(userNames.get(itemPosition).toString(), phone.get(itemPosition).toString());
                }
            });
            layout.removeAllViews();
            layout.addView(listView);

        } catch (JSONException ex) {
            System.out.println("This is json exception" + ex);
        }
    }


    public void showPopUp(String username, String phone) {
        LinearLayout lnrlayout = (LinearLayout) findViewById(R.id.lnrLayout);
        llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View choice_pop_up = inflater.inflate(R.layout.choice_pop_up, null, true);
        addChoiceBtns(choice_pop_up, username, phone);
        pw = new PopupWindow(choice_pop_up, llp.width, llp.height, true);
        pw.setBackgroundDrawable(new ColorDrawable()); //helped me to hide popup
        pw.setOutsideTouchable(true);
        pw.setTouchable(true);
        pw.showAtLocation(lnrlayout, Gravity.CENTER, 0, 0);
    }

    public void addChoiceBtns(View v, final String username, final String phone) {
        Button call_btn = (Button) v.findViewById(R.id.popup_call_btn);
        call_btn.setText("Call: " + username);
        call_btn.setTransformationMethod(null);
        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Calling: " + username, Toast.LENGTH_SHORT).show();
                callHotline(phone);
            }
        });
        Button sms_btn = (Button) v.findViewById(R.id.popup_sms_btn);
        sms_btn.setText("SMS: " + username);
        sms_btn.setTransformationMethod(null);
        sms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Show Messaging: " + username, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void hidePopUp(View view) {
        pw.dismiss();
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
