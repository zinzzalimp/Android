package com.jwseo.imagecabinet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.ArrayList;


public class CabinetFragment extends Fragment {
    private GridViewAdapter mCabinetGridAdapter;
    ImageCabinetApplication application;
    GridView CabinetGridView;

    public CabinetFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment_view = inflater.inflate(com.jwseo.imagecabinet.R.layout.fragment_cabinet, container, false);
        application = (ImageCabinetApplication) getActivity().getApplication();

        return fragment_view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_CLEAR_CABINET_GRID_VIEW);
        filter.addAction(ACTION_REFRESH_SEARCH_GRID_VIEW);
        getActivity().registerReceiver(CabinetBroadcastReceiver, filter);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().unregisterReceiver(CabinetBroadcastReceiver);
    }

    private int getDisplayWidth() {
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    private Handler uiHandler = new Handler();

    public void refreshGridView() {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                CabinetGridView = new GridView(getContext());
                CabinetGridView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                CabinetGridView.setColumnWidth(getDisplayWidth() / 3);

                CabinetGridView.setNumColumns(3);
                CabinetGridView.setClickable(true);
                CabinetGridView.setFocusable(true);
                CabinetGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
                //CabinetGridView.setOnItemClickListener(gridViewOnItemClickListener);
                ArrayList<DaumImageItem> list = new ArrayList<>(application.getCabinetItemMap().values());
                mCabinetGridAdapter = new GridViewAdapter(getContext(), R.layout.image_item_layout, list);
                mCabinetGridAdapter.setGridViewAdapterType(GridViewAdapter.GridViewAdapterType.CABINET_GRIDVIEW);
                CabinetGridView.setAdapter(mCabinetGridAdapter);
                //fragment view
                ((ViewGroup) getView()).addView(CabinetGridView);
                mCabinetGridAdapter.notifyDataSetChanged();
            }
        });
    }


    public void clearView() {
        ((ViewGroup) getView()).removeAllViews();
        CabinetGridView = null;
        System.gc();
    }

    public static final String ACTION_REFRESH_SEARCH_GRID_VIEW = "ACTION_REFRESH_CABINET_GRID_VIEW";
    public static final String ACTION_CLEAR_CABINET_GRID_VIEW = "ACTION_CLEAR_CABINET_GRID_VIEW";

    private BroadcastReceiver CabinetBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_REFRESH_SEARCH_GRID_VIEW)) {
                refreshGridView();
            } else if (action.equals(ACTION_CLEAR_CABINET_GRID_VIEW)) {
                clearView();
            }
        }
    };
}
