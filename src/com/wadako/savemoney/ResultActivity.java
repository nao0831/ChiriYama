
package com.wadako.savemoney;

import static org.joox.JOOX.$;

import java.util.ArrayList;
import java.util.List;

import jp.sharakova.android.urlimageview.ImageCache;
import jp.sharakova.android.urlimageview.UrlImageView;

import org.joox.Match;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import android.app.Activity;
import android.content.Intent;
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
        new SearchAmazonTask(2).execute(price * 30);
        new SearchAmazonTask(4).execute(price * 365);

        initTextViews(price);
    }

    private void initTextViews(int price) {
        TextView price1 = (TextView)findViewById(R.id.pricePerDayText);
        price1.setText((price) + "円");
        TextView price2 = (TextView)findViewById(R.id.pricePerMonthText);
        price2.setText((price * 30) + "円");
        TextView price3 = (TextView)findViewById(R.id.pricePerYearText);
        price3.setText((price * 365) + "円");
    }

    private class SearchAmazonTask extends AsyncTask<Integer, Void, List<String>> {

        private int number;

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
//            if (number == 0) {
//                new SearchAmazonTask(2).execute(price * 30);
//            } else if (number == 2) {
//                new SearchAmazonTask(4).execute(price * 365);
//            }

            if (result == null) {
                Toast.makeText(ResultActivity.this, "通信中にエラーが起きました", Toast.LENGTH_LONG).show();
            } else {
                if (result.size() == 0) {
                    Log.d(AppUtil.APP_NAME, "画像なかったよ");
                } else {
                    Log.d(AppUtil.APP_NAME, "画像あったから取得するよ");
                    String url = result.get(0);
                    ViewGroup whole = (ViewGroup)findViewById(R.id.whole);
                    final View child = whole.getChildAt(number);
                    UrlImageView imageView = (UrlImageView)child.findViewById(R.id.amazon);
                    imageView.setImageUrl(url, new UrlImageView.OnImageLoadListener() {
                        @Override
                        public void onStart(String url) {
                        }
                        
                        @Override
                        public void onComplete(String url) {
                            child.findViewById(R.id.progressBar1).setVisibility(View.GONE);
                        }
                    });
                    Log.d(AppUtil.APP_NAME, url);
                    if (result.size() > 1) {
                        Log.d(AppUtil.APP_NAME, "もう一枚画像を取得するよ");
                        UrlImageView imageView2 = (UrlImageView)child.findViewById(R.id.amazon2);
                        imageView2.setImageUrl(result.get(1),
                                new UrlImageView.OnImageLoadListener() {
                                    @Override
                                    public void onStart(String url) {
                                    }

                                    @Override
                                    public void onComplete(String url) {
                                        child.findViewById(R.id.progressBar2).setVisibility(View.GONE);
                                    }
                                });
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        ImageCache.deleteAll(getCacheDir());
        super.onDestroy();
    }

}
