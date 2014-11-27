/*
 * Copyright (C) 2014
 *
 */

/* REVISION HISTORY
 *
 * DATE         NAME                      REMARKS
 * 2014/11/11                             From:
 */
package net.ronmichael.android.common.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;

/**
 * Created by rmichael on 11/11/14.
 */
public class ReFragmentTabHost extends FragmentTabHost {
    public ReFragmentTabHost(Context context) {
        super(context);
    }

    @Override
    public void addTab(TabSpec tabSpec, Class<?> clss, Bundle args) {
        super.addTab(tabSpec, clss, args);
    }
}
