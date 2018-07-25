package com.nocson.eDepot;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.URL;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private WebView mWebView;
    private String ShowMessage;

    private ValueCallback<Uri> filePathCallbackNormal;
    private ValueCallback<Uri[]> filePathCallbackLollipop;
    private Uri mCapturedImageURI;
    private WebViewInterface mWebViewInterface;

    private ShareActionProvider mShareActionProvider;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //newProduct(null);
                    return true;
                case R.id.navigation_dashboard:

                    return true;
                case R.id.navigation_notifications:

                    return true;
            }
            return false;
        }
    };

    private void GetRegID() {
        String id = FirebaseInstanceId.getInstance().getToken();
    }


    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
        }
    }

    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        uploadImage(imageUri);
//        Cursor cursor = MainActivity.this.getContentResolver().query(imageUri,null, null, null, null);
//        cursor.moveToFirst();
//        long size = cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE));
//        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
//        String name = cursor.getString(nameIndex);
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        String fPath =  cursor.getString(column_index);
//        cursor.close();
//        final   HttpFileUpload  ddd = new HttpFileUpload();
//        ddd.Path = fPath;
//        ddd.FileName = name;
//        ddd.Size = size;
//        new Thread() {
//            public void run() {
//                ddd.run();
//            }
//        }.start();
//        if (imageUri != null) {
//            new Thread() {
//                public void run() {
//                    Cursor cursor = MainActivity.this.getContentResolver().query(imageUri,
//                            null, null, null, null);
//                    cursor.moveToFirst();
//                    long size = cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE));
//                    cursor.close();
//
//                    ArrayList<WebViewInterfaceParam> beginparam = new ArrayList<WebViewInterfaceParam>();
//                    beginparam.add(new WebViewInterfaceParam("fileName",imageUri.toString()));
//                    beginparam.add(new WebViewInterfaceParam("totalSize",Long.toString(size)  ));
//                      String fileGuid = invokeHelloWorldWS(beginparam, "BeginUploadMethod");
//                   if(fileGuid != null)
//                   {
//                       ArrayList<WebViewInterfaceParam> uploadparam = new ArrayList<WebViewInterfaceParam>();
//                       uploadparam.add(new WebViewInterfaceParam("staticName",fileGuid));
//                       uploadparam.add(new WebViewInterfaceParam("dataBuffer",Long.toString(f.length())));
//
//                       Log.d("1231111111", Long.toString(f.length()));
//
//                   }
//                }
//        }.start();
//        }
    }

    void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            for (Integer i = 0; i < imageUris.size(); i++) {
                uploadImage(imageUris.get(i));
            }
        }
    }

    void uploadImage(Uri imageUri)
    {
        final Uri iUri = imageUri;
        Cursor cursor = MainActivity.this.getContentResolver().query(iUri,null, null, null, null);
        cursor.moveToFirst();
        long size = cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE));
        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        String name = cursor.getString(nameIndex);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        String fPath =  cursor.getString(column_index);
        cursor.close();
        final   HttpFileUpload  ddd = new HttpFileUpload();
        ddd.Path = fPath;
        ddd.FileName = name;
        ddd.Size = size;
        new Thread() {
            public void run() {
                ddd.run();
            }
        }.start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        mWebView = (WebView) findViewById(R.id.activity_main_webview);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        mWebViewInterface = new WebViewInterface(MainActivity.this, mWebView); //JavascriptInterface 객체화
        mWebView.addJavascriptInterface(mWebViewInterface, "Android"); //웹뷰에 Jav
        mWebView.getSettings().setDomStorageEnabled(true);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else
