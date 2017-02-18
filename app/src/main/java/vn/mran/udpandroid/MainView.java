package vn.mran.udpandroid;

import android.graphics.Bitmap;
import android.view.View;

import java.io.File;

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

    void onReceiveVideoSuccess(File file , String ip);

    void onReceiveVideoError();

    void loading(String message);

    void onProgressUpdate(int value);
}
