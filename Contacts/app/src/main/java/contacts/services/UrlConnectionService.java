package contacts.services;


import android.content.Context;
import android.os.AsyncTask;

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
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class UrlConnectionService extends AsyncTask<Map, Void, Void> {
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
    protected Void doInBackground(Map... params) {
        ConnectToServer(params[0].get("url").toString());
        if (params[0].get("method") == "POST" || params[0].get("method") == "post") {
            doPost("POST");
            hitUrl((Map) params[0].get("args"));
            recieveResponse();
        } else if (params[0].get("method") == "GET" || params[0].get("method") == "get") {
            FileHandleService objFileHandle = new FileHandleService();
            objFileHandle.WriteToFile("This Text only. I guess.", (Context) params[0].get("context"), "info");
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
                response = "No Response From Server";

            }
            System.out.println("RESPONSE:::::: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void doGet() {

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
}
