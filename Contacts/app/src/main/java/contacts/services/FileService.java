package contacts.services;

import android.content.Context;
import android.os.StrictMode;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FileService {

    Context context;

    public StringBuilder getContactList(Context ctx) {
        context = ctx;
        URL url = DownloadFile();
        if (url == null) {
            return null;
        }
        StringBuilder response = ReadResponse(url);
        if (response == null) {
            return null;
        }
        WriteToFile(response, context);
        StringBuilder contactData = readFile(ctx);
        if (contactData == null) {
            return null;
        }
        return contactData;
    }

    public URL DownloadFile() {
        URL url;
        try {
            url = new URL("http://192.168.1.4/api");
            return url;
        } catch (MalformedURLException ex) {
            System.out.println("Caught Exception URL: " + ex);
            createToast("Couldn't connect to server.");
            return null;
        }
    }


    public StringBuilder ReadResponse(URL url) {
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
            return textData;
        } catch (IOException | NullPointerException ex) {
            System.out.println("Caught Exception IO: " + ex);
            createToast("Unable to read data from server");
            return null;
        }
    }


    public void WriteToFile(StringBuilder data, Context ctx) {
        String filename = "Contacts";
        FileOutputStream outputStream;
        try {
            outputStream = ctx.openFileOutput(filename, ctx.MODE_PRIVATE);
            outputStream.write(data.toString().getBytes());
            outputStream.close();
            createToast("Contact list downloaded successfully");
        } catch (Exception e) {
            System.out.println("WriteToFile>>>>>" + e);
            createToast("Unable to save data on device");
        }
    }

    public StringBuilder readFile(Context ctx) {
        System.out.println("Reading From File ------->>>>>>>>>>");
        StringBuilder text = new StringBuilder();
        File file = new File(ctx.getApplicationContext().getFilesDir() + "/Contacts");
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

    public void createToast(String msg, Context ctx) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }

    public void createToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

}

