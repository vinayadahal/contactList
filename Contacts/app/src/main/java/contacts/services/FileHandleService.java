package contacts.services;


import android.content.Context;
import android.os.Handler;
import android.os.StrictMode;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class FileHandleService {

    public String ReadResponse(HttpURLConnection urlConnection, Context ctx) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder textData = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                textData.append(line + "\n");
            }
            return textData.toString();
        } catch (IOException | NullPointerException ex) {
            System.out.println("Caught Exception IO: " + ex);
            createToast("Failed to connect server", ctx);
            return null;
        }
    }


    public boolean WriteToFile(String data, Context ctx, String filename) {
        System.out.println("Write file from file handler");
        FileOutputStream outputStream;
        try {
            outputStream = ctx.openFileOutput(filename, ctx.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
            createToast("Contact list downloaded successfully", ctx);
            return true;
        } catch (Exception e) {
            System.out.println("WriteToFile>>>>>" + e);
            createToast("Unable to save data on device", ctx);
            return false;
        }
    }

    public StringBuilder readFile(Context ctx, String filename) { // checks and creates file if not exists
        System.out.println("Reading From File ------->>>>>>>>>>");
        StringBuilder text = new StringBuilder();
        File file = new File(ctx.getApplicationContext().getFilesDir() + "/" + filename);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
            return text;
        } catch (IOException e) {
            System.out.println("exception ----------------> " + e);
            createToast("Contact list missing. Please re-download", ctx);
            return null;
        }
    }

    public void createToast(final String msg, final Context ctx) {
        Handler handler = new Handler(ctx.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
            }
        });

    }
}
