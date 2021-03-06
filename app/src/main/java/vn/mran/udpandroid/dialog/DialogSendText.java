package vn.mran.udpandroid.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import vn.mran.udpandroid.R;
import vn.mran.udpandroid.toast.Boast;

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

            void onCancel();
        }

        private OnDialogSendTextListener onDialogSendTextListener;

        public Build setOnDialogSendTextListener(OnDialogSendTextListener onDialogSendTextListener) {
            this.onDialogSendTextListener = onDialogSendTextListener;
            return this;
        }

        private AlertDialog.Builder builder;
        private AlertDialog dialog;
        private TextView txtMessage;

        public Build(final Activity activity) {
            builder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = activity.getLayoutInflater();
            View view = inflater.inflate(R.layout.text_input_dialog, null);
            builder.setView(view);
            dialog = builder.create();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    onDialogSendTextListener.onCancel();
                }
            });
            txtMessage = (TextView) view.findViewById(R.id.txtMessage);
            view.findViewById(R.id.btnSend).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (txtMessage.getText().length() > 0) {
                        onDialogSendTextListener.onSend(txtMessage.getText().toString().trim());
                        dismiss();
                    } else {
                        Boast.makeText(activity, "Please input message !").show();
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
