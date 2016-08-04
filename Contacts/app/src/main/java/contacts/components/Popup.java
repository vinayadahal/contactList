package contacts.components;

import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class Popup {


    public PopupWindow showPopup(View choice_pop_up, LinearLayout.LayoutParams llp) {
        PopupWindow pw = new PopupWindow(choice_pop_up, llp.width, llp.height, true);
        pw.setBackgroundDrawable(new ColorDrawable()); //helped me to hide popup
        pw.setOutsideTouchable(true);
        pw.setTouchable(true);
        return pw;
    }


}
