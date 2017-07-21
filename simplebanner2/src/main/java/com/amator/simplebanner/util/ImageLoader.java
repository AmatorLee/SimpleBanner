package com.amator.simplebanner.util;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


/**
 * Created by AmatorLee on 2017/7/21.
 */

public class ImageLoader {


    public static void loadImage(Context context, Object url, ImageView imageView) {
        if (url instanceof String){
            Picasso.with(context).load((String)url).into(imageView);
        }else if(url instanceof Integer){
            Picasso.with(context).load((Integer)url).into(imageView);
        }else {
            throw new IllegalArgumentException("url type is not trustworthy");
        }
    }

}
