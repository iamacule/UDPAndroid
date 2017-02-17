package vn.mran.udpandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import vn.mran.udpandroid.util.Utils;

public class MainActivity extends AppCompatActivity implements MainView, View.OnClickListener {
    private TextView txtMyIP;
    private CardView btnText;
    private CardView btnImage;
    private CardView btnFile;

    private int port;

    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtMyIP = (TextView) findViewById(R.id.txtMyIP);
        btnText = (CardView) findViewById(R.id.btnText);
        btnImage = (CardView) findViewById(R.id.btnImage);
        btnFile = (CardView) findViewById(R.id.btnFile);

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
        String ipAndPort = Utils.getIPAddress(true) + File.separator + port;
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
        switch (view.getId()) {
            case R.id.btnText:
                if (errorMessage() == null) {
                    presenter.setP2pIP(getEdtP2pIp().getText().toString().trim());
                    presenter.setP2pPort(Integer.parseInt(getEdtP2pPort().getText().toString().trim()));
                    presenter.sendMessage(presenter.TYPE_TEXT);
                } else {
                    Toast.makeText(getApplicationContext(), errorMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnImage:
                if (errorMessage() == null) {

                } else {
                    Toast.makeText(getApplicationContext(), errorMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnFile:
                if (errorMessage() == null) {

                } else {
                    Toast.makeText(getApplicationContext(), errorMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
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
    public void onReceiveText(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
