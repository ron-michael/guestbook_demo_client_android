/*
 * Copyright (C) 2014
 *
 */

/* REVISION HISTORY
 *
 * DATE         NAME                      REMARKS
 * 2014/11/27   Ron Michael Khu           Created
 */

package net.ronmichael.trial.controller;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.ronmichael.trial.model.Message;
import net.ronmichael.trial.util.BroadcastUtil;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A subclass of the Android Application class.
 * </p>
 * For this applicaiton, this class is meant to serve as the Main Controller and entry point of
 * all Controller Layer operation calls from the View Layer and Model Layer.
 * <p/>
 * This class is intended to serve as a facade to the other parts of the application. View classes
 * are not mean to call directly other classes belonging to the Controller Layer nor are they
 * allowed to access manager classes of the Model Layer.
 */
public class AppController extends Application {

    public static final String EVENT_MESSAGES_FETCHED           = "EVENT_MESSAGES_FETCHED";

    private static final String DEFAULT_HOST    = "192.168.1.168";
    private static final String TAG = AppController.class.getSimpleName();
    private static final String URL = "/guests/";

    private static AppController sInstance = null;
    private int mActiveActivites = 0;
    private boolean mDownloadManagerDown = false;
    private RequestQueue mRequestQueue = null;

    private List<Message> mMessages = new ArrayList<Message>();

    public static AppController getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (sInstance == null) {
            sInstance = this;

        }
    }

    /**
     * Handles cleanup for the application. This method is invoked upon termination.
     */
    @Override
    public void onTerminate() {


        super.onTerminate();
    }

    public synchronized void onActivityStarted() {
        if (mActiveActivites == 0) {

        }
        mActiveActivites++;
    }

    public synchronized void onActivityStopped() {
        mActiveActivites--;
        if (mActiveActivites == 0) {

        }
    }

       /**
     * Terminates the application
     */
    public synchronized void terminateApplication() {
        System.runFinalizersOnExit(true) ;
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? "" : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /**
     * Fetched the message data from the server.
     */
    public synchronized boolean requestMessages() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String url = "http://" + preferences.getString("setting_server_url", DEFAULT_HOST) + URL;
        Log.d("TRACE", ">>>>>>>>>>> " + url);

        JsonObjectRequest request =
                new JsonObjectRequest(Request.Method.GET,
                    url, null,
                    (new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            if (response == null) {
                                return;
                            }

                            Log.d(TAG, response.toString());

                            mMessages.clear();

                            JSONObject json = null;
                            try {
                                json = new JSONObject(response.toString().trim());
                            } catch (JSONException e) {
                                e.printStackTrace();
                                return;
                            }

                            Iterator<String> iter = json.keys();
                            List<Message> list = new ArrayList<Message>();
                            while (iter.hasNext()) {
                                String key = iter.next();

                                Message message = new Message();
                                JSONObject contents = json.optJSONObject(key);

                                if (contents != null) {
                                    message.assign(contents);
                                    list.add(message);
                                }

                            }

                            mMessages.addAll(list);
                            BroadcastUtil.broadcastInternalEvent(getApplicationContext(), EVENT_MESSAGES_FETCHED);
                        }
                    }),

                    (new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    })
                );

        // Adding request to request queue
        addToRequestQueue(request, "tag");

        return false;
    }

    /**
     * Saves message data unto the server.
     */
    public synchronized boolean saveMessage(Message message) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String url = "http://" + preferences.getString("setting_server_url", DEFAULT_HOST) + URL;

        Log.d("TRACE", ">>>>>>>>>>> " + url);

        JsonObjectRequest request =
                (new JsonObjectRequest(Request.Method.POST,
                        url, null,
                        (new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                if (response == null) {
                                    return;
                                }

                                Log.d("TRACE", ">>>>>>>>>>>>>>>>>>> " + response.toString());
                                requestMessages();
                            }
                        }),

                        (new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("TRACE", ">>>>>>>>>>>>>>>>>>> " + error.getMessage());
                            }
                        })
                ) {

                    Message mMessage;

                    public JsonObjectRequest init(Message message) {
                        mMessage = message;
                        return this;
                    }

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        Log.d("TRACE", ">>>>>>>>>>>>>> " + mMessage.toJson().toString());
                        params.put("guest", mMessage.toJson().toString());

                        return  params;
                    }

                }).init(message);

        // Adding request to request queue
        addToRequestQueue(request, "tag");

        return false;
    }

    /**
     * Fetch the stored catalog information
     *
     * @return root catalog item
     */
    public synchronized List<Message> getMessages() {
        return mMessages;
    }

    /**
     * Checks if a request-messages operation is ongoing or not.
     *
     * @return true, when a request-messages operation is ongoing; otherwise, false
     */
    public synchronized boolean isRequestMessagesOngoing() {
        return false;
    }

    /**
     * Process the parameters received by an Activity's onActivityResult(). This method needs to be
     * called to complete the purchase-flow that has been triggered by calling purchaseItem().
     *
     * @param requestCode request code of an operation
     * @param resultCode result code of the operation
     * @param data operation result
     * <p/>
     */
    public synchronized void onHandleActivityResult(int requestCode, int resultCode, Intent data) {
    }

}
