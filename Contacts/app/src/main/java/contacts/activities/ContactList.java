package contacts.activities;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
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
import android.widget.Toast;

import java.util.List;

import contacts.components.ConfirmBox;
import contacts.components.MessageAlert;
import contacts.components.Popup;
import contacts.components.progressBar;
import contacts.services.AutoCompleteService;
import contacts.services.FileHandleService;
import contacts.services.JsonToList;
import contacts.services.UrlService;

public class ContactList extends AppCompatActivity {

    private Toolbar Actionbar;
    private AppCompatTextView appCompatTextView;
    private MenuItem searchIcon;
    private MenuItem closeIcon;
    private MenuItem checkIcon;
    private FileHandleService objFileHandle = new FileHandleService();
    private AutoCompleteService autoCompleteService = new AutoCompleteService();
    private AppCompatAutoCompleteTextView autoComplete;
    private PopupWindow pw;
    private String filename = "Contacts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list_layout);
        Actionbar = (Toolbar) findViewById(R.id.action_bar);
        setSupportActionBar(Actionbar);
        getSupportActionBar().setTitle(null);
        View backBtn = findViewById(R.id.back_btn);
        final Context ctx = this;
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ConfirmBox().showConfirmBox(ctx);
            }
        });
        StringBuilder contactData = objFileHandle.readFile(this, filename);
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
        checkIcon = menu.findItem(R.id.action_go_btn);
        checkIcon.setVisible(false);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            new ConfirmBox().showConfirmBox(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_bar_search:
                appCompatTextView = (AppCompatTextView) findViewById(R.id.txtView);
                Actionbar.removeView(appCompatTextView);
                autoComplete = new AppCompatAutoCompleteTextView(this);
                autoComplete = autoCompleteService.EditTextStyler(autoComplete, Actionbar); // calls styler from autoCompleteService
                autoCompleteService.showAutoCompleteDropDown(this, autoComplete); // calls textChange action from another class
                Actionbar.addView(autoComplete);
                searchIcon = item;
                searchIcon.setVisible(false);
                autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        checkIcon.setVisible(true);
                    }
                });
                closeIcon.setVisible(true);
                return true;
            case R.id.action_bar_close:
                Actionbar.removeView(autoComplete);
                Actionbar.addView(appCompatTextView);
                searchIcon.setVisible(true);
                checkIcon.setVisible(false);
                closeIcon.setVisible(false);
                if (objFileHandle.readFile(this, filename) == null) {
                    return true;
                }
                createContactList(objFileHandle.readFile(this, filename));
                return true;
            case R.id.action_bar_download:
                progressBar objProgressBar = new progressBar();
                objProgressBar.showProgressBar(this, item);
                String serverResponse = objFileHandle.ReadResponse(new UrlService().urlGetContactList(this), this);
                if (serverResponse == null) {
                    objProgressBar.hideProgressBar();
                    return true;
                }
                if (objFileHandle.WriteToFile(serverResponse, this, "Contacts")) {
                    new MessageAlert().showToast("Contact list downloaded successfully", this);
                } else {
                    new MessageAlert().showToast("Unable to save data on device", this);
                }
                if (objFileHandle.readFile(this, filename) == null) {
                    new MessageAlert().showToast("Contact list missing. Please re-download", this);
                    return false;
                }
                createContactList(objFileHandle.readFile(this, filename));
                objProgressBar.hideProgressBar();
                return true;
            case R.id.action_go_btn:
                searchContact(autoComplete.getText().toString());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createContactList(StringBuilder text) {
        JsonToList objJsonToList = new JsonToList();
        objJsonToList.setList(text);
        showContactList(objJsonToList.getListNames(), objJsonToList.getListPhone());
    }

    public void searchContact(String searchKeyword) {
        StringBuilder text = objFileHandle.readFile(this, filename);
        if (text == null || text.equals("")) {
            return;
        }
        JsonToList objJsonToList = new JsonToList();
        objJsonToList.setList(text, searchKeyword);
        showContactList(objJsonToList.getListNames(), objJsonToList.getListPhone());
    }

    public void showContactList(final List names, final List phone) {
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
        LinearLayout layout = (LinearLayout) findViewById(R.id.lnrLayout);
        layout.removeAllViews();
        layout.addView(listView);
    }


    public void showPopUp(String username, String phone) {
        LinearLayout lnrlayout = (LinearLayout) findViewById(R.id.lnrLayout);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View choice_pop_up = inflater.inflate(R.layout.choice_pop_up, null, true);
        choiceButtons(choice_pop_up, username, phone);
        Popup objPopup = new Popup();
        pw = objPopup.showPopup(choice_pop_up, llp);
        pw.showAtLocation(lnrlayout, Gravity.CENTER, 0, 0);
    }

    public void choiceButtons(View v, final String username, final String phone) {
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