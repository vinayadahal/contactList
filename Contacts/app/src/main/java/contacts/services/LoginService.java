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

import contacts.config.AppConfig;

public class LoginService extends AsyncTask<String, String, String> {

    HttpURLConnection urlConnection;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        System.out.println("executed---------------");
    }

    @Override
    protected String doInBackground(String... params) {
        ConnectToServer();
        HashMap<String, String> postData = new HashMap<>();
        postData.put("username", params[0]);
        postData.put("password", params[1]);
        String response = "";

        try {
            String postDetails = encodeUrl(postData);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(postDetails.toString());

            writer.flush();
            writer.close();
            os.close();
            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";

            }
            System.out.println("respone-------------->>>>>>>>>>>>>> " + response);
        } catch (ProtocolException ex) {
            System.out.println("caught protocol exception");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean ConnectToServer() {
        URL url;
        AppConfig objAppConfig = new AppConfig();
        try {
            url = new URL(objAppConfig.remoteServer + "/getdata.php");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(200);
            return true;
        } catch (IOException ex) {
            System.out.println("Caught Exception URL: " + ex);
            return false;
        }
    }

    public String encodeUrl(HashMap<String, String> postData) {
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
