package com.jwseo.imagecabinet;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by zinzz on 2016-08-01.
 */
public class GalleryFragmentAdapter extends FragmentStatePagerAdapter {

    //앱은 2개의 fragment만 가진다.
    public static final int MAX_FRAGMENT_SIZE = 2;

    public GalleryFragmentAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return MAX_FRAGMENT_SIZE;
    }
}
