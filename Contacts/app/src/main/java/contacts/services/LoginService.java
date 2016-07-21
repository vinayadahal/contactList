package contacts.services;


import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

import contacts.config.AppConfig;

public class LoginService {

    Context context;

    HttpURLConnection urlConnection;

    public boolean ConnectToServer() {
        URL url;
        AppConfig objAppConfig = new AppConfig();
        try {
            url = new URL(objAppConfig.remoteServer);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(200);
            return true;
        } catch (IOException ex) {
            System.out.println("Caught Exception URL: " + ex);
            return false;
        }
    }

    public void Login(String username, String password) {
        System.out.println(username);
        System.out.println(password);

    }

}
