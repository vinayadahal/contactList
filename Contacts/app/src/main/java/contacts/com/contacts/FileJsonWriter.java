package contacts.com.contacts;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class FileJsonWriter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.click_layout);
        Toolbar Actionbar = (Toolbar) findViewById(R.id.action_bar);
        setSupportActionBar(Actionbar);
        getSupportActionBar().setTitle(null);
        //adding font icons to project
//        Typeface font = Typeface.createFromAsset(this.getAssets(), "fonts/fontawesome-webfont.ttf");
//        Button btn_go_back = (Button) findViewById(R.id.btn_go_back);
//        btn_go_back.setTypeface(font);
//        Button btn_download = (Button) findViewById(R.id.btn_download);
//        btn_download.setTypeface(font);
        readFile();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        System.out.println("called>>>>>>>>>>>>>menu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar_btn, menu);
        View backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        View actn_bar_download = findViewById(R.id.action_bar_download);
//        System.out.println("????????????????????"+actn_bar_download);
//        actn_bar_download.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("damn>>>>>>>>>>>>>");
////                DownloadFile(v);
//            }
//        });

        return true;
    }

    public void DownloadFile(View view) {
        URL url = null;
        try {
            url = new URL("http://192.168.1.5/api");
        } catch (MalformedURLException ex) {
            System.out.println("Caught Exception URL: " + ex);
        }

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(200);
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder textData = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                textData.append(line + "\n");
            }

            WriteToFile(textData);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, "Contact list downloaded successfully", duration);
            toast.show();
            readFile();
        } catch (IOException | NullPointerException ex) {
            System.out.println("Caught Exception IO: " + ex);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, "Unable to fetch contact list from server", duration);
            toast.show();
        }
    }


    public void WriteToFile(StringBuilder data) {
        String filename = "Contacts";
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(data.toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readFile() {
        System.out.println("Reading From File ------->>>>>>>>>>");
        StringBuilder text = new StringBuilder();
        File file = new File(this.getApplicationContext().getFilesDir() + "/Contacts");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
            createContactList(text);
        } catch (IOException e) {
            System.out.println("exception ----------------> " + e);
            Toast.makeText(this, "Contact list missing. Please re-download.", Toast.LENGTH_LONG).show();
        }
    }

    public void createContactList(StringBuilder text) {
        try {
            JSONArray jArray = new JSONArray(text.toString());
            System.out.println(jArray.length());

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

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, names);
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

    public void GoBack(View view) {
        finish();
    }
}
