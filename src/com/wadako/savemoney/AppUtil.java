
package com.wadako.savemoney;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * アプリ全体の便利なメソッドを集めたクラス
 * 
 * @author koichiro wada
 * @adm $Rev$ $Date$ $Author$
 */
public class AppUtil {

    /**
     * GolfApp
     */
    public static final String APP_NAME = "SaveMoney";

    /**
     * @param context
     * @return デバッグモードかどうか
     */
    public static boolean isDebug(Context context) {
        PackageManager pm = context.getPackageManager();
        ApplicationInfo ai = new ApplicationInfo();
        try {
            ai = pm.getApplicationInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            ai = null;
            return false;
        }
        if ((ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) == ApplicationInfo.FLAG_DEBUGGABLE) {
            return true;
        }
        return false;
    }

    /**
     * sharedPreferenceに書きこむ
     * 
     * @param ctx
     * @param key
     * @param value string
     */
    public static void setEnvValue(Context ctx, String key, String value) {
        SharedPreferences pref = ctx.getSharedPreferences(AppUtil.APP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * sharedPreferenceに書きこむ
     * 
     * @param ctx
     * @param key
     * @param value int
     */
    public static void setEnvValue(Context ctx, String key, int value) {
        SharedPreferences pref = ctx.getSharedPreferences(AppUtil.APP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * sharedPreferenceに書きこむ
     * 
     * @param ctx
     * @param key
     * @param value long
     */
    public static void setEnvValue(Context ctx, String key, long value) {
        SharedPreferences pref = ctx.getSharedPreferences(AppUtil.APP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * sharedPreferenceに書きこむ
     * 
     * @param ctx
     * @param key
     * @param value float
     */
    public static void setEnvValue(Context ctx, String key, float value) {
        SharedPreferences pref = ctx.getSharedPreferences(AppUtil.APP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(key, value);
        editor.commit();
    }
    
    /**
     * @param ctx
     * @param key
     * @return string
     */
    public static String getEnvValueStr(Context ctx, String key) {
        SharedPreferences pref = ctx.getSharedPreferences(AppUtil.APP_NAME, Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }

    /**
     * @param ctx
     * @param key
     * @return String
     */
    public static int getEnvValueInt(Context ctx, String key) {
        SharedPreferences pref = ctx.getSharedPreferences(AppUtil.APP_NAME, Context.MODE_PRIVATE);
        return pref.getInt(key, 0);
    }

    /**
     * @param ctx
     * @param key
     * @return long
     */
    public static long getEnvValueLong(Context ctx, String key) {
        SharedPreferences pref = ctx.getSharedPreferences(AppUtil.APP_NAME, Context.MODE_PRIVATE);
        return pref.getLong(key, 0);
    }
    
    /**
     * @param ctx
     * @param key
     * @return float
     */
    public static float getEnvValueFloat(Context ctx, String key) {
        SharedPreferences pref = ctx.getSharedPreferences(AppUtil.APP_NAME, Context.MODE_PRIVATE);
        return pref.getFloat(key, 0);
    }
    
    public static void removeEnvValue(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(AppUtil.APP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();
    }
    
    public static void removeEnvValueAll(Context context) {
        SharedPreferences pref = context.getSharedPreferences(AppUtil.APP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear().commit();
    }
}
