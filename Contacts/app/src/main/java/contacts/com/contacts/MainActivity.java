package contacts.com.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar Actionbar = (Toolbar) findViewById(R.id.action_bar);
        setSupportActionBar(Actionbar);
        getSupportActionBar().setTitle(null);
    }

    public void page2(View view) {
        Intent intent = new Intent(MainActivity.this, FileJsonWriter.class);
        startActivity(intent);
    }
}
