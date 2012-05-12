
package com.wadako.savemoney.amazon;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import android.util.Log;

import com.wadako.savemoney.AppUtil;

public class AmazonApi {

    public static final String ENDPOINT = "ecs.amazonaws.jp";

    public static final String AWS_ACCESS_KEY_ID = "AKIAI5FLLB7K7T5MKMEQ";

    public static final String AWS_SECRET_KEY = "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";

    private static final int TIME_OUT = 30000;
    
    public static final List<String> categories = new ArrayList<String>();

    private SignedRequestsHelper helper;

    private DefaultHttpClient client;

    public AmazonApi() {
        try {
            helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
            client = new DefaultHttpClient();
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, TIME_OUT); // 接続のタイムアウト
            HttpConnectionParams.setSoTimeout(params, TIME_OUT); // データ取得のタイムアウト
            client.setParams(params);
//            categories.add("Beauty");
            categories.add("Electronics");
            categories.add("Grocery");
            categories.add("Hobbies");
            categories.add("Kitchen");
            categories.add("Software");
            categories.add("Toys");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public Node searchItemsByPrice(int price) throws ClientProtocolException, IOException, SAXException, ParserConfigurationException, FactoryConfigurationError {
        int maxPrice = price * 12 / 10;
        int minPrice = price * 8 / 10;
        
        String category = getCategoryByRandom();
        Log.d(AppUtil.APP_NAME, "category : " + category);

        Map<String, String> params = new HashMap<String, String>();
        params.put("Service", "AWSECommerceService");
        params.put("Version", "2011-08-01");
        params.put("Operation", "ItemSearch");
        params.put("ResponseGroup", "OfferFull,Images,ItemAttributes");
        params.put("MinimumPrice", String.valueOf(minPrice));
        params.put("MaximumPrice", String.valueOf(maxPrice));
        params.put("AssociateTag", "koichiro wada");
        params.put("Keywords", getRandomKeywords(10));
        params.put("SearchIndex", category);
        params.put("Sort", "salesrank");
        String url = helper.sign(params);
        Log.d(AppUtil.APP_NAME, "接続します url : " + url);

        HttpGet method = new HttpGet(url);
        method.setHeader("Connection", "Keep-Alive");
        HttpResponse response = client.execute(method);
        String xml = EntityUtils.toString(response.getEntity(), "UTF-8");
        Log.d(AppUtil.APP_NAME, "response body : " + xml);
        Node doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        return doc;
    }
    
    private String getCategoryByRandom() {
        Random random = new Random(System.currentTimeMillis());
        String category = categories.get(random.nextInt(categories.size()));
        return category;
    }
    
    private String getRandomKeywords(int number) {
        String hiragana = "あ,い,う,え,お,か,き,く,け,こ,さ,し,す,せ,そ,た,ち,つ,て,と,な,に,ぬ,ね,の,は,ひ,ふ,へ,ほ,ま,み,む,め,も,や,ゆ,よ,わ,を,ん";
        String[] hiraganas = StringUtils.splitByWholeSeparator(hiragana, ",");
        Random random = new Random(System.currentTimeMillis());
        String[] keywords = new String[number];
        for (int i = 0; i < number; i++) {
            int r = random.nextInt(hiraganas.length);
            keywords[i] = hiraganas[r];
        }
        return StringUtils.join(keywords, '|');
    }

}
