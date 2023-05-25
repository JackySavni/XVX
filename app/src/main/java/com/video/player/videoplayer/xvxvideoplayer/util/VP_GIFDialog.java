package com.video.player.videoplayer.xvxvideoplayer.util;

import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_isValidContext;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.video.player.videoplayer.xvxvideoplayer.R;

public class VP_GIFDialog {
    private String title, message, positiveBtnText, negativeBtnText, positiveBtnColor,
            negativeBtnColor, imgUri, imgType;
    private Activity activity;
    private GIFDialogListener positiveListener, negativeListener;
    private boolean cancel;
    int gifImageResource;

    private VP_GIFDialog(Builder builder) {
        this.title = builder.title;
        this.message = builder.message;
        this.activity = builder.activity;
        this.positiveListener = builder.positiveListener;
        this.negativeListener = builder.negativeListener;
        this.positiveBtnColor = builder.positiveBtnColor;
        this.negativeBtnColor = builder.negativeBtnColor;
        this.positiveBtnText = builder.positiveBtnText;
        this.negativeBtnText = builder.negativeBtnText;
        this.gifImageResource = builder.gifImageResource;
        this.imgUri = builder.imgUri;
        this.imgType = builder.imgType;
        this.cancel = builder.cancel;
    }

    public static class Builder {
        private String title, message, positiveBtnText, negativeBtnText, positiveBtnColor,
                negativeBtnColor, imgUri, imgType;
        private Activity activity;
        private GIFDialogListener positiveListener, negativeListener;
        private boolean cancel;
        int gifImageResource;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setPositiveBtnText(String positiveBtnText) {
            this.positiveBtnText = positiveBtnText;
            return this;
        }

        public Builder setNegativeBtnText(String negativeBtnText) {
            this.negativeBtnText = negativeBtnText;
            return this;
        }

        public Builder setPositiveBtnBackground(String positiveBtnColor) {
            this.positiveBtnColor = positiveBtnColor;
            return this;
        }

        public Builder setNegativeBtnBackground(String negativeBtnColor) {
            this.negativeBtnColor = negativeBtnColor;
            return this;
        }

        //set Positive Listener
        public Builder OnPositiveClicked(GIFDialogListener positiveListener) {
            this.positiveListener = positiveListener;
            return this;
        }

        //set Negative Listener
        public Builder OnNegativeClicked(GIFDialogListener negativeListener) {
            this.negativeListener = negativeListener;
            return this;
        }

        public Builder isCancellable(boolean cancel) {
            this.cancel = cancel;
            return this;
        }

        //set GIF Resource
        public Builder setGifResource(String imgType, int gifImageResource,
                                                String imgUri) {
            this.imgType = imgType;
            this.gifImageResource = gifImageResource;
            this.imgUri = imgUri;
            return this;
        }

        public VP_GIFDialog build() {
            TextView message1, title1;
            ImageView iconImg;
            final Button pBtn, nBtn;
            ImageView gifImageView;
            final Dialog dialog;
            dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(cancel);
            dialog.setContentView(R.layout.vp_rate_dialog);

            //init
            title1 = (TextView) dialog.findViewById(R.id.vp_title);
            message1 = (TextView) dialog.findViewById(R.id.vp_message);
            pBtn = (Button) dialog.findViewById(R.id.vp_positiveBtn);
            nBtn = (Button) dialog.findViewById(R.id.vp_negativeBtn);
            gifImageView = dialog.findViewById(R.id.vp_gifImageView);

            if (imgType.equals("gif")) {
                if (vp_isValidContext(activity)) {
                    try {
                        Glide.with(activity).asGif().load(gifImageResource).into(gifImageView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (vp_isValidContext(activity)) {
                    try {
                        Glide.with(activity).load(gifImageResource).into(gifImageView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            //values
            title1.setText(title);
            message1.setText(message);
            if (positiveBtnText != null) {
                pBtn.setText(positiveBtnText);
            }
            if (negativeBtnText != null) {
                nBtn.setText(negativeBtnText);
            }
            if (positiveBtnColor != null) {
                GradientDrawable bgShape = (GradientDrawable) pBtn.getBackground();
                bgShape.setColor(Color.parseColor(positiveBtnColor));
            }
            if (negativeBtnColor != null) {
                GradientDrawable bgShape = (GradientDrawable) nBtn.getBackground();
                bgShape.setColor(Color.parseColor(negativeBtnColor));
            }
            if (positiveListener != null) {
                pBtn.setOnClickListener(view -> {
                    positiveListener.OnClick();
                    dialog.dismiss();
                });
            } else {
                pBtn.setOnClickListener(view -> dialog.dismiss());
            }

            if (negativeListener != null) {
                nBtn.setVisibility(View.VISIBLE);
                nBtn.setOnClickListener(view -> {
                    negativeListener.OnClick();
                    dialog.dismiss();
                });
            }

            dialog.show();

            return new VP_GIFDialog(this);
        }
    }

    public interface GIFDialogListener {
        void OnClick();
    }
}
