package com.nocson.eDepot;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

public class EzWebViewInterface {

    private WebView appView;
    private Activity context;


    public EzWebViewInterface(Activity activity, WebView view) {
        appView = view;
        context = activity;
    }
     public void callAndroid(final String arg){
         if(arg.equals("true")){
             context.finish();
         }

     }
    @JavascriptInterface
    public void toastLong (String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    @JavascriptInterface
    public void toastShort (String message) { // Show toast for a short time
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
