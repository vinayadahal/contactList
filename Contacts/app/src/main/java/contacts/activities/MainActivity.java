package contacts.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import contacts.components.MessageAlert;
import contacts.services.FileHandleService;
import contacts.services.LoginService;
import contacts.services.UrlService;


public class MainActivity extends AppCompatActivity {

    private FileHandleService objFileHandle = new FileHandleService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatImageButton appCompatImageButton = (AppCompatImageButton) findViewById(R.id.back_btn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
        toolbar.removeView(appCompatImageButton);
    }

    public void login(View view) {
        LoginService objLoginService = new LoginService();
        if (!objLoginService.ConnectToServer()) {
            new MessageAlert().showToast("Contact list downloaded successfully", this);
            return;
        }
        EditText usernameEditText = (EditText) findViewById(R.id.editTextUsername);
        EditText passwordEditText = (EditText) findViewById(R.id.editTextPassword);
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (username.isEmpty() || password.isEmpty()) {
            new MessageAlert().showToast("Username or password cannot be empty", this);
            return;
        }
        Map args = new HashMap<>();
        args.put("username", username);
        args.put("password", password);
        HttpURLConnection objHttpURLConnection = new UrlService().urlPostLogin(args);
        String serverResponse = null;
        if (objHttpURLConnection != null) {
            serverResponse = objFileHandle.ReadResponse(objHttpURLConnection, this);
        }

        if (serverResponse != null) {
            System.out.println(serverResponse + "<<<<<<<<<<<<<<<<");
            if ("AuthFail".equals(serverResponse.trim())) {
                new MessageAlert().showToast("Username or Password is incorrect.", this);
                return;
            }
            objFileHandle.WriteToFile(serverResponse, this, "info");
        }
        StringBuilder loginData = objFileHandle.readFile(this, "info");
        if (loginData == null || loginData.toString().isEmpty()) {
            new MessageAlert().showToast("You need internet connection for first login", this);
            return;
        }
        try {
            JSONArray jArray = new JSONArray(loginData.toString());
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject objJson = jArray.getJSONObject(i);
                if ((objJson.getString("password").equals(new String(Hex.encodeHex(DigestUtils.md5(password)))))) { // cnverting password to md5 and comparing with json
                    showContactList();
                    return;
                }
            }
            new MessageAlert().showToast("Authentication Failed. Please re-login.", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void showContactList() {
        Intent intent = new Intent(MainActivity.this, ContactList.class);
        startActivity(intent);
    }
}