//                if (type.startsWith("image/"))
            {
                handleSendImage(intent); // Handle single image being sent
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendMultipleImages(intent); // Handle multiple images being sent
            }
        } else {
            // Handle other intents, such as being started from the home screen
        }
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
            public void onPermissionRequest(final PermissionRequest request) {
                request.grant(request.getResources());
            }
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                    openFileChooser(uploadMsg, "");
                }

                public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                    filePathCallbackNormal = uploadMsg;
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);

                    i.setType("image/*");
                    startActivityForResult(Intent.createChooser(i, "File Chooser"), 1);
                }
                public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                    openFileChooser(uploadMsg, acceptType);
                }
                // For Android 5.0+
                public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,WebChromeClient.FileChooserParams fileChooserParams) {
//                if(filePathCallbackLollipop != null)
//                {
//                    filePathCallbackLollipop.onReceiveValue(null);
//                    filePathCallbackLollipop = null;
//                }
//                filePathCallbackLollipop = filePathCallback;
//                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//                i.addCategory(Intent.CATEGORY_OPENABLE);
//                i.setType("image/*");
//
//                startActivityForResult(Intent.createChooser(i,"File Chooser"),2);
//
//                return  true;
                    try {
                        if (filePathCallbackLollipop != null) {
//                    filePathCallbackLollipop.onReceiveValue(null);
                            filePathCallbackLollipop = null;
                        }
                        filePathCallbackLollipop = filePathCallback;


                        // Create AndroidExampleFolder at sdcard
                        File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "AndroidExampleFolder");
                        if (!imageStorageDir.exists()) {
                            // Create AndroidExampleFolder at sdcard
                            imageStorageDir.mkdirs();
                        }

                        // Create camera captured image file path and name
                        File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                        mCapturedImageURI = Uri.fromFile(file);

                        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


                        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);

                        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                        i.addCategory(Intent.CATEGORY_OPENABLE);
                        i.setType("*/*");
                        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        // Create file chooser intent
                        Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
                        // Set camera intent to file chooser
                        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{captureIntent});
                        if (chooserIntent.resolveActivity(getPackageManager()) != null) {
                            // Start the image capture intent to take photo
                            startActivityForResult(chooserIntent, 2);
                        }
                        // On select image call onActivityResult method of activity
                        // startActivityForResult(chooserIntent, 2);
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getBaseContext(), "Exception:"+e,Toast.LENGTH_LONG).show();
                    }
                    return true;
            }






        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.loadUrl("javascript:GetTestMethod()");

            }


        });
        mWebView.loadUrl("http://edepot.nocson.co.kr");

        GetRegID();
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        FirebaseInstanceId.getInstance().getToken();

        TextView mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        new Thread() {
            public void run() {
              //  ShowMessage = invokeHelloWorldWS("이명백", "GetTestMethod");
            }
        }.start();




    }


//    public void newProduct (View view) {
//        DBTest dbHandler = new DBTest(this, null, null, 1);

//        int quantity =1;

//        ProductClass product =
//                new ProductClass("aaaa".toString(), quantity);

//        dbHandler.addProduct(product);

//    }

