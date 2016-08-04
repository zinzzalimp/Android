package com.jwseo.imagecabinet.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jwseo.imagecabinet.util.DaumImageItem;
import com.jwseo.imagecabinet.ImageCabinetApplication;
import com.jwseo.imagecabinet.R;
import com.jwseo.imagecabinet.util.Utils;
import com.jwseo.imagecabinet.adapter.GridViewAdapter;
import com.jwseo.imagecabinet.view.CabinetGridView;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class SearchImageFragment extends Fragment {

    public static final String ACTION_CLEAR_CHECKED_VIEW = "ACTION_CLEAR_CHECKED_VIEW";
    private SearchView mSearchView;
    private GridViewAdapter mSearchGridAdapter;
    private String findImage_pre = "https://apis.daum.net/search/image?apikey=";
    private String findImage_med = "&q=";
    private String findImage_post = "&result=20&output=json";
    private GridView SearchGridView;
    private ProgressDialog progressDialog;
    private ImageCabinetApplication application;

    private Handler uiHandler = new Handler();
    private BroadcastReceiver SearchFragmentBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_CLEAR_CHECKED_VIEW)) {
                clearChecked();
            }
        }
    };

    private void clearChecked() {
        for (int i = 0; i < SearchGridView.getChildCount(); i++) {
            SearchGridView.getChildAt(i).findViewById(R.id.check_image).setVisibility(View.INVISIBLE);
            SearchGridView.getChildAt(i).findViewById(R.id.uncheck_image).setVisibility(View.VISIBLE);
        }
    }

    public class GetImageDataTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Processing....");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            //Parsing Json Data using Gson
            //clear common storage
            application.clearImageData();
            //clear GridView for Memory
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (SearchGridView != null) {
                        ((ViewGroup) getView()).removeView(SearchGridView);
                        SearchGridView = null;
                        System.gc();
                    }
                }
            });
            String query_url = params[0];
            try {
                getJSONFromUrl(query_url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            //make Search GridView
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    //if already added remove

                    SearchGridView = new CabinetGridView(getContext());
                    SearchGridView.setOnItemClickListener(gridViewOnItemClickListener);
                    ArrayList<DaumImageItem> list = new ArrayList<>(application.getImageData().values());
                    mSearchGridAdapter = new GridViewAdapter(getContext(), R.layout.image_item_layout, list);
                    mSearchGridAdapter.setGridViewAdapterType(GridViewAdapter.GridViewAdapterType.SEARCH_GRIDVIEW);
                    SearchGridView.setAdapter(mSearchGridAdapter);
                    //fragment view
                    ((LinearLayout) getView()).addView(SearchGridView);
                    mSearchGridAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    public SearchImageFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private GridView.OnItemClickListener gridViewOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (view.findViewById(R.id.uncheck_image).getVisibility() == View.VISIBLE) {
                view.findViewById(R.id.uncheck_image).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.check_image).setVisibility(View.VISIBLE);
                application.checkItem(((DaumImageItem) parent.getItemAtPosition(position)).getHashCode(), true);


            } else {
                view.findViewById(R.id.uncheck_image).setVisibility(View.VISIBLE);
                view.findViewById(R.id.check_image).setVisibility(View.INVISIBLE);
                application.checkItem(((DaumImageItem) parent.getItemAtPosition(position)).getHashCode(), false);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View fragment_view = inflater.inflate(R.layout.fragment_search_image, container, false);
        mSearchView = (SearchView) fragment_view.findViewById(R.id.search_image_view);
        mSearchView.setOnQueryTextListener(mSearchQueryListener);
        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ((SearchView) v).setQuery("", false);
                }
            }
        });
        //mSearchView.setOnClickListener(mSearchViewOnClickListener);
        progressDialog = new ProgressDialog(getContext());
        application = (ImageCabinetApplication) getActivity().getApplication();
        return fragment_view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_CLEAR_CHECKED_VIEW);
        getActivity().registerReceiver(SearchFragmentBroadcastReceiver, filter);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().unregisterReceiver(SearchFragmentBroadcastReceiver);
    }

    private SearchView.OnQueryTextListener mSearchQueryListener = new SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextSubmit(String query) {
            String query_url = makeSearchURL(getString(R.string.imageCabinet_API_KEY), query);

            GetImageDataTask getImageDataTask = new GetImageDataTask();
            getImageDataTask.execute(query_url);

            mSearchView.clearFocus();
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            //do nothing
            return false;
        }
    };

    private String makeSearchURL(String api_key, String query) {
        return findImage_pre + api_key + findImage_med + query + findImage_post;
    }

    public JSONObject getJSONFromUrl(String url_str) throws MalformedURLException {
        StringBuilder jsonString = new StringBuilder();
        BitmapFactory.Options option = new BitmapFactory.Options();
        //scaling image not found bitmap
        option.inSampleSize = Utils.getDisplayWidth(getContext()) / 3;
        Bitmap image_not_found = BitmapFactory.decodeResource(getResources(), R.drawable.image_not_found, option);
        if (!URLUtil.isValidUrl(url_str))
            throw new MalformedURLException();
        if (URLUtil.isHttpUrl(url_str)) {
            URL url = new URL(url_str);
            HttpURLConnection urlConnection;

            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(false);

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null)
                    jsonString.append(line);
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObject = (JsonObject) jsonParser.parse(jsonString.toString());

                JsonArray jsonArray = jsonObject.getAsJsonObject("channel").getAsJsonArray("item");

                for (JsonElement element : jsonArray) {
                    Gson get_imageItem = new Gson();
                    DaumImageItem elem = get_imageItem.fromJson(element, DaumImageItem.class);
                    elem.instantiateItem(BitmapFactory.decodeResource(getResources(), R.drawable.image_not_found));
                    application.addImageData(elem);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
        } else if (URLUtil.isHttpsUrl(url_str)) {
            URL url = new URL(url_str);
            HttpURLConnection urlConnection;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            try {

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null)
                    jsonString.append(line);
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObject = (JsonObject) jsonParser.parse(jsonString.toString());

                JsonArray jsonArray = jsonObject.getAsJsonObject("channel").getAsJsonArray("item");

                for (JsonElement element : jsonArray) {
                    Gson get_imageItem = new Gson();
                    DaumImageItem elem = get_imageItem.fromJson(element, DaumImageItem.class);
                    elem.instantiateItem(image_not_found);
                    application.addImageData(elem);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }

        }

        return null;
    }


}
