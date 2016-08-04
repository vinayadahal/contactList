package contacts.services;


import android.content.Context;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import contacts.config.AppConfig;

public class UrlService {

    AppConfig objAppConfig = new AppConfig();
    private String filename = "Contacts";

    public HttpURLConnection urlGetContactList(Context ctx) {
        Map connectionDetails = new HashMap<>();
        connectionDetails.put("url", objAppConfig.remoteServer);
        connectionDetails.put("method", "GET");
        connectionDetails.put("context", ctx);
        connectionDetails.put("filename", filename);
        NetworkService objNetworkService = new NetworkService();
        try {
            return objNetworkService.execute(connectionDetails).get(); //waits for execute to complete.
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HttpURLConnection urlPostLogin(Map args) {
        Map connectionDetails = new HashMap<>();
        connectionDetails.put("url", objAppConfig.remoteServer + "/getdata.php");
        connectionDetails.put("method", "POST");
        connectionDetails.put("args", args);
        NetworkService objNetworkService = new NetworkService();
        try {
            return objNetworkService.execute(connectionDetails).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

}
