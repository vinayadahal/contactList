package contacts.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatImageButton appCompatImageButton = (AppCompatImageButton) findViewById(R.id.back_btn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
        toolbar.removeView(appCompatImageButton);
    }

    public void page2(View view) {
        Intent intent = new Intent(MainActivity.this, ContactList.class);
        startActivity(intent);
    }
}
