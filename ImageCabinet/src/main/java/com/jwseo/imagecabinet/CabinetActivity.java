package com.jwseo.imagecabinet;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabItem;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CabinetActivity extends AppCompatActivity {

    private GalleryFragmentAdapter mFragmentAdapter;
    private TabLayout mGalleryTabLayout;
    private TabItem mGallerySearchItem;
    private TabItem mGalleryCabinet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cabinet);
        mGalleryTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mGalleryCabinet = (TabItem) findViewById(R.id.tab_my_cabinet);
        mGallerySearchItem = (TabItem) findViewById(R.id.tab_search_Image);
        mGalleryTabLayout.setBackgroundColor(Color.YELLOW);
        mFragmentAdapter = new GalleryFragmentAdapter(getSupportFragmentManager());
    }

    private View.OnClickListener mSearchOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnClickListener mCabinetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
