package contacts.components;


import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class MessageAlert {
    public void showToast(final String msg, final Context ctx) {
        Handler handler = new Handler(ctx.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
