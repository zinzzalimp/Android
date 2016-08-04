package com.jwseo.imagecabinet;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;

/**
 * Created by zinzz on 2016-08-04.
 */
public class CabinetGridView extends GridView {

    public CabinetGridView(Context context) {
        super(context);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        setColumnWidth(Utils.getDisplayWidth(getContext()) / 3);

        setNumColumns(3);
        setClickable(true);
        setFocusable(true);
        setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        setHorizontalSpacing(10);
        setVerticalSpacing(10);

    }


}
