package com.jwseo.imagecabinet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.hardware.display.DisplayManagerCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;


public class SearchImageFragment extends Fragment {

    private SearchView mSearchView;
    private GridViewAdapter mSearchGridAdapter;
    private String findImage_pre = "https://apis.daum.net/search/image?apikey=";
    private String findImage_med = "&q=";
    private String findImage_post = "&output=json";
    private GridView SearchGridView;
    private ProgressDialog progressDialog;
    private ImageCabinetApplication application;



    private Handler uiHandler = new Handler();

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

                    SearchGridView = new GridView(getContext());
                    SearchGridView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                    SearchGridView.setColumnWidth(getDisplayWidth() / 3);

                    SearchGridView.setNumColumns(3);
                    SearchGridView.setClickable(true);
                    SearchGridView.setFocusable(true);
                    SearchGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
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

    private int getDisplayWidth() {
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }
    public class TestGetImageTask extends AsyncTask<String, Void, Void> {
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
            //TODO: Change to use Daum API
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
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(testJSON);

            JsonArray jsonArray = jsonObject.getAsJsonObject("channel").getAsJsonArray("item");

            for (JsonElement element : jsonArray) {
                Gson get_imageItem = new Gson();
                DaumImageItem elem = get_imageItem.fromJson(element, DaumImageItem.class);
                elem.instantiateItem(BitmapFactory.decodeResource(getResources(), R.drawable.image_not_found));
                application.addImageData(elem);
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

                    SearchGridView = new GridView(getContext());
                    SearchGridView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                    SearchGridView.setColumnWidth(getDisplayWidth() / 3);

                    SearchGridView.setNumColumns(3);
                    SearchGridView.setClickable(true);
                    SearchGridView.setFocusable(true);
                    SearchGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
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

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private SearchView.OnQueryTextListener mSearchQueryListener = new SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextSubmit(String query) {
            String query_url = makeSearchURL(getString(R.string.imageCabinet_API_KEY), query);

            GetImageDataTask getImageDataTask = new GetImageDataTask();
            getImageDataTask.execute(query_url);

            //ignore others but item
            //TestGetImageTask testGetImageTask = new TestGetImageTask();
            //testGetImageTask.execute(query_url);
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

        if (!URLUtil.isValidUrl(url_str))
            throw new MalformedURLException();
        if (URLUtil.isHttpUrl(url_str)) {
            URL url = new URL(url_str);
            //URL url = new URL("http://newy.tistory.com/entry/post-2");
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
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
        } else if (URLUtil.isHttpsUrl(url_str)) {
            URL url = new URL(url_str);
            HttpURLConnection urlConnection;
            StringBuilder jsonString = new StringBuilder();
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
                    elem.instantiateItem(BitmapFactory.decodeResource(getResources(), R.drawable.image_not_found));
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


    private static final String testJSON = "{\n" +
            "  \"channel\": {\n" +
            "    \"result\": \"15\",\n" +
            "    \"pageCount\": \"48\",\n" +
            "    \"title\": \"Search Daum Open API\",\n" +
            "    \"totalCount\": \"49\",\n" +
            "    \"description\": \"Daum Open API search result\",\n" +
            "    \"item\": [\n" +
            "      {\n" +
            "        \"pubDate\": \"20160607194356\",\n" +
            "        \"title\": \"&lt;b&gt;ì&lt;/b&gt;£½&lt;b&gt;ì ë&lt;/b&gt;¨&lt;b&gt;ì&lt;/b&gt;± &lt;b&gt;ì ì&lt;/b&gt; ëê³ &lt;b&gt;ì&lt;/b&gt;½í¼ëì &lt;b&gt;ì¹ë&lt;/b&gt;¶ëª¨ &lt;b&gt;ë&lt;/b&gt;²&lt;b&gt;ì&lt;/b&gt;  ê³µë°©\",\n" +
            "        \"thumbnail\": \"https://search2.kakaocdn.net/argon/130x130_85_c/RVuZBgQGrN\",\n" +
            "        \"cp\": \"728111\",\n" +
            "        \"height\": \"220\",\n" +
            "        \"link\": \"http://blog.naver.com/alclssu01/220730099997\",\n" +
            "        \"width\": \"220\",\n" +
            "        \"image\": \"http://dthumb.phinf.naver.net/?src=%22http%3A%2F%2Fimage.fnnews.com%2Fresource%2Fmedia%2Fimage%2F2016%2F06%2F04%2F201606040502051648_m.jpg%22&type=f220\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"pubDate\": \"20160408111103\",\n" +
            "        \"title\": \"[&lt;b&gt;ì¹&lt;/b&gt;´ëë´&lt;b&gt;ì&lt;/b&gt;¤] &lt;b&gt;ë&lt;/b&gt;¹&lt;b&gt;ì ì&lt;/b&gt; PCìì &lt;b&gt;ë&lt;/b&gt;¹&lt;b&gt;ì&lt;/b&gt;¥ &lt;b&gt;ì&lt;/b&gt;§ìì¼í ê²&lt;b&gt;ë&lt;/b&gt;¤ 2í¸\",\n" +
            "        \"thumbnail\": \"https://search3.kakaocdn.net/argon/130x130_85_c/FScitl5jsEc\",\n" +
            "        \"cp\": \"728111\",\n" +
            "        \"height\": \"220\",\n" +
            "        \"link\": \"http://blog.naver.com/ittang7/220677696018\",\n" +
            "        \"width\": \"220\",\n" +
            "        \"image\": \"http://dthumb.phinf.naver.net/?src=%22http%3A%2F%2Fimage.fnnews.com%2Fresource%2Fmedia%2Fimage%2F2016%2F04%2F07%2F201604071700205185_m.png%22&type=f220\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"pubDate\": \"20141215234833\",\n" +
            "        \"title\": \"겨울철화재 나무 화목보일러 사용시 주의 사항\",\n" +
            "        \"thumbnail\": \"https://search2.kakaocdn.net/argon/130x130_85_c/II88rbaMEYF\",\n" +
            "        \"cp\": \"794570\",\n" +
            "        \"height\": \"401\",\n" +
            "        \"link\": \"http://sjy8593.tistory.com/1037\",\n" +
            "        \"width\": \"670\",\n" +
            "        \"image\": \"http://cfile22.uf.tistory.com/image/251DDC39548EE62B303156\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"pubDate\": \"20121212191420\",\n" +
            "        \"title\": \"3살 여자조카 선물 고르기\",\n" +
            "        \"thumbnail\": \"https://search2.kakaocdn.net/argon/130x130_85_c/CdjvLr8z8T5\",\n" +
            "        \"cp\": \"794570\",\n" +
            "        \"height\": \"706\",\n" +
            "        \"link\": \"http://happy7000.tistory.com/61\",\n" +
            "        \"width\": \"603\",\n" +
            "        \"image\": \"http://cfile8.uf.tistory.com/image/1671283A50C8576127D2BE\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"pubDate\": \"20160712152030\",\n" +
            "        \"title\": \"사드배치 반대하는 지지리도 못난 국민들 .\",\n" +
            "        \"thumbnail\": \"https://search2.kakaocdn.net/argon/130x130_85_c/3otg4KfUZU9\",\n" +
            "        \"cp\": \"269393\",\n" +
            "        \"height\": \"237\",\n" +
            "        \"link\": \"http://blog.daum.net/tndyd7627/8004672\",\n" +
            "        \"width\": \"420\",\n" +
            "        \"image\": \"http://cfile234.uf.daum.net/image/27033A3E57848C241F42B6\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"pubDate\": \"20150702060215\",\n" +
            "        \"title\": \"[복음에 대한 짧은 묵상] 20150702\",\n" +
            "        \"thumbnail\": \"https://search3.kakaocdn.net/argon/130x130_85_c/GWZUtv6TEUU\",\n" +
            "        \"cp\": \"269393\",\n" +
            "        \"height\": \"546\",\n" +
            "        \"link\": \"http://blog.daum.net/viababoo/1027\",\n" +
            "        \"width\": \"643\",\n" +
            "        \"image\": \"http://cfile221.uf.daum.net/image/2545044A559455452C426C\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"pubDate\": \"20080315195434\",\n" +
            "        \"title\": \"마츠야마켄이치\",\n" +
            "        \"thumbnail\": \"https://search1.kakaocdn.net/argon/130x130_85_c/HNwuvBPgnbQ\",\n" +
            "        \"cp\": \"269393\",\n" +
            "        \"height\": \"225\",\n" +
            "        \"link\": \"http://blog.daum.net/-_-aaas/9643007\",\n" +
            "        \"width\": \"300\",\n" +
            "        \"image\": \"http://cfs7.blog.daum.net/original/32/blog/2007/09/14/19/23/46ea611089a45&filename=ë§ˆì¸ ì•¼ë§ˆ%20ì¼„ì\u009D´ì¹˜3.jpg\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"pubDate\": \"20130726001339\",\n" +
            "        \"title\": \"2013 최신 미드-시카고 PD-시카고 파이어 스핀오프\",\n" +
            "        \"thumbnail\": \"https://search1.kakaocdn.net/argon/130x130_85_c/9uOfyPAUUIu\",\n" +
            "        \"cp\": \"794570\",\n" +
            "        \"height\": \"391\",\n" +
            "        \"link\": \"http://garuda.tistory.com/164\",\n" +
            "        \"width\": \"270\",\n" +
            "        \"image\": \"http://cfile9.uf.tistory.com/image/2719D73552D47831293887\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"pubDate\": \"20121212191420\",\n" +
            "        \"title\": \"3살 여자조카 선물 고르기\",\n" +
            "        \"thumbnail\": \"https://search1.kakaocdn.net/argon/130x130_85_c/CLMwlHBny2u\",\n" +
            "        \"cp\": \"794570\",\n" +
            "        \"height\": \"629\",\n" +
            "        \"link\": \"http://happy7000.tistory.com/61\",\n" +
            "        \"width\": \"628\",\n" +
            "        \"image\": \"http://cfile8.uf.tistory.com/image/1524CF3F50C857B002EF74\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"pubDate\": \"20150420164715\",\n" +
            "        \"title\": \"Green Smoke(그린 스모크) 전자담배 구매 대행 후기\",\n" +
            "        \"thumbnail\": \"https://search2.kakaocdn.net/argon/130x130_85_c/GbdiuHAhauh\",\n" +
            "        \"cp\": \"794570\",\n" +
            "        \"height\": \"391\",\n" +
            "        \"link\": \"http://southpawmusik.tistory.com/29\",\n" +
            "        \"width\": \"610\",\n" +
            "        \"image\": \"http://cfile26.uf.tistory.com/image/215929485534ACB436D29D\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"lastBuildDate\": \"Wed, 03 Aug 2016 10:08:18 +0900\",\n" +
            "    \"link\": \"http://dna.daum.net/apis\",\n" +
            "    \"generator\": \"Daum Open API\"\n" +
            "  }\n" +
            "}";


}
