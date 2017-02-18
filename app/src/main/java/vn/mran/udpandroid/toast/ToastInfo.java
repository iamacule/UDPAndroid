package vn.mran.udpandroid.toast;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import vn.mran.udpandroid.R;


/**
 * Created by MrAn on 04-May-16.
 */
public class ToastInfo {
    private Toast toast;

    public ToastInfo(Context context, String message) {
        create(context, message);
    }

    public void cancel() {
        toast.cancel();
    }

    public void show() {
        toast.show();
    }

    private void create(Context context, String message) {
        View view = View.inflate(context, R.layout.toast, null);
        TextView text = (TextView) view.findViewById(R.id.toast_message);
        text.setText(message);

        toast = new Toast(context.getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
    }
}
