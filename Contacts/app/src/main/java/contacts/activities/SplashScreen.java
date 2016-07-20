package contacts.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final Intent intent = new Intent(this, MainActivity.class);
        Thread lTimer = new Thread() {
            public void run() {
                try {
                    int lTimer1 = 0;
                    while (lTimer1 < 5000) {
                        sleep(100);
                        lTimer1 = lTimer1 + 100;
                    }
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }
            }
        };
        lTimer.start();
    }

//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//        finish();
//}
}
