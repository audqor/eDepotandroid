package com.nocson.eDepot;

import android.app.ActivityManager;
import android.content.Context;
import android.os.AsyncTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

public class EzWebServiceTask extends AsyncTask<String,String,String> {
    IEzCallBack uiHandler;
    String[] parmas;
    public EzWebServiceTask(IEzCallBack uiHandler){
        this.uiHandler = uiHandler;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String...strings) {
        parmas = strings;
        return RequestWebMethod(GetURL(parmas[0]),strings[1],null);
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        uiHandler.onCall(s,parmas[0]);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
    public final static String SOAP_ACTION = "http://www.nocson.com/";
    public final static String  NAMESPACE  = "http://www.nocson.com/";

    public final static String GetURL(String url){
        return  url+"/ezservice/ezmwebservice.asmx";
    }

    public static String RequestWebMethod(String url, String webMethName,ArrayList params) {
        String resTxt = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters

        if(params != null) {
            for (Integer i = 0; i < params.size(); i++) {
                PropertyInfo propertyInfo = new PropertyInfo();
                EzWebServiceParam param = (EzWebServiceParam) params.get(i);
                // Set Name
                propertyInfo.setName(param.GetKey());
                // Set Value
                propertyInfo.setValue(param.GetValue());
                // Set dataType
                propertyInfo.setType(String.class);
                // Add the property to request object
                request.addProperty(propertyInfo);
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
        HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
        try {
            // Invoke web service
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            // Get the response
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            // Assign it to resTxt variable static variable
            resTxt = response.toString();

        } catch (Exception e) {
            //Print error
            e.printStackTrace();
            //Assign error message to resTxt
            resTxt = "Error occured";

        }

        return resTxt;
    }



}
