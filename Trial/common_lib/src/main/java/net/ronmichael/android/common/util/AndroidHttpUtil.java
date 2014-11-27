/*
 * Copyright (C) 2014
 *
 */

/* REVISION HISTORY
 *
 * DATE         NAME                      REMARKS
 * 2014/10/08   Ron Michael Khu           Created
 */

package net.ronmichael.android.common.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import net.ronmichael.common.util.HttpUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 * Intended to contain assorted http-related utility methods meant to be used for Android projects.
 * <p/>
 * Note: methods here are expected to have no side effects.
 */
public class AndroidHttpUtil extends HttpUtil {

    /**
     * Performs an HTTP GET access of the specified URL and return the result contents as text.
     *
     * @param url URL to be accessed via HTTP GET
     *
     * @return Bitmap on success; otherwise, the Throwable instance encountered during the operation
     */
    public static Object httpGetImage(String url) {
        Object result = httpGetStream(url);
        if (result instanceof Throwable) {
            return (Throwable) result;
        }

        InputStream content = (InputStream)result;
        Bitmap bitMap = BitmapFactory.decodeStream(content);

        try {
            content.close();
        } catch (IOException e) {
        }

        return bitMap;
    }

}
