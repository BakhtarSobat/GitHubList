package com.bsobat.github.ui.util;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public final class DataBinder {
    private DataBinder() {
        //NO-OP
    }

    @BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        if(url == null) return;
        Context context = imageView.getContext();
        Glide.with(context).load(url).into(imageView);
    }
}
