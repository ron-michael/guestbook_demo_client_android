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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import net.ronmichael.trial.R;
import net.ronmichael.trial.model.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for rendering Message data into a message item cell in a message listing, regardless
 * whether it's a simple row-based listing or a grid listing.
 */
public class MessageItemAdapter extends ArrayAdapter<Message> {

    private Activity mContext;
    private List<Message> mList;

    public MessageItemAdapter(Activity context, int textId, List<Message> list) {
        super(context, textId, list);

        mContext = context;
        if (list == null) {
            list = new ArrayList<Message>();
        }

        mList = list;
    }

    /**
     * The view holder intended to serve as container for the components and other view objects
     * that are necessary to render a message item.
     * <p/>
     * The properties or fields in this class will be assigned to their corresponding counterparts
     * in the XML layout. It is expected that the layout loaded will have a structure that
     * correspond to each of the properties defined here.
     */
    private static class ViewHolder {
        TextView name;
        TextView message;
        ImageView thumbnail;
    }

    @Override
    public boolean isEnabled(int position) {

        return true;
    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    @Override
    public Message getItem(int index) {
        if (mList == null) {
            return null;
        }

        return mList.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int pos, View view, ViewGroup arg2) {

        ViewHolder holder;

        Message item = (Message) getItem(pos);
        Log.d("TRACE", "item: " + item.name);

        //-- Initialize view holder instance --//
        if (view == null) {
            view = ((Activity) mContext).getLayoutInflater().inflate(R.layout.listcell_message, null);

            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.message = (TextView) view.findViewById(R.id.message_preview);
            holder.thumbnail = (ImageView) view.findViewById(R.id.photo_thumbnail);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }


        //-- Start value and property assignment of components --/

        TextUtils.TruncateAt truncate = TextUtils.TruncateAt.END;

        holder.message.setEllipsize(truncate);
        holder.message.setText(item.message);

        holder.name.setText(item.name);
        holder.name.setText(item.name);

        return view;
    }

}
