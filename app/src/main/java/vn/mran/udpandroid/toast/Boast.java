package vn.mran.udpandroid.toast;

import android.annotation.SuppressLint;
import android.content.Context;

public class Boast {
    private volatile static Boast globalBoast = null;
    private ToastInfo internalToast;

    private Boast(ToastInfo toast) {
        internalToast = toast;
    }

    @SuppressLint("ShowToast")
    public static Boast makeText(Context context, String message) {
        return new Boast(new ToastInfo(context, message));
    }

    public void cancel() {
        internalToast.cancel();
    }

    public void show() {
        show(true);
    }

    public void show(boolean cancelCurrent) {
        if (cancelCurrent && (globalBoast != null)) {
            globalBoast.cancel();
        }
        globalBoast = this;

        internalToast.show();
    }
}