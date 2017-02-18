package vn.mran.udpandroid;

import android.graphics.Bitmap;
import android.view.View;

/**
 * Created by thong on 2/17/2017.
 * PROJECT UDPAndroid
 */

public interface MainView {
    void onReceiveText(String message, String ip);

    void onPortError();

    void onIpError();

    void onCreateP2pSuccess(int id);

    void onSendImageSuccess();

    void onSendImageError();

    void onReceiveImageError();

    void onReceiveImageSuccess(Bitmap bitmap, String ip);

    void loading(String message);
}
