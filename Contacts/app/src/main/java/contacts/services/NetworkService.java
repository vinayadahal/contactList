package contacts.services;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class NetworkService extends AsyncTask<Map, Void, HttpURLConnection> {

    public HttpURLConnection urlConnection;

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
    protected HttpURLConnection doInBackground(Map... params) {
        ConnectToServer(params[0].get("url").toString());
        if (params[0].get("method") == "POST" || params[0].get("method") == "post") {
            doPost("POST");
            processRequestPost((Map) params[0].get("args"));
            return receieveResponse();
        } else if (params[0].get("method") == "GET" || params[0].get("method") == "get") {
            return doGet("GET", (Context) params[0].get("context"));
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

    protected void processRequestPost(Map dataList) {
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

    protected HttpURLConnection receieveResponse() {
        try {
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                return urlConnection;
            } else {
                System.out.println("Response Code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected HttpURLConnection doGet(String method, final Context ctx) {
        try {
            urlConnection.setRequestMethod(method);
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        return urlConnection;
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
