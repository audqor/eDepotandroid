package com.nocson.eDepot;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class webInterface {
    Context mContext;

    /** Instantiate the interface and set the context */
    webInterface(Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }
}
