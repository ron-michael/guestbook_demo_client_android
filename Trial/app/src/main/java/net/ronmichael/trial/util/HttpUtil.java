/*
 * Copyright (C) 2014
 *
 */

/* REVISION HISTORY
 *
 * DATE         NAME                      REMARKS
 * 2014/10/08   Ron Michael Khu           Created
 */

package net.ronmichael.trial.util;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Intended to contain assorted http-related utility methods.
 * <p/>
 * Note: methods here are expected to have no side effects.
 */
public class HttpUtil {

    public static final Exception NON_200_STATUS_EXCEPTION = new Exception("non 200");
    public static final Exception READ_CONTENTS_EXCEPTION = new Exception("read stream exception");

    /**
     * Performs an HTTP GET access of the specified URL and consume all contents into the
     * specified StringBuilder object.
     *
     * @param url URL to be accessed via HTTP GET
     * @param output storage object for the text contents
     *
     * @return null on success; otherwise, the Throwable instance encountered during the operation
     */
    public static Throwable httpGetText(String url, StringBuilder output) {
        Object result = httpGetStream(url);
        if (result instanceof Throwable) {
            return (Throwable) result;
        }

        InputStream content = (InputStream)result;
        BufferedReader reader = new BufferedReader(new InputStreamReader(content));

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
        } catch (IOException e) {
            return READ_CONTENTS_EXCEPTION;
        }

        try {
            content.close();
        } catch (IOException e) {
        }

        return null;
    }

    /**
     * Performs an HTTP GET access of the specified URL and return the result contents as text.
     *
     * @param url URL to be accessed via HTTP GET
     *
     * @return String contents on success; otherwise, the Throwable instance encountered during the operation
     */
    public static Object httpGetText(String url) {
        StringBuilder output = new StringBuilder();
        Object result = httpGetText(url, output);

        if (result instanceof Throwable) {
            return result;
        }

        return output.toString();
    }

    /**
     * Performs an HTTP GET access of the specified URL and returns the InputStream containing the
     * result contents.
     *
     * @param url URL to be accessed via HTTP GET
     *
     * @return InputStream on success; otherwise, the Throwable instance encountered during the operation
     */
    public static Object httpGetStream(String url) {
        Log.d("TRACE", "access URL: " + url);
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);

        //-- Access URL --//
        HttpResponse response;
        try {
            response = client.execute(httpGet);
        } catch (ClientProtocolException e) {
            return e;
        } catch (IOException e) {
            return e;
        }

        //-- Get and check HTTP status --//
        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();

        if (statusCode != 200) {
            return NON_200_STATUS_EXCEPTION;
        }

        //-- Read contents and populate output object ---//
        HttpEntity entity = response.getEntity();
        InputStream content = null;
        try {
            content = entity.getContent();
        } catch (IOException e) {
            return READ_CONTENTS_EXCEPTION;
        }

        return content;
    }
}
