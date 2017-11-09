package com.example.user.kinveyprac;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyPingCallback;

/**
 * Created by Dongmin on 2017-11-09.
 */

public class menus extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "menus";
    private Client Kinvey_CLIENT;

    Button Qr_button;
    TextView qr_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntent();
        setContentView(R.layout.menus);
        Kinvey_CLIENT = LoginActivity.getSharedClient();

        if (Kinvey_CLIENT.isUserLoggedIn()) {
            Log.i(TAG, "Logged in: " + getSharedClient().getActiveUser().getUsername());
            //Toast.makeText(LoginActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
        }

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        findViewById(R.id.LoginButton).setOnClickListener(this);
        findViewById(R.id.signupButton).setOnClickListener(this);
        logout_bt = (Button) findViewById(R.id.logoutButton);
        logout_bt.setEnabled(false);
        findViewById(R.id.emailVerificationButton).setOnClickListener(this);
        Qr_button = (Button) findViewById(R.id.Qr_scan);
        Qr_button.setEnabled(false);
//        findViewById(R.id.getuserdetailButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.LoginButton:
                login(username.getText().toString(), password.getText().toString());
                break;
            case R.id.signupButton:
                userSignUp(username.getText().toString(), password.getText().toString());
                break;
            case R.id.logoutButton:
                userlogout();
                break;
            case R.id.emailVerificationButton:
                emailVerification();
                break;
            case R.id.Qr_scan:
                Intent intent=null;
                intent=new Intent(LoginActivity.this, demodemo.class);
                startActivityForResult(intent, 1);
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String str = data.getStringExtra("QR_text");
                if (!str.equals("")) {
                    Bic_num = str;
                    qr_txt = (TextView) findViewById(R.id.Qr_txt);
                    qr_txt.setText(Bic_num);
                }
            }
        }
    }
}