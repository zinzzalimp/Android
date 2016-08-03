package com.jwseo.imagecabinet;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by zinzz on 2016-08-01.
 */
public class GalleryFragmentAdapter extends FragmentStatePagerAdapter {

    //앱은 2개의 fragment만 가진다.
    private int mTabCount = 0;
    private static final int IMAGE_SERARCH_TAB_INDEX = 0;
    private static final int IMAGE_CABINET_INDEX = 1;

    public GalleryFragmentAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        mTabCount = tabCount;
    }


    @Override
    public Fragment getItem(int position) {
        if (position == IMAGE_SERARCH_TAB_INDEX) {
            SearchImageFragment searchImageFragment = new SearchImageFragment();
            return searchImageFragment;
        } else if (position == IMAGE_CABINET_INDEX) {
            CabinetFragment cabinetFragment = new CabinetFragment();
            return cabinetFragment;

        }
        return null;
    }

    @Override
    public int getCount() {
        return mTabCount;
    }
}
