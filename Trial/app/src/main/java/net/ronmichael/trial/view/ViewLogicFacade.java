/*
 * Copyright (C) 2014
 *
 */

/* REVISION HISTORY
 *
 * DATE         NAME                      REMARKS
 * 2014/11/27   Ron Michael Khu           Created
 */

package net.ronmichael.trial.view;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.ronmichael.trial.R;
import net.ronmichael.trial.controller.AppController;
import net.ronmichael.trial.model.Message;
import net.ronmichael.trial.util.AssortedUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

/**
 *
 */
public class ViewLogicFacade {

    private static ViewLogicFacade sInstance = null;


    public boolean needForRefresh = false;

    public static synchronized ViewLogicFacade getInstance() {
        if (sInstance == null) {
            sInstance = new ViewLogicFacade();

        }

        return sInstance;
    }

    /**
     * When needed, prompts the user to make certain prerequisite operations. The main activity
     *
     * @param context calling context
     */
    public synchronized void performPreparations(Context context) {

    }

    /**
     * Triggers the download of catalog information from the server, and the processing of
     * it.
     *
     * @param context calling context
     */
    public synchronized boolean triggerProcessingOfMessagesFromServer(Context context) {
        if (isRequestMessageOngoing()) {
            //return false;
        }

        if (! AssortedUtil.isNetworkAvailable(context)) {
            Resources res =  context.getResources();

            AssortedUtil.showMessageDialog(context,
                    res.getString(R.string.notification_network_none_title),
                    res.getString(R.string.notification_network_none_message), null);
            //Toast.makeText(context, res.getString(R.string.notification_network_none_message),
            //        Toast.LENGTH_LONG).show();
            return false;
        }

        AppController.getInstance().requestMessages();
        return true;
    }

    public synchronized boolean triggerSavingOfMessageToServer(Context context, Message message) {

        Log.d("TRACE", ">>>>>>>>>>>>>>>>triggerSavingOfMessageToServer");

        if (! AssortedUtil.isNetworkAvailable(context)) {
            Resources res =  context.getResources();

            AssortedUtil.showMessageDialog(context,
                    res.getString(R.string.notification_network_none_title),
                    res.getString(R.string.notification_network_none_message), null);
            //Toast.makeText(context, res.getString(R.string.notification_network_none_message),
            //        Toast.LENGTH_LONG).show();
            return false;
        }

        Log.d("TRACE", ">>>>>>>>>>>>>>>>triggerSavingOfMessageToServer");
        AppController.getInstance().saveMessage(message);
        return true;
    }

    public synchronized boolean isRequestMessageOngoing() {
        return AppController.getInstance().isRequestMessagesOngoing();
    }

    /**
     * Retrieve the messages
     *
     * @return list of messages
     */
    public List<Message> getMessages() {
        List<Message> messages = AppController.getInstance().getMessages();

        return messages;
    }


    /**
     * Prompts the user to confirm the application-exit operation.
     *
     * @param mainActivity main activity of the application
     */
    public synchronized void promptApplicationExit(final Activity mainActivity) {
        //Note: consider usiing a single instance of the listener.

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            protected Activity mActivity;

            public DialogInterface.OnClickListener init(Activity activity){
                mActivity = activity;
                return this;
            }

            public void onClick(DialogInterface dialog, int id) {
                mActivity.finish();
            }
        }.init(mainActivity);

        AssortedUtil.showConfirmationDialog(mainActivity, R.string.confirmation_exit_app_message,
                listener);
    }

    /**
     * Exit or terminate the application
     */
    public synchronized void exitApplication() {
        // Note: perform cleanup

        AppController.getInstance().terminateApplication();
    }

    /**
     *  Base activity that all activities of the application should subclass
     */
    public static class BaseActivity  extends FragmentActivity {
        @Override
        protected void onStop() {
            super.onStop();
            AppController.getInstance().onActivityStopped();
        }

        @Override
        protected void onStart() {
            super.onStart();
            AppController.getInstance().onActivityStopped();
        }

        @Override
        protected synchronized void onActivityResult(int requestCode, int resultCode, Intent data) {
            Log.d("TRACE", "BaseActivity onActivityResult()");

            ViewLogicFacade.getInstance().needForRefresh = true;
            AppController.getInstance().onHandleActivityResult(requestCode, resultCode, data);
        }
    }

}
