package contacts.com.contacts;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import java.util.List;

public class FileJsonWriter extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.click_layout);
        //adding font icons to project
        Typeface font = Typeface.createFromAsset(this.getAssets(), "fonts/fontawesome-webfont.ttf");
        Button btn_go_back = (Button) findViewById(R.id.btn_go_back);
        btn_go_back.setTypeface(font);
        Button btn_download = (Button) findViewById(R.id.btn_download);
        btn_download.setTypeface(font);
        readFile();
    }

    public void DownloadFile(View view) {
        URL url = null;
        try {
            url = new URL("http://192.168.1.3/api");
        } catch (MalformedURLException ex) {
            System.out.println("Caught Exception URL: " + ex);
        }

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        String result = null;
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(200);
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
            WriteToFile(result);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, "File downloaded successfully", duration);
            toast.show();
            readFile();

        } catch (IOException ex) {
            System.out.println("Caught Exception IO: " + ex);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, "Unable to fetch data from server", duration);
            toast.show();
        }
    }


    public void WriteToFile(String data) {
        String filename = "Contacts";
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
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
        } catch (IOException e) {
            System.out.println("exception ----------------> " + e);
        }

        try {
            JSONArray jArray = new JSONArray(text.toString());
            System.out.println(jArray.length());

            List<TextView> textList = new ArrayList<>(jArray.length());
            LinearLayout layout = (LinearLayout) findViewById(R.id.lnrLayout);

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject objJson = jArray.getJSONObject(i);
                System.out.println(objJson.getString("name"));
                System.out.println(objJson.getString("phone"));

                TextView txtView = new TextView(this);
                txtView.setText(objJson.getString("name") + " " + objJson.getString("phone"));
                txtView.setPadding(10, 5, 10, 5);
                layout.addView(txtView);
                textList.add(txtView);
            }
        } catch (JSONException ex) {
            System.out.println(ex);
        }

//        TextView tv = (TextView) findViewById(R.id.textViewPage2);
//        tv.setText(text);


    }

    public void GoBack(View view) {
        finish();
    }
}
