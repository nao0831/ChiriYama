
package com.wadako.savemoney;

import static org.joox.JOOX.$;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import jp.sharakova.android.urlimageview.ImageCache;
import jp.sharakova.android.urlimageview.UrlImageView;

import org.joox.Match;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wadako.savemoney.amazon.AmazonApi;

public class ResultActivity extends Activity {

    private int price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);

        Intent intent = getIntent();
        price = intent.getIntExtra("price", 0);

        new SearchAmazonTask(0).execute(price);
        new SearchAmazonTask2(0).execute(price);
        new SearchAmazonTask(2).execute(price * 30);
        new SearchAmazonTask2(2).execute(price * 30);
        new SearchAmazonTask(4).execute(price * 365);
        new SearchAmazonTask2(4).execute(price * 365);

        initTextViews(price);
    }

    private void initTextViews(int price) {
        NumberFormat format = NumberFormat.getInstance();
        format.setGroupingUsed(true);
        String price1Str = format.format(price);
        TextView price1 = (TextView)findViewById(R.id.pricePerDayText);
        price1.setText("¥" + price1Str + "\n/day");
        String price2Str = format.format(price * 30);
        TextView price2 = (TextView)findViewById(R.id.pricePerMonthText);
        price2.setText("¥" + price2Str + "\n/month");
        String price3Str = format.format(price * 365);
        TextView price3 = (TextView)findViewById(R.id.pricePerYearText);
        price3.setText("¥" + price3Str + "\n/year");
    }

    private class SearchAmazonTask extends AsyncTask<Integer, Void, List<String>> {

        private int number;
        private List<String> detailPageUrls = new ArrayList<String>();

        public SearchAmazonTask(int number) {
            this.number = number;
        }

        @Override
        protected List<String> doInBackground(Integer... params) {

            AmazonApi api = new AmazonApi();
            try {
                Node root = api.searchItemsByPrice(params[0]);
                Match match = $(root);
                Match urls = match.find("Item>LargeImage>URL");
                List<String> urlList = new ArrayList<String>();
                for (int i = 0; i < urls.size(); i++) {
                    Element url = urls.get(i);
                    String urlStr = url.getChildNodes().item(0).getNodeValue();
                    urlList.add(urlStr);
                    Node item = url.getParentNode().getParentNode();
                    String detailPageUrl = item.getChildNodes().item(1).getChildNodes().item(0).getNodeValue();
                    detailPageUrls.add(detailPageUrl);
                }
                return urlList;
            } catch (Exception e) {
                Log.e(AppUtil.APP_NAME, "通信中にエラーが起きました", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<String> result) {
            super.onPostExecute(result);
            if (result == null) {
                Toast.makeText(ResultActivity.this, "通信中にエラーが起きました", Toast.LENGTH_LONG).show();
            } else {
                ViewGroup whole = (ViewGroup)findViewById(R.id.whole);
                final View child = whole.getChildAt(number);
                if (result.size() == 0) {
                    Log.d(AppUtil.APP_NAME, "画像なかったよ");
                    child.findViewById(R.id.progressBar1).setVisibility(View.GONE);
                } else {
                    Log.d(AppUtil.APP_NAME, "画像あったから取得するよ");
                    String url = result.get(0);
                    final UrlImageView imageView = (UrlImageView)child.findViewById(R.id.amazon);
                    imageView.setImageUrl(url, new UrlImageView.OnImageLoadListener() {
                        @Override
                        public void onStart(String url) {
                        }
                        
                        @Override
                        public void onComplete(String url) {
                            child.findViewById(R.id.progressBar1).setVisibility(View.GONE);
                            imageView.setOnClickListener(new ImageOnClickListener(detailPageUrls.get(0)));
                        }
                    });
                    Log.d(AppUtil.APP_NAME, url);
                }
            }
        }
    }
    
    private class SearchAmazonTask2 extends AsyncTask<Integer, Void, List<String>> {

        private int number;
        private List<String> detailPageUrls = new ArrayList<String>();

        public SearchAmazonTask2(int number) {
            this.number = number;
        }

        @Override
        protected List<String> doInBackground(Integer... params) {

            AmazonApi api = new AmazonApi();
            try {
                Node root = api.searchItemsByPrice(params[0]);
                Match match = $(root);
                Match urls = match.find("Item>LargeImage>URL");
                List<String> urlList = new ArrayList<String>();
                for (int i = 0; i < urls.size(); i++) {
                    Element url = urls.get(i);
                    String urlStr = url.getChildNodes().item(0).getNodeValue();
                    urlList.add(urlStr);
                    Node item = url.getParentNode().getParentNode();
                    String detailPageUrl = item.getChildNodes().item(1).getChildNodes().item(0).getNodeValue();
                    detailPageUrls.add(detailPageUrl);
                }
                return urlList;
            } catch (Exception e) {
                Log.e(AppUtil.APP_NAME, "通信中にエラーが起きました", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<String> result) {
            super.onPostExecute(result);
            if (result == null) {
                Toast.makeText(ResultActivity.this, "通信中にエラーが起きました", Toast.LENGTH_LONG).show();
            } else {
                ViewGroup whole = (ViewGroup)findViewById(R.id.whole);
                final View child = whole.getChildAt(number);
                if (result.size() == 0) {
                    Log.d(AppUtil.APP_NAME, "画像なかったよ");
                    child.findViewById(R.id.progressBar2).setVisibility(View.GONE);
                } else {
                    Log.d(AppUtil.APP_NAME, "画像あったから取得するよ");
                    String url = result.get(0);
                    final UrlImageView imageView = (UrlImageView)child.findViewById(R.id.amazon2);
                    imageView.setImageUrl(url, new UrlImageView.OnImageLoadListener() {
                        @Override
                        public void onStart(String url) {
                        }
                        
                        @Override
                        public void onComplete(String url) {
                            child.findViewById(R.id.progressBar2).setVisibility(View.GONE);
                            imageView.setOnClickListener(new ImageOnClickListener(detailPageUrls.get(0)));
                        }
                    });
                    Log.d(AppUtil.APP_NAME, url);
                }
            }
        }
    }
    
    private class ImageOnClickListener implements View.OnClickListener {
        private String url;
        public ImageOnClickListener(String url) {
            this.url = url;
        }
        
        @Override
        public void onClick(View v) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        ImageCache.deleteAll(getCacheDir());
        super.onDestroy();
    }

}
