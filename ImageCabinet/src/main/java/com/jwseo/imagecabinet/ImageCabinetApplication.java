package com.jwseo.imagecabinet;

import android.app.Application;

import com.jwseo.imagecabinet.util.DaumImageItem;

import java.util.HashMap;

/**
 * Created by zinzz on 2016-08-03.
 * use only for Common Storage
 */
public class ImageCabinetApplication extends Application {
    private HashMap<String, DaumImageItem> mImageItemMap = new HashMap<>();
    private HashMap<String, DaumImageItem> mCabinetItemMap = new HashMap<>();

    public HashMap<String, DaumImageItem> getImageData() {
        return mImageItemMap;
    }

    public HashMap<String, DaumImageItem> getCabinetItemMap() {
        return mCabinetItemMap;
    }

    public boolean addImageData(DaumImageItem item) {
        if (mImageItemMap.containsKey(item.getHashCode()))
            return false;
        else {
            mImageItemMap.put(item.getHashCode(), item);
            return true;
        }
    }

    public void checkItem(String key, boolean checked) {
        if (mImageItemMap.containsKey(key)) {
            mImageItemMap.get(key).setChecked(checked);
            /*if (checked)
                mCabinetItemMap.put(key, mImageItemMap.get(key));
            else
                mCabinetItemMap.remove(key);*/
        }
    }


    public void clearImageData() {
        mImageItemMap.clear();
    }

    private HashMap<String, DaumImageItem> getCheckedItems() {
        HashMap<String, DaumImageItem> result = new HashMap<>();
        for (String key : mImageItemMap.keySet()) {
            if (mImageItemMap.get(key).getChecked()) {
                result.put(key, mImageItemMap.get(key));
            }
        }
        return result;
    }

    public void saveCheckedItem() {
        for (String key : mImageItemMap.keySet()) {
            if (mImageItemMap.get(key).getChecked()) {
                mCabinetItemMap.put(key, mImageItemMap.get(key));
            }
        }
    }
}
