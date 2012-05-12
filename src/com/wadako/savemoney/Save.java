package com.wadako.savemoney;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

public class Save {
    
    public static final String PICTURE_RES_ID_KEY = "picruteResIdKey";
    public static final String PRICE_KEY = "priceKey";
    public static final int MAX_SAVE_COUNT = 100;
    
    private int pictureResId;
    private int price;
    private int number;
    
    public boolean save(Context context) {
        if (pictureResId == 0 || price == 0) {
            return false;
        } else {
            for (int i = 0; i < MAX_SAVE_COUNT; i++) {
                if (Save.getSaveByNumber(context, i) == null) {
                    AppUtil.setEnvValue(context, getPictureResIdKeyByNumber(i), pictureResId);
                    AppUtil.setEnvValue(context, getPriceKeyByNumber(i), price);
                    number = i;
                    Log.d(AppUtil.APP_NAME, "登録したよ");
                    return true;
                }
            }
            Log.e(AppUtil.APP_NAME, "登録数がいっぱいだよ");
            return false;
        }
    }
    
    public static Save getSaveByNumber(Context context, int number) {
        Save save = new Save();
        save.pictureResId = AppUtil.getEnvValueInt(context, getPictureResIdKeyByNumber(number));
        save.price = AppUtil.getEnvValueInt(context, getPriceKeyByNumber(number));
        save.number = number;
        if (save.pictureResId == 0 || save.price == 0) {
            return null;
        } else {
            return save;
        }
    }
    
    public static List<Save> getSaves(Context context) {
        List<Save> saves = new ArrayList<Save>();
        for (int i = 0; i < MAX_SAVE_COUNT; i++) {
            Save save = getSaveByNumber(context, i);
            if (save != null) {
                saves.add(save);
            } else {
                return saves;
            }
        }
        return saves;
    }
    
    public static int getSumOfPrice(Context context) {
        int sum = 0;
        List<Save> saves = getSaves(context);
        for (Save save : saves) {
            sum += save.price;
        }
        return sum;
    }
    
    public void delete(Context context) {
        Log.d("", "number : " + number);
        AppUtil.removeEnvValue(context, getPictureResIdKeyByNumber(number));
        AppUtil.removeEnvValue(context, getPriceKeyByNumber(number));
        for (int i = number + 1; i < MAX_SAVE_COUNT; i++) {
            Save save = getSaveByNumber(context, i);
            if (save == null) {
                return;
            } else {
                save.save(context);
                AppUtil.removeEnvValue(context, getPictureResIdKeyByNumber(i));
                AppUtil.removeEnvValue(context, getPriceKeyByNumber(i));
            }
        }
    }
    
    public static void deleteAll(Context context) {
        AppUtil.removeEnvValueAll(context);
    }
    
    public static String getPictureResIdKeyByNumber(int number) {
        return PICTURE_RES_ID_KEY + "_" + number;
    }
    
    public static String getPriceKeyByNumber(int number) {
        return PRICE_KEY + "_" + number;
    }
    
    /**
     * @return the pictureResId
     */
    public int getPictureResId() {
        return pictureResId;
    }
    /**
     * @param pictureResId the pictureResId to set
     */
    public void setPictureResId(int pictureResId) {
        this.pictureResId = pictureResId;
    }
    /**
     * @return the price
     */
    public int getPrice() {
        return price;
    }
    /**
     * @param price the yen to set
     */
    public void setPrice(int price) {
        this.price = price;
    }
    
    
}
