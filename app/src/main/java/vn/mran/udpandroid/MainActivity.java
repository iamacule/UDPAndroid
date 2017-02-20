package vn.mran.udpandroid;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;

import vn.mran.udpandroid.dialog.DialogSendText;
import vn.mran.udpandroid.dialog.DialogShowImage;
import vn.mran.udpandroid.dialog.DialogShowText;
import vn.mran.udpandroid.dialog.DialogShowVideo;
import vn.mran.udpandroid.toast.Boast;
import vn.mran.udpandroid.util.Utils;

public class MainActivity extends AppCompatActivity implements MainView, View.OnClickListener {
    private final int RESULT_LOAD_IMAGE = 0;
    private final int RESULT_LOAD_VIDEO = 1;
    private final String TAG = getClass().getSimpleName();

    private TextView txtMyIP;
    private CardView btnText;
    private CardView btnImage;
    private CardView btnFile;

    private String ip;
    private int port;

    private MainPresenter presenter;

    private DialogShowText.Build dialogShowText;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        dialogShowText = new DialogShowText.Build(this);

        txtMyIP = (TextView) findViewById(R.id.txtMyIP);
        btnText = (CardView) findViewById(R.id.btnText);
        btnImage = (CardView) findViewById(R.id.btnImage);
        btnFile = (CardView) findViewById(R.id.btnFile);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.setCanceledOnTouchOutside(false);

        displayMyIpAddress();

        presenter = new MainPresenter(this, port);
        presenter.startThread();

        btnText.setOnClickListener(this);
        btnImage.setOnClickListener(this);
        btnFile.setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.endThread();
    }

    private void displayMyIpAddress() {
        port = (int) (Math.random() * 9999) + 1000;
        ip = Utils.getIPAddress(true);
        String ipAndPort = ip + File.separator + port;
        txtMyIP.setText(txtMyIP.getText() + ipAndPort);
    }

    private EditText getEdtP2pIp() {
        return (EditText) findViewById(R.id.edtP2pIp);
    }

    private EditText getEdtP2pPort() {
        return (EditText) findViewById(R.id.edtP2pPort);
    }

    @Override
    public void onClick(View view) {
        if (errorMessage() == null) {
            presenter.setP2pData(view, getEdtP2pIp().getText().toString().trim(), getEdtP2pPort().getText().toString().trim());
        } else {
            Boast.makeText(getApplicationContext(), errorMessage()).show();
        }
    }

    private String errorMessage() {
        String error = null;
        if (getEdtP2pIp().getText().length() <= 0) {
            error = "Please input p2p IP Address";
        } else if (getEdtP2pPort().getText().length() <= 0) {
            error = "Please input p2p Port";
        }
        return error;
    }

    @Override
    public void onReceiveText(final String message, final String ip) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialogShowText.dismiss();
                dialogShowText.setTitle("Receive from : " + ip)
                        .setMessage(message)
                        .show();
            }
        });
    }

    @Override
    public void onPortError() {
        Boast.makeText(this, "Port error !").show();
    }

    @Override
    public void onIpError() {
        Boast.makeText(this, "IP error !").show();
    }

    @Override
    public void onCreateP2pSuccess(int id) {
        switch (id) {
            case R.id.btnText:
                presenter.sendMessage(presenter.TYPE_TEXT);
                new DialogSendText.Build(MainActivity.this)
                        .setOnDialogSendTextListener(new DialogSendText.Build.OnDialogSendTextListener() {
                            @Override
                            public void onSend(String message) {
                                presenter.sendMessage(message);
                            }

                            @Override
                            public void onCancel() {
                                presenter.sendMessage(presenter.CANCEL);
                            }
                        }).show();

                break;
            case R.id.btnImage:
                presenter.sendMessage(presenter.TYPE_IMAGE);
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
                break;
            case R.id.btnFile:
                presenter.sendMessage(presenter.TYPE_VIDEO);
                Intent intent2 = new Intent(Intent.ACTION_PICK);
                intent2.setType("video/*");
                startActivityForResult(intent2, RESULT_LOAD_VIDEO);
                break;
        }
    }

    @Override
    public void onSendImageSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                Boast.makeText(MainActivity.this, "Send image success !");
            }
        });
    }

    @Override
    public void onSendImageError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                Boast.makeText(MainActivity.this, "Send image error !");
            }
        });
    }

    @Override
    public void onReceiveImageError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                Boast.makeText(MainActivity.this, "Can not receive image").show();
            }
        });
    }

    @Override
    public void onReceiveImageSuccess(final Bitmap bitmap, final String ip) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                Log.d(TAG, "Width : " + bitmap.getWidth());
                Log.d(TAG, "Height : " + bitmap.getHeight());
                new DialogShowImage.Build(MainActivity.this)
                        .setTitle("Receive from : " + ip)
                        .setImage(bitmap).show();
            }
        });
    }

    @Override
    public void onReceiveVideoSuccess(final File file, final String ip) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                new DialogShowVideo.Build(MainActivity.this)
                        .setTitle("Receive from : " + ip)
                        .show(file);
            }
        });
    }

    @Override
    public void onReceiveVideoError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                Boast.makeText(MainActivity.this, "Can not receive video").show();
            }
        });
    }

    @Override
    public void loading(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.setMessage(message);
                progressDialog.show();
            }
        });
    }

    @Override
    public void onProgressUpdate(final int value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.setProgress(value);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            File file = new File(filePath);
            if (file.length() > 0) {
                String fileData = file.getName() +
                        File.separator +
                        ip +
                        File.separator +
                        String.valueOf(port + 1) +
                        File.separator +
                        file.length();
                presenter.sendMessage(fileData);
                SystemClock.sleep(20);
                presenter.sendFile(file);
            }
        } else {
            presenter.sendMessage(presenter.CANCEL);
            Boast.makeText(MainActivity.this, "Canceled !");
        }
    }

    private void requestPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                    1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
