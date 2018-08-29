package com.nocson.eDepot;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class EzRegistration extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ez_registration);
        Button b = (Button)findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                 String domain = ((Spinner)findViewById(R.id.spinner_domain)).getSelectedItem().toString();
                 String address =   ((EditText)findViewById(R.id.txtUrl)).getText().toString();

                String url = domain.toLowerCase() + "://" + address;


                EzCommonHandler.ValidUrl(url,new IEzCallBack(){
                    @Override
                    public void onCall(String result,String url) {


                        if(result.trim().contains("true")){

                            startActivity(EzCommonHandler.GetNextActivity(getApplicationContext(), url));
                            finish();
                       }
                        else {

                            Toast.makeText(EzRegistration.this,"유효하지 않는 주소입니다.",Toast.LENGTH_LONG);
                        }
                    }
                });
            }
        });
    }
}
