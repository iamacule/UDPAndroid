package vn.mran.udpandroid.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
public class DialogShowImage {
    public static class Build {

        private AlertDialog.Builder builder;
        private AlertDialog dialog;
        private ImageView img;
        private TextView txtTitle;

        public Build(final Activity activity) {
            builder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = activity.getLayoutInflater();
            View view = inflater.inflate(R.layout.image_show_dialog, null);
            builder.setView(view);
            dialog = builder.create();
            img = (ImageView) view.findViewById(R.id.img);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            view.findViewById(R.id.btnOK).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }

        public Build setTitle(String title){
            txtTitle.setText(title);
            return this;
        }

        public Build setImage(Bitmap bitmap){
            img.setImageBitmap(bitmap);
            return this;
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
