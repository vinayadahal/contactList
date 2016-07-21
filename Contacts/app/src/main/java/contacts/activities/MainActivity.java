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

import contacts.services.LoginService;


public class MainActivity extends AppCompatActivity {

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
        objLoginService.Login(username, password);
        showContactList();
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
