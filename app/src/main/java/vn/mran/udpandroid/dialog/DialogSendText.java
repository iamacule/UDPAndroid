package vn.mran.udpandroid.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import vn.mran.udpandroid.R;

/**
 * Created by AnPham on 07.01.2016.
 * <p>
 * Last modified on 19.01.2017
 * <p>
 * Copyright 2017 Audi AG, All Rights Reserved
 */
public class DialogSendText {
    public static class Build {

        public interface OnDialogSendTextListener {
            void onSend(String message);
        }

        private OnDialogSendTextListener onDialogSendTextListener;

        public void setOnDialogSendTextListener(OnDialogSendTextListener onDialogSendTextListener) {
            this.onDialogSendTextListener = onDialogSendTextListener;
        }

        private AlertDialog.Builder builder;
        private AlertDialog dialog;
        private TextView txtMessage;

        private Build(Activity activity) {
            builder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = activity.getLayoutInflater();
            View view = inflater.inflate(R.layout.text_input_dialog, null);
            builder.setView(view);
            dialog = builder.create();
            txtMessage = (TextView) view.findViewById(R.id.txtMessage);
            ((Button) view.findViewById(R.id.btnSend)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (txtMessage.getText().length()>0){
                        onDialogSendTextListener.onSend(txtMessage.getText().toString().trim());
                    }
                }
            });
        }

        public void show() {
            dismiss();
            if (dialog != null & !dialog.isShowing()) {
                dialog.show();
            }
        }

        public void dismiss() {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}
