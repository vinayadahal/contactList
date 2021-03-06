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

import contacts.components.MessageAlert;

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
            new MessageAlert().showToast("Failed to connect server", ctx);
            return null;
        }
    }


    public boolean WriteToFile(String data, Context ctx, String filename) {
        FileOutputStream outputStream;
        try {
            outputStream = ctx.openFileOutput(filename, ctx.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
            return true;
        } catch (Exception e) {
            System.out.println("WriteToFile failed >>>>>" + e);
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
            return null;
        }
    }
}
