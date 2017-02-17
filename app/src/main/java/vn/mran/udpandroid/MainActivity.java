package vn.mran.udpandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;

import vn.mran.udpandroid.util.Utils;

public class MainActivity extends AppCompatActivity implements MainView {
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

        presenter = new MainPresenter(this);

        displayMyIpAddress();
    }

    private void displayMyIpAddress() {
        port = (int) (Math.random() * 9999) + 1000;
        String ipAndPort = Utils.getIPAddress(true) + File.separator + port;
        txtMyIP.setText(txtMyIP.getText() + ipAndPort);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private EditText getEdtP2pIp() {
        return (EditText) findViewById(R.id.edtP2pIp);
    }

    private EditText getEdtP2pPort() {
        return (EditText) findViewById(R.id.edtP2pPort);
    }
}
