package com.nocson.eDepot;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.ArrayList;

public class EzAlertIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private String currentToken = null;
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String token = FirebaseInstanceId.getInstance().getToken();
        if(currentToken == null)
        {
            currentToken = token;
        }
        else
        {
            sendRegistrationToServer(token);
        }
        Log.d(TAG, "Refreshed token: " + token);

        // 생성등록된 토큰을 개인 앱서버에 보내 저장해 두었다가 추가 뭔가를 하고 싶으면 할 수 있도록 한다.
//        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.

//        if(currentToken != token)
//        {
//            EzDatabaseAccess da = new EzDatabaseAccess(this,"NSDB",null,1);
//            EzServerInfo   serverInfo = da.GetSettingInfo();
//
//            ArrayList<EzWebServiceParam> paramList= new  ArrayList<EzWebServiceParam>();
//            paramList.add(new EzWebServiceParam("beforeToken",currentToken));
//            paramList.add(new EzWebServiceParam("afterToken",token));
//            EzWebServiceTask.RequestWebMethod(EzWebServiceTask.GetURL(serverInfo.getDomain() + "://" + serverInfo.getAddress()),"ResetToken",paramList);
//            currentToken = token;
//        }

    }
}
