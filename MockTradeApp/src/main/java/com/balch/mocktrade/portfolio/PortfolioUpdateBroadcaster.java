package com.balch.mocktrade.portfolio;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

public class PortfolioUpdateBroadcaster  {
    private static final String TAG = PortfolioUpdateBroadcaster.class.getSimpleName();

    public static final String ACTION = PortfolioUpdateBroadcaster.class.getName();


    static public void broadcast(Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ACTION));
    }

}