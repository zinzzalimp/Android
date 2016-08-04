package com.jwseo.imagecabinet;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;

public class CabinetActivity extends AppCompatActivity {

    private GalleryFragmentAdapter mFragmentAdapter;
    private TabLayout mGalleryTabLayout;
    private ViewPager mCabinetPager;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cabinetmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.savemenu:
                ((ImageCabinetApplication) getApplication()).saveCheckedItem();
                Toast.makeText(this, "Image Saved", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cabinet);

        mGalleryTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mGalleryTabLayout.addTab(mGalleryTabLayout.newTab().setText("IMAGES"));
        mGalleryTabLayout.addTab(mGalleryTabLayout.newTab().setText("MY CABINETS"));
        mGalleryTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mCabinetPager = (ViewPager) findViewById(R.id.cabinet_viewPager);
        mFragmentAdapter = new GalleryFragmentAdapter(this, getSupportFragmentManager(), mGalleryTabLayout.getTabCount());
        mCabinetPager.setAdapter(mFragmentAdapter);
        mCabinetPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mGalleryTabLayout));
        mGalleryTabLayout.setOnTabSelectedListener(tabSelectedListener);
    }

    private TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            if (mGalleryTabLayout != null) {
                mCabinetPager.setCurrentItem(tab.getPosition(), true);
                if (tab.getPosition() == GalleryFragmentAdapter.IMAGE_CABINET_INDEX) {
                    Intent refresh_cabinet_intent = new Intent();
                    refresh_cabinet_intent.setAction(CabinetFragment.ACTION_REFRESH_SEARCH_GRID_VIEW);
                    sendBroadcast(refresh_cabinet_intent);
                }
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            if (mGalleryTabLayout != null) {
                if (tab.getPosition() == GalleryFragmentAdapter.IMAGE_CABINET_INDEX) {
                    Intent clear_cabinet_intent = new Intent();
                    clear_cabinet_intent.setAction(CabinetFragment.ACTION_CLEAR_CABINET_GRID_VIEW);
                    sendBroadcast(clear_cabinet_intent);
                }
            }
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    };

}
