package com.jwseo.imagecabinet.adapter;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.jwseo.imagecabinet.util.DaumImageItem;
import com.jwseo.imagecabinet.R;

import java.util.ArrayList;

/**
 * Created by zinzz on 2016-08-03.
 */
public class GridViewAdapter extends ArrayAdapter {
    public enum GridViewAdapterType {
        SEARCH_GRIDVIEW,
        CABINET_GRIDVIEW,
    }
    private Context mContext;
    private int mlayoutResourceId;
    private ArrayList mData = new ArrayList();
    private boolean isCabinet = false;

    public void setGridViewAdapterType(GridViewAdapterType type) {
        if (type.equals(GridViewAdapterType.SEARCH_GRIDVIEW))
            isCabinet = false;
        else if (type.equals(GridViewAdapterType.CABINET_GRIDVIEW))
            isCabinet = true;
    }

    public boolean isCabinetType() {
        return isCabinet;
    }


    public static class GridViewHolder {
        ImageView mainImage;
        ImageView checkImage;
        ImageView unCheckImage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        GridViewHolder holder = null;
        if (row == null) {
            row = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(mlayoutResourceId, parent, false);
            int width = getDisplayWidth();
            row.setLayoutParams(new ViewGroup.LayoutParams(width / 3, width / 3));
            holder = new GridViewHolder();
            holder.mainImage = (ImageView) row.findViewById(R.id.image_item);
            holder.checkImage = (ImageView) row.findViewById(R.id.check_image);
            holder.unCheckImage = (ImageView) row.findViewById(R.id.uncheck_image);
            if (!isCabinet)
                holder.unCheckImage.setVisibility(View.VISIBLE);
            row.setTag(holder);
        } else {
            holder = (GridViewHolder) row.getTag();
        }

        DaumImageItem item = (DaumImageItem) mData.get(position);
        holder.mainImage.setImageBitmap(item.getThumbnail());

        return row;
    }

    public GridViewAdapter(Context context, int layoutResourceId, ArrayList data) {
        super(context, layoutResourceId, data);

        mContext = context;
        mlayoutResourceId = layoutResourceId;
        mData = data;
    }

    public GridViewAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);

        mContext = context;
        mlayoutResourceId = layoutResourceId;
    }

    public void setList(ArrayList data) {
        mData = data;
    }

    private int getDisplayWidth() {
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }
}
