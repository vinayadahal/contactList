package contacts.com.contacts;

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
//        AppCompatEditText appCompatEditText = (AppCompatEditText) findViewById(R.id.search_box);
        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
        toolbar.removeView(appCompatImageButton);
//        toolbar.removeView(appCompatEditText);
    }

    public void page2(View view) {
        Intent intent = new Intent(MainActivity.this, FileJsonWriter.class);
        startActivity(intent);
    }
}
