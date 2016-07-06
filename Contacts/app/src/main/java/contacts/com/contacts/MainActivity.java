package contacts.com.contacts;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        AppCompatButton appCompatButton = (AppCompatButton) findViewById(R.id.back_btn);
        AppCompatImageButton appCompatImageButton = (AppCompatImageButton) findViewById(R.id.back_btn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
        toolbar.removeView(appCompatImageButton);
    }

    public void page2(View view) {
        Intent intent = new Intent(MainActivity.this, FileJsonWriter.class);
        startActivity(intent);
    }
}
