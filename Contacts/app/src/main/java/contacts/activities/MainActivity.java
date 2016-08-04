package contacts.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

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
        if (objHttpURLConnection == null) {
            System.out.println("response is null");
        }
        String serverResponse = objFileHandle.ReadResponse(objHttpURLConnection, this);
        System.out.println("server Response:::::" + serverResponse);
        StringBuilder loginData = objFileHandle.readFile(this, "info");
        if (loginData == null) {
            objFileHandle.WriteToFile(serverResponse, this, "info");
        }

        System.out.println("File output>>>>>>>>>>>>>>>>>..." + objFileHandle.readFile(this, "info"));

//        if (!objConnection.serverResponse) {
//            createToast("Authentication Failed. Please re-login.");
//        } else {
        showContactList();
//        }
    }


    public void showContactList() {
        Intent intent = new Intent(MainActivity.this, ContactList.class);
        startActivity(intent);
    }
}
