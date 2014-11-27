/*
 * Copyright (C) 2014
 *
 */

/* REVISION HISTORY
 *
 * DATE         NAME                      REMARKS
 * 2014/10/09   Ron Michael Khu           Created
 */

package net.ronmichael.trial.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class BroadcastUtil {

    /**
     * Broadcasts internal event. Active listeners (or receivers) will be able to receive
     * the event.
     *
     * @param context calling or active context
     * @param intent intent containing the event name as intent action
     * @param params parameters or extras to be included in the event to be broadcasted
     */
    public static void broadcastInternalEvent(Context context, final String name, Bundle params) {
        Intent intent = new Intent(name);
        if (null != params) {
            intent.putExtras(params);
        }

        broadcastInternalEvent(context, intent);
    }

    /**
     * Broadcasts internal event. Active listeners (or receivers) will be able to receive
     * the event.
     *
     * @param context calling or active context
     * @param intent intent containing the event name as intent action
     */
    public static void broadcastInternalEvent(Context context, final String name) {
        broadcastInternalEvent(context, name, null);
    }

    /**
     * Broadcasts internal event. Active listeners (or receivers) will be able to receive
     * the event.
     *
     * @param context calling or active context
     * @param intent intent containing the event name as intent action
     */
    public static void broadcastInternalEvent(Context context, Intent intent) {
        Log.d("TRACE", ">>>>>> broadcasting internal event: " + intent.getAction());

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * Unregisters the specified receiver instance.
     *
     * @param context calling or active context
     * @param receiver receiver instance to unregister
     */
    public static void unregisterForInternalEvent(Context context, BroadcastReceiver receiver) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }

    /**
     * Registers a receiver for an internal event
     *
     * @param context calling or active context
     * @param receiver receiver to be registered to an internal event
     * @param filter intent filter
     */
    public static void registerForInternalEvent(Context context, BroadcastReceiver receiver,
                                         IntentFilter filter) {

        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filter);
    }

    /**
     * Registers a receiver for an internal event
     *
     * @param context calling or active context
     * @param receiver receiver to be registered to an internal event
     * @param name name of the event to listen for
     */
    public static void registerForInternalEvent(Context context, BroadcastReceiver receiver,
                                         final String name) {

        IntentFilter intentFilter = new IntentFilter(name);

        registerForInternalEvent(context, receiver, intentFilter);
    }

    /**
     * Registers a receiver for an internal event.
     *
     * @param context calling or active context
     * @param receiver receiver to be registered to an internal event
     * @param names names of the events to listen for
     */
    public static void registerForInternalEvent(Context context, BroadcastReceiver receiver,
                                         final String names[]) {

        IntentFilter intentFilter = new IntentFilter();
        for (String name: names) {
            intentFilter.addAction(name);
        }

        registerForInternalEvent(context, receiver, intentFilter);
    }
}
