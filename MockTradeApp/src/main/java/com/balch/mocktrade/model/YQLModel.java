/*
 * Author: Balch
 * Created: 9/4/14 12:26 AM
 *
 * This file is part of MockTrade.
 *
 * MockTrade is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MockTrade is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MockTrade.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2014
 */

package com.balch.mocktrade.model;


import android.content.Context;

import com.android.volley.RequestQueue;
import com.balch.android.app.framework.model.ModelInitializer;
import com.balch.mocktrade.settings.Settings;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public abstract class YQLModel implements ModelInitializer {
    protected final static String YQL_BASE_URL = "https://query.yahooapis.com/v1/public/yql";
    protected final static String YQL_ENV = "store://datatables.org/alltableswithkeys";

    protected final static String GOOGLE_BASE_URL = "http://finance.google.com/finance/info?client=ig&q=";

    protected ModelProvider modelProvider;

    public YQLModel() {
    }

    protected YQLModel(ModelProvider modelProvider) {
        initialize(modelProvider);
    }

    public void initialize(ModelProvider modelProvider) {
        this.modelProvider = modelProvider;
    }

    protected String getYQLQueryUrl(String query) throws UnsupportedEncodingException {
        return String.format("%s?q=%s&env=%s&format=json", YQL_BASE_URL,
                URLEncoder.encode(query, "UTF-8"),
                URLEncoder.encode(YQL_ENV, "UTF-8"));
    }

    protected String getGoogleQueryUrl(String symbols) throws UnsupportedEncodingException  {
        return GOOGLE_BASE_URL +  URLEncoder.encode(symbols, "UTF-8");
    }

    public RequestQueue getRequestQueue() {
        return modelProvider.getRequestQueue();
    }

    public Context getContext() {
        return modelProvider.getContext();
    }

    public Settings geSettings() {
        return modelProvider.getSettings();
    }

}
