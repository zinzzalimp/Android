package com.jwseo.imagecabinet;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.webkit.URLUtil;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zinzz on 2016-08-02.
 */
public class DaumImageItem {

    public String pubDate;
    public String title;
    public String thumbnail;
    public String cp;
    public int height;
    public String link;
    public int width;
    //acual image address
    public String image;
    private boolean mChecked = false;

    public String getHashCode() {
        return mHashString;
    }

    private String mHashString;

    private void makeHashbyImageAddress() {
        String pass = image;
        MessageDigest mdEnc;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(pass.getBytes(), 0, pass.length());
            pass = new BigInteger(1, mdEnc.digest()).toString(16);
            while (pass.length() < 32) {
                pass = "0" + pass;
            }
            mHashString = pass;
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }

    public boolean getChecked() {
        return mChecked;
    }

    private Bitmap mThumbnailBitmap = null;
    private Bitmap mImageBitmap = null;

    public Bitmap getThumbnail() {
        return mThumbnailBitmap;
    }

    public Bitmap getImage() {
        return mImageBitmap;
    }

    public void instantiateItem(Bitmap imageNotFound) {
        //get Thumbnail Data
        if (URLUtil.isValidUrl(thumbnail)) {
            try {

                URL url = new URL(thumbnail);
                URLConnection conn = url.openConnection();
                conn.connect();

                int nSize = conn.getContentLength();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(conn.getInputStream(), nSize);
                mThumbnailBitmap = BitmapFactory.decodeStream(bufferedInputStream);
                bufferedInputStream.close();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                mThumbnailBitmap = imageNotFound;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //get Image BItmap
        /*if (URLUtil.isValidUrl(image)) {
            try {

                URL url = new URL(image);
                URLConnection conn = url.openConnection();
                conn.connect();

                int nSize = conn.getContentLength();

                BufferedInputStream bufferedInputStream = new BufferedInputStream(conn.getInputStream(), nSize);
                mImageBitmap = BitmapFactory.decodeStream(bufferedInputStream);
                bufferedInputStream.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                mImageBitmap = imageNotFound;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        //use hashkey
        makeHashbyImageAddress();
    }
}
