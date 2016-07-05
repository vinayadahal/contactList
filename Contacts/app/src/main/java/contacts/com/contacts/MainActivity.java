package contacts.com.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatButton appCompatButton = (AppCompatButton) findViewById(R.id.back_btn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
        toolbar.removeView(appCompatButton);
    }

    public void page2(View view) {
        Intent intent = new Intent(MainActivity.this, FileJsonWriter.class);
        startActivity(intent);
    }
}
