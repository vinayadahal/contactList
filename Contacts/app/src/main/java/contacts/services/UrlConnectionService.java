package contacts.services;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class UrlConnectionService extends AsyncTask<Map, Void, String> {

    public boolean serverResponse;
    HttpURLConnection urlConnection;

    public boolean ConnectToServer(String remoteUrl) {
        URL url;
        try {
            url = new URL(remoteUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(200);
            return true;
        } catch (IOException ex) {
            System.out.println("Caught Exception URL: " + ex);
            return false;
        }
    }


    @Override
    protected String doInBackground(Map... params) {
        System.out.println("should be first//////////////////////////////");
        ConnectToServer(params[0].get("url").toString());
        if (params[0].get("method") == "POST" || params[0].get("method") == "post") {
            doPost("POST");
            hitUrl((Map) params[0].get("args"));
            recieveResponse();
            return "post OK";
        } else if (params[0].get("method") == "GET" || params[0].get("method") == "get") {
            doGet("GET", (Context) params[0].get("context"));
            return "get OK";
        } else {
            System.out.println("HTTP Method Error");
        }
        return null;
    }

    protected void doPost(String method) {
        try {
            urlConnection.setRequestMethod(method);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
    }

    protected void hitUrl(Map dataList) {
        try {
            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(encodeUrl(dataList));
            writer.flush();
            writer.close();
            os.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void recieveResponse() {
        try {
            String response = "";
            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "No Response";
            }

            if (response.isEmpty() || response == "No Response") {
                serverResponse = false;
            } else {
                serverResponse = true;
            }
            System.out.println("RESPONSE:::::: " + response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(String method, final Context ctx) {
        try {
            urlConnection.setRequestMethod(method);
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        FileHandleService objFileHandle = new FileHandleService();
        String serverResponse = objFileHandle.ReadResponse(urlConnection, ctx);
        if (serverResponse == null) {
            createToast("No response from server", ctx);
            return;
        }
        objFileHandle.WriteToFile(serverResponse, ctx, "Contacts");
    }

    public String encodeUrl(Map<String, String> postData) {
        StringBuilder result = new StringBuilder();
        boolean flag = true;
        for (Map.Entry<String, String> dataSet : postData.entrySet()) {
            if (flag) {
                flag = false;
            } else {
                result.append("&");
            }
            try {
                result.append(URLEncoder.encode(dataSet.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(dataSet.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                System.out.println("<<<<encoding error caught>>>>");
            }
        }
        return result.toString();
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


