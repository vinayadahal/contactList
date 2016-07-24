package contacts.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import contacts.config.AppConfig;
import contacts.services.LoginService;
import contacts.services.UrlConnectionService;


public class MainActivity extends AppCompatActivity {

    AppConfig objAppConfig = new AppConfig();

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
        UrlConnectionService objConnection = new UrlConnectionService();
        if (!objLoginService.ConnectToServer()) {
            createToast("Contact list downloaded successfully");
            return;
        }
        EditText usernameEditText = (EditText) findViewById(R.id.editTextUsername);
        EditText passwordEditText = (EditText) findViewById(R.id.editTextPassword);
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (username.isEmpty() || password.isEmpty()) {
            createToast("Username or password cannot be empty");
            return;
        }

        Map args = new HashMap<>();
        args.put("username", username);
        args.put("password", password);

        Map connectionDetails = new HashMap<>();
        connectionDetails.put("url", objAppConfig.remoteServer + "/getdata.php");
        connectionDetails.put("method", "POST");
        connectionDetails.put("args", args);
        objConnection.execute(connectionDetails);
        System.out.println("THE RESPONSE" + objConnection.serverResponse);
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

    public void createToast(String msg, Context ctx) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }

    public void createToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
