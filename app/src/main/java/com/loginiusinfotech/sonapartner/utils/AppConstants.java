package com.loginiusinfotech.sonapartner.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppConstants {
    public static final String baseUrl ="http://sonamarketing.in/glossyfinish/";

    public static final String SAVE_USER_ID="userId";
    public static final String SAVE_EMAIL="email";
    public static final String SAVE_ROLE_ID="role_id";
    public static final String SAVE_ROLE="role";
    public static final String SAVE_STATUS="status";
    public static final String SAVE_CAT_POSITION ="cat_position";
    public static final String SAVE_SUB_CAT_POSITION ="sub_cat_position";
    public static final String SAVE_MOBILE_NUMBER="mobileno";
    public static final String SAVE_WHATSAPP_NUMBER="whatsappno";

    public static void displayImageOriginal(Context ctx, ImageView img, String drawable) {
        try {
            Glide.with(ctx).load(drawable)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(img);
        } catch (Exception e) {
        }
    }
}
