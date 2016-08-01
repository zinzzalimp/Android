package com.jwseo.imagecabinet;

import android.content.Context;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by zinzz on 2016-08-01.
 */
public class GalleryItem {
    private ViewGroup mItemView;
    private Context mContext;
    private ImageView mImage;
    private ImageView mCheckItemImageView;
    private ImageView mUncheckItemImageView;
    private boolean mChecked = false;
    public GalleryItem(Context context) {
        mContext = context;
        ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.image_item_layout,mItemView);
        if(mItemView != null)
        {
            mImage = (ImageView) mItemView.findViewById(R.id.image_item);
            mCheckItemImageView = (ImageView) mItemView.findViewById(R.id.check_image);
            mUncheckItemImageView = (ImageView) mItemView.findViewById(R.id.uncheck_image);
            mCheckItemImageView.setVisibility(View.INVISIBLE);
            mUncheckItemImageView.setVisibility(View.INVISIBLE);
        }
        //if not inflate view, throw inflate Exception
        else {
            throw new InflateException("Inflate Failed", new Throwable());
        }
    }


}
