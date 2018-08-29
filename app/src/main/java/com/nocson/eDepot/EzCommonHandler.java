package com.nocson.eDepot;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

public class EzCommonHandler {
    EzDatabaseAccess da;
    EzServerInfo serverInfo;
    public EzCommonHandler(Context parentActivity){
        da = new EzDatabaseAccess(parentActivity,"NSDB",null,1);
        serverInfo = da.GetSettingInfo();
    }
    public String GetDomain()
    {
        return serverInfo.getDomain();
    }

    public String GetURL()
    {
        return serverInfo.getAddress();
    }


    public String GetID()
    {
        return  serverInfo.getAccount();
    }

    public String GetPWD()
    {
        return serverInfo.getPassword();
    }

    public static void ValidUrl(String url, IEzCallBack callback)
    {
        new EzWebServiceTask(callback).execute(url,"IsConnection");
    }

    public static Intent GetNextActivity(Context parentActivity){

        EzDatabaseAccess da = new EzDatabaseAccess(parentActivity,"NSDB",null,1);
        //da.deleteAccount("");
        EzServerInfo setting = da.GetSettingInfo();
        if(setting == null)
            return new Intent(parentActivity,com.nocson.eDepot.EzRegistration.class);
        else {
            return new Intent(parentActivity, com.nocson.eDepot.EzMain.class);
        }
    }

    public static Intent GetNextActivity(Context parentActivity,String uri){

        EzDatabaseAccess da = new EzDatabaseAccess(parentActivity,"NSDB",null,1);
        String[] domainAddress  = uri.split("://");

        EzServerInfo newSetting = new EzServerInfo();
        newSetting.setDomain(domainAddress[0]);
        newSetting.setAddress(domainAddress[1]);
        newSetting.setAccount("");
        newSetting.setPassword("");
        da.addAccount(newSetting);
        EzServerInfo setting = da.GetSettingInfo();
        if(setting == null)
            return new Intent(parentActivity,com.nocson.eDepot.EzRegistration.class);
        else {
            return new Intent(parentActivity,com.nocson.eDepot.
                    EzMain.class);
        }
    }
    public static void setBadge(Context context, int count) {
        final String launcherClassName = getLauncherClassName(context);

        if (launcherClassName == null) {
            return;
        }


        final Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        Bundle extras =intent.getExtras();
        int cnt = 0;
        if(extras != null) {
            cnt += extras.getInt("badge_count") ;
        }
        intent.putExtra("badge_count",count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);

        context.sendBroadcast(intent);
    }

    private static String getLauncherClassName(Context context) {
        final PackageManager pm = context.getPackageManager();
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        final List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);

        for (final ResolveInfo resolveInfo : resolveInfos) {
            final String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }

        return null;
    }
}
