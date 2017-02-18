package vn.mran.udpandroid.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;

import vn.mran.udpandroid.R;

/**
 * Created by AnPham on 07.01.2016.
 * <p>
 * Last modified on 19.01.2017
 * <p>
 * Copyright 2017 Audi AG, All Rights Reserved
 */
public class DialogShowVideo {
    public static class Build {

        private AlertDialog.Builder builder;
        private AlertDialog dialog;
        private VideoView videoView;
        private TextView txtTitle;
        private Activity ac;

        public Build(final Activity activity) {
            ac = activity;
            builder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = activity.getLayoutInflater();
            View view = inflater.inflate(R.layout.video_show_dialog, null);
            builder.setView(view);
            dialog = builder.create();
            videoView = (VideoView) view.findViewById(R.id.videoView);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            view.findViewById(R.id.btnOK).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (videoView.isPlaying()) videoView.stopPlayback();
                    dismiss();
                }
            });
        }

        public Build setTitle(String title) {
            txtTitle.setText(title);
            return this;
        }

        public void show(File file) {
            dismiss();
            if (dialog != null & !dialog.isShowing()) {
                dialog.show();
                videoView.setMediaController(new MediaController(ac));
                videoView.setVideoPath(file.getPath());
                videoView.requestFocus();
                videoView.start();
            }
        }

        public void dismiss() {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}