//    public void removeProduct (View view) {
//        DBTest dbHandler = new DBTest(this, null,
//                null, 1);
//
//      boolean result = dbHandler.deleteProduct("aaaa");
//  }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent
            data) {
        if (requestCode == 1) {
            if (filePathCallbackNormal == null) return;
            Uri result = (data == null || resultCode != RESULT_OK) ? null : data.getData();
            filePathCallbackNormal.onReceiveValue(result);
            filePathCallbackNormal = null;
        } else if (requestCode == 2) {
            Uri[] result = new Uri[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                if(resultCode == RESULT_OK){
                    result = (data == null) ? new Uri[]{mCapturedImageURI} : WebChromeClient.FileChooserParams.parseResult(resultCode, data);
                }
                filePathCallbackLollipop.onReceiveValue(result);
            }
        }
    }




    public final static String SOAP_ACTION = "http://www.nocson.com/";
    public final static String  NAMESPACE  = "http://www.nocson.com/";
    public final static String URL = "http://edepot.nocson.co.kr/ezservice/ezmwebservice.asmx";
    public static String invokeHelloWorldWS(ArrayList param, String webMethName) {
        String resTxt = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo sayHelloPI = new PropertyInfo();
        if(param != null) {
            for (Integer i = 0; i < param.size(); i++) {

                WebViewInterfaceParam ddd = (WebViewInterfaceParam) param.get(i);
                // Set Name
                sayHelloPI.setName(ddd.Name);
                // Set Value
                sayHelloPI.setValue(ddd.Value);
                // Set dataType
                sayHelloPI.setType(String.class);
                // Add the property to request object
                request.addProperty(sayHelloPI);
            }


        }
        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        //Set envelope as dotNet
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
            // Invoke web service
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            // Get the response
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            // Assign it to resTxt variable static variable
            resTxt = response.toString();
            Log.d("12321", resTxt);
        } catch (Exception e) {
            //Print error
            e.printStackTrace();
            //Assign error message to resTxt
            resTxt = "Error occured";

            Log.d("1231", e.getMessage());
        }
        //Return resTxt to calling object
        return resTxt;
    }



    public class WebViewInterface {

        private WebView mAppView;
        private Activity mContext;

        /**
         * 생성자.
         * @param activity : context
         * @param view : 적용될 웹뷰
         */
        public WebViewInterface(Activity activity, WebView view) {
            mAppView = view;
            mContext = activity;
        }
        /**
         * 안드로이드 토스트를 출력한다. Time Long.
         * @param message : 메시지
         */
        @JavascriptInterface
        public void toastLong (String message) {
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        }
        /**
         * 안드로이드 토스트를 출력한다. Time Short.
         * @param message : 메시지
         */
        @JavascriptInterface
        public void toastShort (String message) { // Show toast for a short time
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
    }

    public class WebViewInterfaceParam {
        private  String Name;
        private  String Value;
        public WebViewInterfaceParam(String ParamName, String ParamValue) {
            Name = ParamName;
            Value = ParamValue;
        }
    }




    private FileInputStream mFileInputStream = null;
    private URL connectUrl = null;
    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";
    String mSdPath;

    class HttpFileUpload extends Thread {
        public Long Size;
        public String Path;
        public String FileName;
        HttpFileUpload(){
        }

        public void run(){
            String urlString = "http://edepot.nocson.co.kr/EzService/EzMFileUploader.ashx?FileName=" + FileName + "&TotalSize=" + Long.toString(Size);
            String filepath = "";

            String ext = Environment.getExternalStorageState();

            // /mnt/sdcard/ 폴더 가져오기, 기기마다 sdcard 폴더의 위치가 다르기 때문에 사용한다고 함
            if(ext.equals(Environment.MEDIA_MOUNTED)){
                mSdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            }
            else{
                mSdPath = Environment.MEDIA_UNMOUNTED;
            }

            filepath = Path;
            Log.d("logtest", filepath);

            // TODO Auto-generated method stub
            try {
                mFileInputStream = new FileInputStream(filepath);
                connectUrl = new URL(urlString);
                Log.d("File Up", "mFileInputStream is " + mFileInputStream);
                // open connection
                HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                // write data
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
//                dos.writeBytes(twoHyphens + boundary + lineEnd);

                // uploadedfile 파일이 ashx 핸들러에서 파일을 찾을 때 사용함으로 이름이 반드시 동일해야함..
                // 이름을 바꾸면 ashx 파일에서도 바꿀것.
//                dos.writeBytes("Content-Disposition:form-data;name=\"uploadedfile\";filename=\"" + filepath + "\"" + lineEnd);
//                dos.writeBytes(lineEnd);
                int bytesAvailable = mFileInputStream.available();
                int bufferSize = bytesAvailable; // 1kbyte = 1024;  다른 예제에서는 1kbyte 단위로 잘라서 읽어 들이는데 여기서는 파일 전체를 한번에 읽기

                byte[] buffer = new byte[Integer.parseInt(Long.toString(Size)) ];
                int bytesRead = mFileInputStream.read(buffer, 0, Integer.parseInt(Long.toString(Size)) );
                Log.d("File Up", "image byte is " + bytesRead);
                dos.write(buffer, 0, Integer.parseInt(Long.toString(Size)) );
//                dos.writeBytes(lineEnd);
//                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                // close streams
                Log.e("File Up", "File is written");
                mFileInputStream.close();
                dos.flush(); // 버퍼에 있는 값을 모두 밀어냄

                // 웹서버에서 결과를 받아옴
                int ch;
                InputStream is = conn.getInputStream();
                StringBuffer b = new StringBuffer();
                while ((ch = is.read()) != -1) {
                    b.append((char) ch);
                }
                String s = b.toString();
                Log.e("File Up", "result = " + s);
                dos.close();
            } catch (Exception e) {
                Log.d("File Up", "exception " + e.getMessage());
            }
        }
    }
}


