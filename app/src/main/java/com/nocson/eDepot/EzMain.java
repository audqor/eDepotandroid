package com.nocson.eDepot;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Spinner;
import android.widget.Toast;
import android.webkit.WebViewClient;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EzMain extends Activity {
    private WebView webView;
    private EzWebViewInterface webViewInterface;
    private EzCommonHandler commonHandler;
    private ValueCallback<Uri> filePathCallbackNormal;
    private ValueCallback<Uri[]> filePathCallbackLollipop;
    private Uri capturedImageURI;
    public ArrayList<String> paramArray = new ArrayList<String>();

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);


        Bundle extras = intent.getExtras();

        if (extras != null && extras.getInt("notificationId") != 0) {
            int id  = extras.getInt("notificationId");
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel(id);

            webView.loadUrl("javascript:OnAlertPage()");
        }

    }


    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    public void onBackPressed() {

            boolean isCheck = true;

            if(isCheck) {
                webView.loadUrl("javascript:BackClick()");

                return;
            }
            else {
                String curl = webView.getUrl();
                if (curl.toLowerCase().contains("ezmfileview") || curl.toLowerCase().contains("ezmalert") || curl.toLowerCase().contains("ezmproperties") || curl.toLowerCase().contains("ezmfileupload")) {
                    if (webView.canGoBack()) {
                        webView.goBack();
                        return;
                    }
                }
                super.onBackPressed();
            }
    }

    @Override
    protected void onResume() {
        super.onResume();
        EzAlertMessagingService.cnt =0;
        EzCommonHandler.setBadge(this,0);
    }

    boolean isNotify = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ez_main);
        webView = (WebView) findViewById(R.id.activity_main_webview);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setDisplayZoomControls(false);
        if(getIntent().getExtras()!= null) {
            if(getIntent().getExtras().getInt("notificationId") != 0)  {
                isNotify = true;
            }
        }

        webView.setDownloadListener(new DownloadListener(){
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {

                try {
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    request.setMimeType(mimeType);
                    request.addRequestHeader("User-Agent", userAgent);
                    request.setDescription("Downloading file");
                    String fileName = contentDisposition.replace("inline; Filename=", "");
                    fileName = fileName.replace("attachment; Filename=", "");
                    fileName = fileName.replaceAll("\"", "");
                    request.setTitle(fileName);
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                    Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show();
                } catch (Exception e) {

                    if (ContextCompat.checkSelfPermission(EzMain.this,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(EzMain.this,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            Toast.makeText(getBaseContext(), "첨부파일 다운로드를 위해\n동의가 필요합니다.", Toast.LENGTH_LONG).show();
                            ActivityCompat.requestPermissions(EzMain.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    110);
                        } else {
                            Toast.makeText(getBaseContext(), "첨부파일 다운로드를 위해\n동의가 필요합니다.", Toast.LENGTH_LONG).show();
                            ActivityCompat.requestPermissions(EzMain.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    110);
                        }
                    }
                }
            }
        });
        if(getIntent().getExtras()!= null && !isNotify) {
            isNotify = getIntent().getExtras().getBoolean("isNotify");
        }
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webViewInterface = new EzWebViewInterface(EzMain.this, webView);
        webView.addJavascriptInterface(webViewInterface, "Android");
        webView.getSettings().setDomStorageEnabled(true);

        commonHandler = new EzCommonHandler(EzMain.this);


        final Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            handleSendImage(intent); // Handle single image being sent
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            handleSendMultipleImages(intent); // Handle multiple images being sent
        }
        else{
            webView.loadUrl(commonHandler.GetDomain() +"://"+commonHandler.GetURL()+"/mobile/ezmlogin.aspx");
        }

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                try {
                   if (filePathCallbackLollipop != null) {
                        filePathCallbackLollipop = null;
                    }
                    filePathCallbackLollipop = filePathCallback;

                    File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "AndroidExampleFolder");
                    if (!imageStorageDir.exists()) {
                        imageStorageDir.mkdirs();
                    }

                    File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                    capturedImageURI = Uri.fromFile(file);

                    Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageURI);



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


            public void onPermissionRequest(final PermissionRequest request) {
                request.grant(request.getResources());
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String regID = GetRegID();

                if(regID != null && regID.length() > 0 ) {
                    if (url.toLowerCase().contains("ezmlogin")) {
                        if(url.toLowerCase().contains("reconnection=true")) {
                            EzDatabaseAccess da = new EzDatabaseAccess(getApplicationContext(),"NSDB",null,1);
                            da.deleteAccount("");
                            Intent nextActivity = EzCommonHandler.GetNextActivity(getApplicationContext());
                            startActivity(nextActivity);
                            finish();
                        }
                        else {
                            view.loadUrl("javascript:MLogin('" + regID + "','1')");
                        }
                    } else if (url.toLowerCase().contains("ezmusersite")) {
                        if (isNotify) {
                            view.loadUrl("javascript:OnAlertPage()");
                            isNotify = false;
                        }
                    } else if (url.toLowerCase().contains("ezmfileupload")) {
                        String ParamString = new String();
                        for (Integer j = 0; j < paramArray.size(); j++) {
                            ParamString += paramArray.get(j);
                        }
                        if (ParamString != null && ParamString.length() > 0)
                            view.loadUrl("javascript:InitUpload('0'," + ParamString + ",'" + GetRegID() + "',true)");
                        else {
                                view.loadUrl("javascript:InitUpload('1',null,'"+GetRegID()+"',true)");
                          //  view.loadUrl("javascript:InitUpload('1'," + ParamString + ",'" + GetRegID() + "',true)");
                        }
                    }

                }
            }
        });
    }


    private String GetRegID() {
        return FirebaseInstanceId.getInstance().getToken();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (filePathCallbackNormal == null) return;
            Uri result = (data == null || resultCode != RESULT_OK) ? null : data.getData();
            filePathCallbackNormal.onReceiveValue(result);
            filePathCallbackNormal = null;
        } else if (requestCode == 2) {
            Uri[] result = new Uri[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                if(resultCode == RESULT_OK){
                    result = (data == null) ? new Uri[]{capturedImageURI} : WebChromeClient.FileChooserParams.parseResult(resultCode, data);
                }
                if(result == null){
                ClipData mClipData = data.getClipData();
                    ArrayList<Uri> mArrayUri = new ArrayList<>();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        mArrayUri.add(uri);
                    }
                    if(mArrayUri != null && mArrayUri.size() > 0)
                    {
                        int x = mArrayUri.size();
                        result = new Uri[x];
                        result = mArrayUri.toArray(result);
                    }
                }


                filePathCallbackLollipop.onReceiveValue(result);
            }
        }
        else if(requestCode == 3 || requestCode == 4){
            setResult(requestCode);
            finish();
        }

