package com.dynasys.appdisoft.Login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dynasys.appdisoft.Login.DataLocal.DataPreferences;
import com.dynasys.appdisoft.R;

public class WebServicesActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_services);
        if (DataPreferences.getPref("servicio",getApplicationContext())==null){
            ((EditText) findViewById(R.id.webservice_address)).setText("http://173.249.42.116:3050");
        }else{
            ((EditText) findViewById(R.id.webservice_address)).setText(DataPreferences.getPref("servicio",getApplicationContext()));
        }

        ((Button) findViewById(R.id.webservice_savebtn)).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.webservice_savebtn:
                DataPreferences.putPref("servicio",((EditText) findViewById(R.id.webservice_address)).getText().toString(),getApplicationContext());
                this.finish();
                break;
        }
    }
}
