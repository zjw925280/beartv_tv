package com.android.liba.exception;


import com.android.liba.R;
import com.android.liba.context.AppContext;


public class NetworkError extends Error {

    public NetworkError() {
        super( AppContext.getInstance().getString(R.string.no_network));
    }

}