//        try {
//            // When an Image is picked
//            if (requestCode == 2 && resultCode == RESULT_OK
//                    && null != data) {
//                // Get the Image from data
//
//                String[] filePathColumn = { MediaStore.Images.Media.DATA };
//                if(data.getData()!=null){
//
//                    Uri mImageUri=data.getData();
//
//                    // Get the cursor
//                    Cursor cursor = getContentResolver().query(mImageUri,
//                            filePathColumn, null, null, null);
//                    // Move to first row
//                    cursor.moveToFirst();
//
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    cursor.close();
//
//                } else {
//                    if (data.getClipData() != null) {
//                        ClipData mClipData = data.getClipData();
//                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
//                        for (int i = 0; i < mClipData.getItemCount(); i++) {
//
//                            ClipData.Item item = mClipData.getItemAt(i);
//                            Uri uri = item.getUri();
//                            mArrayUri.add(uri);
//                            // Get the cursor
//                            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//                            // Move to first row
//                            cursor.moveToFirst();
//                            int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
//                            long size = cursor.getLong(sizeIndex);
//                            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
//                            String name = cursor.getString(nameIndex);
////                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                            String fPath =  item.getUri().getPath();
////                            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//
//                            cursor.close();
//                            FileUploadStart(uri,name,size,fPath,mClipData.getItemCount(),i);
//
//                        }
//                        Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
//                    }
//                }
//            } else {
//                Toast.makeText(this, "You haven't picked Image",
//                        Toast.LENGTH_LONG).show();
//            }
//        } catch (Exception e) {
//            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
//                    .show();
//        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        ArrayList<Uri> imageUris = new  ArrayList<Uri>();
        imageUris.add(imageUri);
        uploadImage(imageUris);
    }

    void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        uploadImage(imageUris);
    }
    void uploadImage(   ArrayList<Uri> imageUriList)
    {
        paramArray = new ArrayList<String>();
        paramArray.add("[");
        if(imageUriList != null && imageUriList.size() > 0)
            for (Integer i = 0; i < imageUriList.size(); i++) {
                try {
                    final Uri iUri = imageUriList.get(i);
                    Cursor cursor = EzMain.this.getContentResolver().query(iUri,null, null, null, null);
                    cursor.moveToFirst();
                    long size = cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE));
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    String name = cursor.getString(nameIndex);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    String fPath =  cursor.getString(column_index);
                    cursor.close();
                    final   EzUploader  uploadInfo = new EzUploader();
                    uploadInfo.uri =  iUri;
                    uploadInfo.FileName = name;
                    uploadInfo.Size = size;
                    uploadInfo.SourcePath = fPath;


                    uploadInfo.CurrentCount = i;
                    uploadInfo.TotalCount = imageUriList.size();


                    new Thread() {
                        public void run() {
                           final String ID =  uploadInfo.doInBackground(null);
                            if(ID != null && ID.length() > 0)
                            {
                                paramArray.add( "{'StaticName':'" + ID + "', 'FileName':'"+ uploadInfo.FileName + "', 'Size':" + uploadInfo.Size + "},");
                            }
                            if(paramArray.size() == uploadInfo.TotalCount + 1) {
                                String ParamString = new String();
                                paramArray.add("]");

                                webView.loadUrl(commonHandler.GetDomain()+"://"+commonHandler.GetURL()+"/mobile/ezmfileupload.aspx");

                            }

                        }
                    }.start();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }


    }


}
