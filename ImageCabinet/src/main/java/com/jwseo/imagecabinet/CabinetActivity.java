package com.jwseo.imagecabinet;

import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.TabLayout;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.HashMap;

public class CabinetActivity extends AppCompatActivity implements SearchImageFragment.OnItemSelectedListener {

    private GalleryFragmentAdapter mFragmentAdapter;
    private TabLayout mGalleryTabLayout;
    private ViewPager mCabinetPager;
    private HashMap<String, DaumImageItem> mCheckedImageData = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cabinet);
        mGalleryTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mGalleryTabLayout.addTab(mGalleryTabLayout.newTab().setText("IMAGES"));
        mGalleryTabLayout.addTab(mGalleryTabLayout.newTab().setText("MY CABINETS"));
        mGalleryTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mCabinetPager = (ViewPager) findViewById(R.id.cabinet_viewPager);
        mFragmentAdapter = new GalleryFragmentAdapter(getSupportFragmentManager(), mGalleryTabLayout.getTabCount());
        mCabinetPager.setAdapter(mFragmentAdapter);
        mCabinetPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mGalleryTabLayout));
        mGalleryTabLayout.setOnTabSelectedListener(tabSelectedListener);
    }

    private TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            if (mGalleryTabLayout != null) {
                mCabinetPager.setCurrentItem(tab.getPosition(), true);
                if (tab.getPosition() == 1) {
                    //((CabinetFragment)mFragmentAdapter.getItem(tab.getPosition())).refreshGridView();
                }
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {


        }
    };


    @Override
    public void OnSelected(DaumImageItem item) {

    }
}
