package com.wadako.savemoney;
import android.content.Context;

public class ResourcesUtils {

    public static int getResourceIdByResourceName(Context context, String resourceName) {
        String name = "drawable/" + resourceName + "_144";
        int imageResource = context.getResources().getIdentifier(name, null, context.getPackageName());
        return imageResource;
    }
}
