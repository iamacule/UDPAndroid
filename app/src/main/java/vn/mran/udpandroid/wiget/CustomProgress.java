package vn.mran.udpandroid.wiget;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import vn.mran.udpandroid.R;

/**
 * Created by An Pham on 20-Feb-17.
 * Last modifined on 20-Feb-17
 */

public class CustomProgress {
    private View view;

    public CustomProgress(View view) {
        this.view = view;
        setVisibility(View.GONE);
        view.findViewById(R.id.root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Add this for disable click event below
            }
        });
    }

    public void setVisibility(int vis) {
        view.findViewById(R.id.root).setVisibility(vis);
    }

    public void setPercent(int value) {
        ((TextView) view.findViewById(R.id.txtPercent)).setText(String.valueOf(value));
    }

    public void setMessage(String message) {
        ((TextView) view.findViewById(R.id.txtMessage)).setText(message);
    }
}
