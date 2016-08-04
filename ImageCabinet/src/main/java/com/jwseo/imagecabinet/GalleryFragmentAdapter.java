package com.jwseo.imagecabinet;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

/**
 * Created by zinzz on 2016-08-01.
 */
public class GalleryFragmentAdapter extends FragmentStatePagerAdapter {

    //앱은 2개의 fragment만 가진다.
    private int mTabCount = 0;
    public static final int IMAGE_SERARCH_TAB_INDEX = 0;
    public static final int IMAGE_CABINET_INDEX = 1;
    private Context mContext;
    private CabinetActivity mCabinetActivity;

    public GalleryFragmentAdapter(Context context, FragmentManager fm, int tabCount) {
        super(fm);
        mContext = context;
        mTabCount = tabCount;
        mCabinetActivity = (CabinetActivity) mContext;
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

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
